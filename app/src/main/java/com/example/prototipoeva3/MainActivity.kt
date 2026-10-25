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
import com.example.prototipoeva3.ui.screens.SecurityMonitorScreen
import com.example.prototipoeva3.ui.screens.ValuesScreen
import com.google.firebase.auth.FirebaseAuth
import com.example.prototipoeva3.ui.theme.PrototipoEVA3Theme
import kotlinx.coroutines.launch

//Variables de validación de intentos de solicitud de permisos
private const val PHASE_READY = 0
private const val PHASE_INITIAL_REQUEST = 1
private const val PHASE_BLOCKED_RETRYABLE = 2
private const val PHASE_RETRY_REQUEST = 3
private const val PHASE_BLOCKED_INDEFINITELY = 4

//Instancias de pantallas principales
private enum class AppScreen {
    LOGIN,
    CONFIGURATION,
    VALUES,
}

class MainActivity : ComponentActivity() {
    //Instancias de clases de Firebase Authenticator y Ciclo de Vida de la Sesión
    private lateinit var authRepository: AuthRepository
    private lateinit var sessionLifecycle: SessionLifecycle

    //Variables de comprobación de estado de sesión
    private var onSessionExpired: (() -> Unit)? = null
    private var refreshPermissions: (() -> Unit)? = null
    private var sessionExpiredAtLaunch = false

    //--- Funciones de Ciclo Principal (onCreate -> onStart -> onResume -> onStop)

