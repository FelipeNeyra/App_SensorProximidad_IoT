package com.example.prototipoeva3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.example.prototipoeva3.auth.AuthRepository
import com.example.prototipoeva3.auth.AuthenticationResult
import com.example.prototipoeva3.auth.AuthenticationState
import com.example.prototipoeva3.auth.ConnectivityMonitor
import com.example.prototipoeva3.auth.ConnectivityState
import com.example.prototipoeva3.auth.FirebaseAuthRepository
import com.example.prototipoeva3.auth.SessionLifecycle
import com.example.prototipoeva3.auth.authStateAfter
import com.example.prototipoeva3.auth.canEnterProtectedScreens
import com.example.prototipoeva3.auth.canSubmit
import com.example.prototipoeva3.auth.messageForAuthState
import com.example.prototipoeva3.auth.validateCredentials
import com.example.prototipoeva3.permissions.PermissionState
import com.example.prototipoeva3.permissions.nextPermissionState
import com.example.prototipoeva3.permissions.permissionsToRequest
import com.example.prototipoeva3.ui.screens.ConfigurationScreen
import com.example.prototipoeva3.ui.screens.LoginScreen
import com.example.prototipoeva3.ui.screens.PermissionBlockedScreen
import com.example.prototipoeva3.ui.screens.ValuesScreen
import com.example.prototipoeva3.ui.theme.PrototipoEVA3Theme
import kotlinx.coroutines.launch

private const val PHASE_READY = 0
private const val PHASE_INITIAL_REQUEST = 1
private const val PHASE_BLOCKED_RETRYABLE = 2
private const val PHASE_RETRY_REQUEST = 3
private const val PHASE_BLOCKED_INDEFINITELY = 4

private enum class AppScreen {
    LOGIN,
    CONFIGURATION,
    VALUES,
}

class MainActivity : ComponentActivity() {
    private lateinit var authRepository: AuthRepository
    private lateinit var sessionLifecycle: SessionLifecycle
    private var onSessionExpired: (() -> Unit)? = null
    private var refreshPermissions: (() -> Unit)? = null
    private var sessionExpiredAtLaunch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authRepository = FirebaseAuthRepository()
        sessionLifecycle = SessionLifecycle(
            context = this,
            signOut = authRepository::signOut,
            onExpired = {
                sessionExpiredAtLaunch = true
                onSessionExpired?.invoke()
            },
        )
        sessionLifecycle.checkExpirationOnLaunch()

        val requiredPermissions = requiredRuntimePermissions()
        val initiallyGranted = requiredPermissions.all(::isPermissionGranted)

