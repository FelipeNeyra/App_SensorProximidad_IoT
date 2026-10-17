package com.example.prototipoeva3

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.example.prototipoeva3.permissions.PermissionState
import com.example.prototipoeva3.permissions.canNavigate
import com.example.prototipoeva3.permissions.nextPermissionState
import com.example.prototipoeva3.permissions.permissionsToRequest
import com.example.prototipoeva3.ui.screens.ConfigurationScreen
import com.example.prototipoeva3.ui.screens.LoginScreen
import com.example.prototipoeva3.ui.screens.PermissionBlockedScreen
import com.example.prototipoeva3.ui.screens.ValuesScreen
import com.example.prototipoeva3.ui.theme.PrototipoEVA3Theme
import kotlinx.coroutines.launch

private const val PHASE_READY = 0
private const val PHASE_INITIAL_PROMPT = 1
private const val PHASE_INITIAL_REQUEST = 2
private const val PHASE_BLOCKED_RETRYABLE = 3
private const val PHASE_RETRY_REQUEST = 4
private const val PHASE_BLOCKED_INDEFINITELY = 5

private enum class AppScreen {
    LOGIN,
    CONFIGURATION,
    VALUES
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requiredPermissions = requiredRuntimePermissions()
        val initiallyGranted = requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }

        setContent {
            PrototipoEVA3Theme {
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()
                var permissionPhase by rememberSaveable {
                    mutableIntStateOf(
                        if (initiallyGranted) PHASE_READY else PHASE_INITIAL_PROMPT
                    )
                }
                var currentScreenName by rememberSaveable {
                    mutableStateOf(AppScreen.LOGIN.name)
                }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { result ->
                    val currentState = permissionStateForPhase(
                        phase = permissionPhase,
                        deniedPermissions = requiredPermissions - result.filterValues { it }.keys
                    )
                    val nextState = nextPermissionState(currentState, result)
                    permissionPhase = phaseForPermissionState(nextState)
                }

                LaunchedEffect(permissionPhase) {
                    if (permissionPhase == PHASE_INITIAL_REQUEST ||
                        permissionPhase == PHASE_RETRY_REQUEST
                    ) {
                        val granted = requiredPermissions.filter { permission ->
                            ContextCompat.checkSelfPermission(
                                this@MainActivity,
                                permission
                            ) == PackageManager.PERMISSION_GRANTED
                        }.toSet()
                        val missing = permissionsToRequest(requiredPermissions, granted)
                        if (missing.isEmpty()) {
                            permissionPhase = PHASE_READY
                        } else {
                            permissionLauncher.launch(missing.toTypedArray())
                        }
                    }
                }

                val grantedPermissions = requiredPermissions.filter { permission ->
                    ContextCompat.checkSelfPermission(this, permission) ==
                        PackageManager.PERMISSION_GRANTED
                }.toSet()
                val deniedPermissions = permissionsToRequest(requiredPermissions, grantedPermissions)
                val permissionState = permissionStateForPhase(permissionPhase, deniedPermissions)
                val bluetoothMessage = stringResource(R.string.bluetooth_conceptual_message)
                val wifiMessage = stringResource(R.string.wifi_conceptual_message)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    if (!canNavigate(permissionState)) {
                        PermissionBlockedScreen(
                            isIndefinite = permissionState is PermissionState.BlockedIndefinitely,
                            isInitialRequest = permissionState is PermissionState.Requesting &&
                                !permissionState.isRetry,
                            onRetry = {
                                permissionPhase = if (permissionPhase == PHASE_INITIAL_PROMPT) {
                                    PHASE_INITIAL_REQUEST
                                } else {
                                    PHASE_RETRY_REQUEST
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    } else {
                        when (AppScreen.valueOf(currentScreenName)) {
                            AppScreen.LOGIN -> LoginScreen(
                                onContinue = { currentScreenName = AppScreen.CONFIGURATION.name },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
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
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )

                            AppScreen.VALUES -> ValuesScreen(
                                onBack = { currentScreenName = AppScreen.CONFIGURATION.name },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requiredRuntimePermissions(): Set<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            setOf(Manifest.permission.NEARBY_WIFI_DEVICES)
        } else {
            emptySet()
        }
}

private fun permissionStateForPhase(
    phase: Int,
    deniedPermissions: Set<String>
): PermissionState = when (phase) {
    PHASE_INITIAL_PROMPT -> PermissionState.Requesting(isRetry = false)
    PHASE_INITIAL_REQUEST -> PermissionState.Requesting(isRetry = false)
    PHASE_BLOCKED_RETRYABLE -> PermissionState.BlockedRetryable(deniedPermissions)
    PHASE_RETRY_REQUEST -> PermissionState.Requesting(isRetry = true)
    PHASE_BLOCKED_INDEFINITELY -> PermissionState.BlockedIndefinitely(deniedPermissions)
    else -> PermissionState.Ready
}

private fun phaseForPermissionState(state: PermissionState): Int = when (state) {
    PermissionState.Ready -> PHASE_READY
    is PermissionState.Requesting -> if (state.isRetry) PHASE_RETRY_REQUEST else PHASE_INITIAL_REQUEST
    is PermissionState.BlockedRetryable -> PHASE_BLOCKED_RETRYABLE
    is PermissionState.BlockedIndefinitely -> PHASE_BLOCKED_INDEFINITELY
}
