package com.example.prototipoeva3.auth

import com.example.prototipoeva3.permissions.PermissionState

/** Local validation only; it does not determine whether an account exists. */
//Interfáz de resultados de Login local (Credenciales válidas o no)
sealed interface CredentialsState {
    data object Empty : CredentialsState
    data object InvalidFormat : CredentialsState
    data object Valid : CredentialsState
}

//Estado de red del dispositivo (Conectado o desconectado)
enum class ConnectivityState {
    Connected,
    Offline,
}

//Interfáz de clasificación de errores del SDK de autenticación
enum class AuthFailureKind {
    InvalidCredentials,
    NetworkUnavailable,
    ServiceUnavailable,
    Unexpected,
}

/** Safe, provider-independent outcomes. No credentials or raw exceptions are retained. */
//Almacena el resultado devuelto por la interfaz AuthRepository
sealed interface AuthenticationResult {
    data object Success : AuthenticationResult
    data object InvalidCredentials : AuthenticationResult
    data object Unavailable : AuthenticationResult
    data object Failure : AuthenticationResult
}

//Define el estado de la sesión global en la aplicación
sealed interface AuthenticationState {
    data object Authenticated : AuthenticationState
    data object InvalidCredentials : AuthenticationState
    data object Unavailable : AuthenticationState
    data object UnexpectedFailure : AuthenticationState
    data object SignedOut : AuthenticationState
}

//Validación de Credenciales de Ingreso (Campos no vacíos y formato de correo electrónico válido)
fun validateCredentials(email: String, password: String): CredentialsState {
    if (email.isBlank() || password.isBlank()) return CredentialsState.Empty

    val normalizedEmail = email.trim()
    val atIndex = normalizedEmail.indexOf('@')
    val domain = normalizedEmail.substringAfter('@', "")
    val validEmail = atIndex > 0 && domain.contains('.') &&
        !domain.startsWith('.') && !domain.endsWith('.') &&
        !normalizedEmail.any(Char::isWhitespace)

    return if (validEmail) CredentialsState.Valid else CredentialsState.InvalidFormat
}

//Establecimiento de estado luego de Ingreso de Credenciales (Sesión activa o fallos)
fun authStateAfter(result: AuthenticationResult): AuthenticationState = when (result) {
    AuthenticationResult.Success -> AuthenticationState.Authenticated
    AuthenticationResult.InvalidCredentials -> AuthenticationState.InvalidCredentials
    AuthenticationResult.Unavailable -> AuthenticationState.Unavailable
    AuthenticationResult.Failure -> AuthenticationState.UnexpectedFailure
}

//Resultado de estado luego de Ingreso de Credenciales (Sesión activa o fallos)
fun authResultAfterFailure(kind: AuthFailureKind): AuthenticationResult = when (kind) {
    AuthFailureKind.InvalidCredentials -> AuthenticationResult.InvalidCredentials
    AuthFailureKind.NetworkUnavailable,
    AuthFailureKind.ServiceUnavailable -> AuthenticationResult.Unavailable
    AuthFailureKind.Unexpected -> AuthenticationResult.Failure
}

/** A validated Internet-capable network is only a connectivity signal, not a Firebase health check. */
//Verificación de estado de conexión (Internet)
fun connectivityState(hasInternetCapability: Boolean, isValidated: Boolean): ConnectivityState =
    if (hasInternetCapability && isValidated) ConnectivityState.Connected else ConnectivityState.Offline

/** Offline status is informative: an explicit user attempt may still be sent to Firebase. */
//Verificación de estado de credenciales, conexión y permisos
fun canSubmit(
    credentialsState: CredentialsState,
    connectivityState: ConnectivityState,
    permissionState: PermissionState,
): Boolean {
    val hasConnectionState = connectivityState == ConnectivityState.Connected ||
        connectivityState == ConnectivityState.Offline
    return hasConnectionState && credentialsState == CredentialsState.Valid &&
        permissionState == PermissionState.Ready
}

