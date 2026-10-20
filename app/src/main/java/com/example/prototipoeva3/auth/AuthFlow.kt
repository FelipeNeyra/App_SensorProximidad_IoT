package com.example.prototipoeva3.auth

import com.example.prototipoeva3.permissions.PermissionState

/** Local validation only; it does not determine whether an account exists. */
sealed interface CredentialsState {
    data object Empty : CredentialsState
    data object InvalidFormat : CredentialsState
    data object Valid : CredentialsState
}

enum class ConnectivityState {
    Connected,
    Offline,
}

enum class AuthFailureKind {
    InvalidCredentials,
    NetworkUnavailable,
    ServiceUnavailable,
    Unexpected,
}

/** Safe, provider-independent outcomes. No credentials or raw exceptions are retained. */
sealed interface AuthenticationResult {
    data object Success : AuthenticationResult
    data object InvalidCredentials : AuthenticationResult
    data object Unavailable : AuthenticationResult
    data object Failure : AuthenticationResult
}

sealed interface AuthenticationState {
    data object Authenticated : AuthenticationState
    data object InvalidCredentials : AuthenticationState
    data object Unavailable : AuthenticationState
    data object UnexpectedFailure : AuthenticationState
    data object SignedOut : AuthenticationState
}

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

fun authStateAfter(result: AuthenticationResult): AuthenticationState = when (result) {
    AuthenticationResult.Success -> AuthenticationState.Authenticated
    AuthenticationResult.InvalidCredentials -> AuthenticationState.InvalidCredentials
    AuthenticationResult.Unavailable -> AuthenticationState.Unavailable
    AuthenticationResult.Failure -> AuthenticationState.UnexpectedFailure
}

fun authResultAfterFailure(kind: AuthFailureKind): AuthenticationResult = when (kind) {
    AuthFailureKind.InvalidCredentials -> AuthenticationResult.InvalidCredentials
    AuthFailureKind.NetworkUnavailable,
    AuthFailureKind.ServiceUnavailable -> AuthenticationResult.Unavailable
    AuthFailureKind.Unexpected -> AuthenticationResult.Failure
}

/** A validated Internet-capable network is only a connectivity signal, not a Firebase health check. */
fun connectivityState(hasInternetCapability: Boolean, isValidated: Boolean): ConnectivityState =
    if (hasInternetCapability && isValidated) ConnectivityState.Connected else ConnectivityState.Offline

/** Offline status is informative: an explicit user attempt may still be sent to Firebase. */
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

fun messageForAuthState(state: AuthenticationState): String = when (state) {
    AuthenticationState.Authenticated -> "auth_success_message"
    AuthenticationState.InvalidCredentials -> "auth_invalid_credentials_message"
    AuthenticationState.Unavailable -> "auth_service_unavailable_message"
    AuthenticationState.UnexpectedFailure -> "auth_generic_error_message"
    AuthenticationState.SignedOut -> "auth_signed_out_message"
}

fun canEnterProtectedScreens(
    authState: AuthenticationState,
    permissionState: PermissionState,
): Boolean = authState == AuthenticationState.Authenticated &&
    permissionState == PermissionState.Ready

sealed interface SessionState {
    data object Active : SessionState
    data class AwaitingTimeout(val deadlineMillis: Long) : SessionState
    data object SignedOut : SessionState
}

sealed interface SessionEvent {
    data object Backgrounded : SessionEvent
    data object UserInteracted : SessionEvent
    data object TimeoutReached : SessionEvent
    data object Logout : SessionEvent
}

sealed interface SessionAction {
    data object KeepSession : SessionAction
    data class ScheduleSignOut(val deadlineMillis: Long) : SessionAction
    data object CancelScheduledSignOut : SessionAction
    data object SignOut : SessionAction
}

private const val SESSION_TIMEOUT_MILLIS = 5 * 60 * 1000L

/** Checks a persisted deadline using an injected clock value. */
fun isSessionExpired(deadlineMillis: Long?, nowMillis: Long): Boolean =
    deadlineMillis != null && deadlineMillis > 0L && nowMillis >= deadlineMillis

/** Pure session policy. The platform adapter owns timers and supplies the current time. */
fun sessionAction(
    state: SessionState,
    event: SessionEvent,
    nowMillis: Long,
): SessionAction = when (event) {
    SessionEvent.Logout -> SessionAction.SignOut
    SessionEvent.Backgrounded -> when (state) {
        SessionState.Active -> SessionAction.ScheduleSignOut(nowMillis + SESSION_TIMEOUT_MILLIS)
        is SessionState.AwaitingTimeout -> SessionAction.ScheduleSignOut(state.deadlineMillis)
        SessionState.SignedOut -> SessionAction.KeepSession
    }
    SessionEvent.UserInteracted -> when (state) {
        is SessionState.AwaitingTimeout -> if (nowMillis < state.deadlineMillis) {
            SessionAction.CancelScheduledSignOut
        } else {
            SessionAction.SignOut
        }
        SessionState.Active -> SessionAction.KeepSession
        SessionState.SignedOut -> SessionAction.KeepSession
    }
    SessionEvent.TimeoutReached -> when (state) {
        is SessionState.AwaitingTimeout -> if (nowMillis >= state.deadlineMillis) {
            SessionAction.SignOut
        } else {
            SessionAction.KeepSession
        }
        SessionState.Active, SessionState.SignedOut -> SessionAction.KeepSession
    }
}