        setContent {
            PrototipoEVA3Theme {
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()
                val connectivityMonitor = remember { ConnectivityMonitor(this) }
                var connectivityState by remember {
                    mutableStateOf(connectivityMonitor.currentState())
                }
                var permissionDenialCount by rememberSaveable { mutableIntStateOf(0) }
                var permissionPhase by rememberSaveable {
                    mutableIntStateOf(
                        if (initiallyGranted) PHASE_READY else PHASE_INITIAL_REQUEST,
                    )
                }
                var authState by remember {
                    mutableStateOf(
                        if (!sessionExpiredAtLaunch && authRepository.currentUser() != null) {
                            AuthenticationState.Authenticated
                        } else {
                            AuthenticationState.SignedOut
                        },
                    )
                }
                var authMessage by remember {
                    mutableStateOf(
                        if (sessionExpiredAtLaunch) getString(R.string.session_expired_message)
                        else null,
                    )
                }
                var isSigningIn by remember { mutableStateOf(false) }
                var currentScreenName by rememberSaveable {
                    mutableStateOf(
                        if (authState == AuthenticationState.Authenticated) {
                            AppScreen.CONFIGURATION.name
                        } else {
                            AppScreen.LOGIN.name
                        },
                    )
                }

                DisposableEffect(Unit) {
                    onSessionExpired = {
                        authState = AuthenticationState.SignedOut
                        authMessage = getString(R.string.session_expired_message)
                    }
                    refreshPermissions = {
                        if (requiredPermissions.all(::isPermissionGranted)) {
                            permissionDenialCount = 0
                            permissionPhase = PHASE_READY
                        }
                    }
                    onDispose {
                        onSessionExpired = null
                        refreshPermissions = null
                    }
                }

                DisposableEffect(connectivityMonitor) {
                    connectivityMonitor.start { connectivityState = it }
                    onDispose { connectivityMonitor.stop() }
                }

                LaunchedEffect(authState) {
                    currentScreenName = if (authState == AuthenticationState.Authenticated) {
                        AppScreen.CONFIGURATION.name
                    } else {
                        AppScreen.LOGIN.name
                    }
                }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                ) { result ->
                    val requesting = permissionStateForPhase(
                        phase = permissionPhase,
                        deniedPermissions = result.filterValues { !it }.keys,
                        denialCount = permissionDenialCount,
                    ) as PermissionState.Requesting
                    val nextState = nextPermissionState(requesting, result)
                    val denied = result.filterValues { !it }.keys
                    val permanentlyDenied = denied.any { permission ->
                        !shouldShowRequestPermissionRationale(permission)
                    }

                    permissionPhase = when {
                        nextState == PermissionState.Ready -> {
                            permissionDenialCount = 0
                            PHASE_READY
                        }
                        permanentlyDenied || nextState is PermissionState.BlockedIndefinitely -> {
                            permissionDenialCount = maxOf(permissionDenialCount + 1, 3)
                            PHASE_BLOCKED_INDEFINITELY
                        }
                        nextState is PermissionState.BlockedRetryable -> {
                            permissionDenialCount = nextState.denialCount
                            PHASE_BLOCKED_RETRYABLE
                        }
                        else -> PHASE_READY
                    }
                }

                LaunchedEffect(permissionPhase) {
                    if (permissionPhase == PHASE_INITIAL_REQUEST ||
                        permissionPhase == PHASE_RETRY_REQUEST
                    ) {
                        val granted = requiredPermissions.filter(::isPermissionGranted).toSet()
                        val missing = permissionsToRequest(requiredPermissions, granted)
                        if (missing.isEmpty()) {
                            permissionDenialCount = 0
                            permissionPhase = PHASE_READY
                        } else {
                            permissionLauncher.launch(missing.toTypedArray())
                        }
                    }
                }

                val grantedPermissions = requiredPermissions.filter(::isPermissionGranted).toSet()
                val deniedPermissions = permissionsToRequest(requiredPermissions, grantedPermissions)
                val permissionState = permissionStateForPhase(
                    phase = permissionPhase,
                    deniedPermissions = deniedPermissions,
                    denialCount = permissionDenialCount,
                )
                val bluetoothMessage = stringResource(R.string.bluetooth_conceptual_message)
                val wifiMessage = stringResource(R.string.wifi_conceptual_message)
                val connectivityMessage = stringResource(
                    if (connectivityState == ConnectivityState.Connected) {
                        R.string.internet_connected_message
                    } else {
                        R.string.internet_unavailable_message
                    },
                )
                val permissionInformation = stringResource(R.string.internet_permission_information)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                ) { innerPadding ->
                    if (permissionState != PermissionState.Ready) {
                        PermissionBlockedScreen(
                            isIndefinite = permissionState is PermissionState.BlockedIndefinitely,
                            isInitialRequest = permissionState is PermissionState.Requesting &&
                                !permissionState.isRetry,
                            onRetry = { permissionPhase = PHASE_RETRY_REQUEST },
                            onOpenSettings = {
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", packageName, null),
                                )
                                startActivity(intent)
                            },
                            modifier = Modifier.fillMaxSize().padding(innerPadding),
                        )
                    } else if (!canEnterProtectedScreens(authState, permissionState)) {
                        LoginScreen(
                            onSignIn = { email, password ->
                                when (validateCredentials(email, password)) {
                                    com.example.prototipoeva3.auth.CredentialsState.Empty -> {
                                        authMessage = getString(R.string.login_empty_fields)
                                    }
                                    com.example.prototipoeva3.auth.CredentialsState.InvalidFormat -> {
                                        authMessage = getString(R.string.login_invalid_email)
                                    }
                                    com.example.prototipoeva3.auth.CredentialsState.Valid -> {
                                        if (!canSubmit(
                                                com.example.prototipoeva3.auth.CredentialsState.Valid,
                                                connectivityState,
                                                permissionState,
                                            )
                                        ) {
                                            authMessage = getString(R.string.permission_denied_message)
                                        } else {
                                            isSigningIn = true
                                            authMessage = if (connectivityState == ConnectivityState.Offline) {
                                                getString(R.string.internet_unavailable_message)
                                            } else {
                                                null
                                            }
                                            authRepository.signIn(email.trim(), password) { result ->
                                                runOnUiThread {
                                                    isSigningIn = false
                                                    authState = authStateAfter(result)
                                                    if (result != AuthenticationResult.Success) {
                                                        authMessage = if (
                                                            connectivityState == ConnectivityState.Offline
                                                        ) {
                                                            getString(R.string.internet_unavailable_message)
                                                        } else {
                                                            getString(messageResource(authState))
                                                        }
                                                    } else {
                                                        authMessage = null
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            isLoading = isSigningIn,
                            message = authMessage,
                            connectivityMessage = connectivityMessage,
                            permissionInformation = permissionInformation,
                            modifier = Modifier.fillMaxSize().padding(innerPadding),
                        )
                    } else {
                        when (AppScreen.valueOf(currentScreenName)) {
                            AppScreen.LOGIN -> LoginScreen(
                                onSignIn = { _, _ -> },
                                isLoading = false,
                                message = authMessage,
                                connectivityMessage = connectivityMessage,
                                permissionInformation = permissionInformation,
                                modifier = Modifier.fillMaxSize().padding(innerPadding),
                            )

                            AppScreen.CONFIGURATION -> ConfigurationScreen(
                                onBluetoothClick = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(bluetoothMessage)
                                    }
                                },
                                onWifiClick = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(wifiMessage)
                                    }
                                },
                                onValuesClick = { currentScreenName = AppScreen.VALUES.name },
                                onSignOut = {
                                    sessionLifecycle.onExplicitSignOut()
                                    authRepository.signOut()
                                    authState = AuthenticationState.SignedOut
                                    authMessage = getString(R.string.auth_signed_out_message)
                                },
                                modifier = Modifier.fillMaxSize().padding(innerPadding),
                            )

                            AppScreen.VALUES -> ValuesScreen(
                                onBack = { currentScreenName = AppScreen.CONFIGURATION.name },
                                modifier = Modifier.fillMaxSize().padding(innerPadding),
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (::sessionLifecycle.isInitialized) sessionLifecycle.onForeground()
    }

    override fun onResume() {
        super.onResume()
        refreshPermissions?.invoke()
    }

    override fun onStop() {
        if (::sessionLifecycle.isInitialized && ::authRepository.isInitialized) {
            sessionLifecycle.onBackground(authRepository.currentUser() != null)
        }
        super.onStop()
    }

    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun requiredRuntimePermissions(): Set<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            setOf(Manifest.permission.NEARBY_WIFI_DEVICES)
        } else {
            emptySet()
        }
}

private fun permissionStateForPhase(
    phase: Int,
    deniedPermissions: Set<String>,
    denialCount: Int,
): PermissionState = when (phase) {
    PHASE_INITIAL_REQUEST -> PermissionState.Requesting(isRetry = false)
    PHASE_BLOCKED_RETRYABLE -> PermissionState.BlockedRetryable(deniedPermissions, denialCount)
    PHASE_RETRY_REQUEST -> PermissionState.Requesting(isRetry = true, priorDenials = denialCount)
    PHASE_BLOCKED_INDEFINITELY -> PermissionState.BlockedIndefinitely(deniedPermissions)
    else -> PermissionState.Ready
}

private fun messageResource(state: AuthenticationState): Int = when (state) {
    AuthenticationState.Authenticated -> R.string.auth_success_message
    AuthenticationState.InvalidCredentials -> R.string.auth_invalid_credentials_message
    AuthenticationState.Unavailable -> R.string.auth_service_unavailable_message
    AuthenticationState.UnexpectedFailure -> R.string.auth_generic_error_message
    AuthenticationState.SignedOut -> R.string.auth_signed_out_message
}