    override fun onCreate(savedInstanceState: Bundle?) {
        //Inicialización de Firebase Authenticator y Sesión actual
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
        //Verificación de estado de sesión cerrada y el tiempo de inactividad expiro si la app estaba cerrada
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

                //Validación de sesión activa y usuario ingresado para persistencia de sesión
                var authState by remember {
                    mutableStateOf(
                        if (!sessionExpiredAtLaunch && authRepository.currentUser() != null) {
                            AuthenticationState.Authenticated
                        } else {
                            AuthenticationState.SignedOut
                        },
                    )
                }

                //Mensaje de 'Expiración de Sesión' en pantalla de Login
                var authMessage by remember {
                    mutableStateOf(
                        if (sessionExpiredAtLaunch) getString(R.string.session_expired_message)
                        else null,
                    )
                }

                //Retorno de pantalla correspondiente según autenticación o no
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

                //Registro de cierre de sesión automático y limpieza de sus configuraciones una vez realizadas
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
                        //Se muestra pantalla de Login si el usuario no dispone de credenciales válidas
                        LoginScreen(
                            onSignIn = { email, password ->
                                when (validateCredentials(email, password)) {
                                    //Validaciones locales desde AuthFlow.kt
                                    com.example.prototipoeva3.auth.CredentialsState.Empty -> {
                                        authMessage = getString(R.string.login_empty_fields)
                                    }
                                    com.example.prototipoeva3.auth.CredentialsState.InvalidFormat -> {
                                        authMessage = getString(R.string.login_invalid_email)
                                    }
                                    com.example.prototipoeva3.auth.CredentialsState.Valid -> {
                                        //Validación de Conectividad y Permisos (en caso de haber pasado las credenciales)
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
                                            //Validación de credenciales en Firebase Authentication
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
                            //Mensajes de muestra y variables de control
                            isLoading = isSigningIn,
                            message = authMessage,
                            connectivityMessage = connectivityMessage,
                            permissionInformation = permissionInformation,
                            modifier = Modifier.fillMaxSize().padding(innerPadding),
                        )
                    } else {
                        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
                        val isSecurityUser = currentUserEmail.contains("seguridad", ignoreCase = true)

                        if (isSecurityUser) {
                            SecurityMonitorScreen(
                                onSignOut = {
                                    sessionLifecycle.onExplicitSignOut()
                                    authRepository.signOut()
                                    authState = AuthenticationState.SignedOut
                                    authMessage = getString(R.string.auth_signed_out_message)
                                },
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
                                isBluetoothGranted = isBluetoothGranted(),
                                isWifiGranted = isWifiGranted(),
                                onBluetoothClick = {
                                    if (isBluetoothGranted()) {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Bluetooth está activo y con permisos concedidos para conectar con el Arduino.")
                                        }
                                    } else {
                                        val btPerms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                            arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
                                        } else {
                                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                                        }
                                        val permanentlyDenied = btPerms.any { !shouldShowRequestPermissionRationale(it) && !isPermissionGranted(it) }
                                        if (permanentlyDenied) {
                                            val intent = Intent(
                                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", packageName, null),
                                            )
                                            startActivity(intent)
                                        } else {
                                            permissionPhase = PHASE_RETRY_REQUEST
                                        }
                                    }
                                },
                                onWifiClick = {
                                    if (isWifiGranted()) {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Wi-Fi está activo y operativo para conectar con Firebase.")
                                        }
                                    } else {
                                        val wifiPerms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            arrayOf(Manifest.permission.NEARBY_WIFI_DEVICES)
                                        } else {
                                            emptyArray()
                                        }
                                        val permanentlyDenied = wifiPerms.any { !shouldShowRequestPermissionRationale(it) && !isPermissionGranted(it) }
                                        if (permanentlyDenied) {
                                            val intent = Intent(
                                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", packageName, null),
                                            )
                                            startActivity(intent)
                                        } else {
                                            permissionPhase = PHASE_RETRY_REQUEST
                                        }
                                    }
                                },
                                onValuesClick = { currentScreenName = AppScreen.VALUES.name },
                                onSignOut = {
                                    sessionLifecycle.onExplicitSignOut()
                                    authRepository.signOut()
                                    authState = AuthenticationState.SignedOut
                                    authMessage = getString(R.string.auth_signed_out_message)
                                },
                                onCheckSecurityUserExists = { secEmail, onResult ->
                                    coroutineScope.launch {
                                        val result = authRepository.checkUserExistsInAuth(secEmail)
                                        onResult(result.getOrDefault(false))
                                    }
                                },
                                onLinkSecurityUser = { secEmail ->
                                    authRepository.linkSecurityUser(secEmail)
                                },
                                onUnlinkSecurityUser = {
                                    authRepository.unlinkSecurityUser()
                                },
                                linkedSecurityEmail = authRepository.getLinkedSecurityUserEmail(),
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
    }

    //Función predeterminada de Inicio (Se ejecuta cada vez que la App sea abierta)
    //Verifica el estado de la sesión (Activa o cerrada según tiempo)
    override fun onStart() {
        super.onStart()
        if (::sessionLifecycle.isInitialized) sessionLifecycle.onForeground()
    }

    //Función predeterminada de Reanudar ()
    override fun onResume() {
        super.onResume()
        refreshPermissions?.invoke()
    }

    //Función predeterminada de cierre (Se ejecuta cada vez que la App sea minímizada)
    //Implementa el temporizador de 5 minutos para cerrar la sesión automáticamente
    override fun onStop() {
        if (::sessionLifecycle.isInitialized && ::authRepository.isInitialized) {
            sessionLifecycle.onBackground(authRepository.currentUser() != null)
        }
        super.onStop()
    }

    // --- Funciones Propias ---
    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun isBluetoothGranted(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            isPermissionGranted(Manifest.permission.BLUETOOTH_SCAN) &&
                isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
        }

    private fun isWifiGranted(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isPermissionGranted(Manifest.permission.NEARBY_WIFI_DEVICES)
        } else {
            true
        }

    private fun requiredRuntimePermissions(): Set<String> {
        val permissions = mutableSetOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        return permissions
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

//Mensajes de muestra al usuario según pase su ingreso desde Firebase Authenticator
private fun messageResource(state: AuthenticationState): Int = when (state) {
    AuthenticationState.Authenticated -> R.string.auth_success_message
    AuthenticationState.InvalidCredentials -> R.string.auth_invalid_credentials_message
    AuthenticationState.Unavailable -> R.string.auth_service_unavailable_message
    AuthenticationState.UnexpectedFailure -> R.string.auth_generic_error_message
    AuthenticationState.SignedOut -> R.string.auth_signed_out_message
}