// Mensajes genéricos para cada estado de autenticación
fun messageForAuthState(state: AuthenticationState): String = when (state) {
    AuthenticationState.Authenticated -> "auth_success_message"
    AuthenticationState.InvalidCredentials -> "auth_invalid_credentials_message"
    AuthenticationState.Unavailable -> "auth_service_unavailable_message"
    AuthenticationState.UnexpectedFailure -> "auth_generic_error_message"
    AuthenticationState.SignedOut -> "auth_signed_out_message"
}

//Verificación de estado de autenticación y permisos para ingreso a pantallas protegidas
fun canEnterProtectedScreens(
    authState: AuthenticationState,
    permissionState: PermissionState,
): Boolean = authState == AuthenticationState.Authenticated &&
    permissionState == PermissionState.Ready

//Estado actual del temporizador de sesión (Activa, inactiva con tiempo límite, cerrada)
sealed interface SessionState {
    data object Active : SessionState
    data class AwaitingTimeout(val deadlineMillis: Long) : SessionState
    data object SignedOut : SessionState
}

//Eventos que afectan al temporizador de sesión
sealed interface SessionEvent {
    data object Backgrounded : SessionEvent //Usuario minímizo la aplicación
    data object UserInteracted : SessionEvent //Usuario interactuó con la aplicación
    data object TimeoutReached : SessionEvent //Paso el tiempo límite de la sesión
    data object Logout : SessionEvent
}

//Instrucciones que debe ejecutar la aplicación según los eventos que efectén al temporizador de sesión
sealed interface SessionAction {
    data object KeepSession : SessionAction //Mantener la sesión activa
    data class ScheduleSignOut(val deadlineMillis: Long) : SessionAction //Programar cierre de sesión
    data object CancelScheduledSignOut : SessionAction //Cancelar la temporización (el usuario volvió)
    data object SignOut : SessionAction
}

//Variable y Función de verificación tiempo en que la sesión puede permanecer activa sin interacción del usuario
private const val SESSION_TIMEOUT_MILLIS = 5 * 60 * 1000L

/** Checks a persisted deadline using an injected clock value. */
fun isSessionExpired(deadlineMillis: Long?, nowMillis: Long): Boolean =
    deadlineMillis != null && deadlineMillis > 0L && nowMillis >= deadlineMillis

/** Pure session policy. The platform adapter owns timers and supplies the current time. */
//Función para validar estado de tiempo de la sesión y cerrarla en cuanto corresponda
fun sessionAction(
    state: SessionState,
    event: SessionEvent,
    nowMillis: Long,
): SessionAction = when (event) {
    //Cierre de sesión hecho por el usuario
    SessionEvent.Logout -> SessionAction.SignOut

    //Al minímizar la aplicación
    SessionEvent.Backgrounded -> when (state) {
        //Establecer tiempo límite para cerrar la sesión
        SessionState.Active -> SessionAction.ScheduleSignOut(nowMillis + SESSION_TIMEOUT_MILLIS)
        is SessionState.AwaitingTimeout -> SessionAction.ScheduleSignOut(state.deadlineMillis)
        SessionState.SignedOut -> SessionAction.KeepSession
    }

    //El usuario regresa e interactúa con la aplicación
    SessionEvent.UserInteracted -> when (state) {
        //Si estado estaba en espera de tiempo (AwaitingTimeout), mantener la sesión
        is SessionState.AwaitingTimeout -> if (nowMillis < state.deadlineMillis) {
            SessionAction.CancelScheduledSignOut
        } else {
            SessionAction.SignOut
        }
        SessionState.Active -> SessionAction.KeepSession
        SessionState.SignedOut -> SessionAction.KeepSession
    }

    //El tiempo límite de la sesión ha pasado
    SessionEvent.TimeoutReached -> when (state) {
        //La sesión se cierra de forma autónoma
        is SessionState.AwaitingTimeout -> if (nowMillis >= state.deadlineMillis) {
            SessionAction.SignOut
        } else {
            SessionAction.KeepSession
        }
        SessionState.Active, SessionState.SignedOut -> SessionAction.KeepSession
    }
}
