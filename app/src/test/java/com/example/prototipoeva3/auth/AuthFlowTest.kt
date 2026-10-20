package com.example.prototipoeva3.auth

import com.example.prototipoeva3.permissions.PermissionState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthFlowTest {
    @Test
    fun validatesEmptyMalformedAndAcceptableCredentials() {
        assertEquals(CredentialsState.Empty, validateCredentials("", ""))
        assertEquals(CredentialsState.Empty, validateCredentials("user@example.com", " "))
        assertEquals(CredentialsState.InvalidFormat, validateCredentials("not-an-email", "pw"))
        assertEquals(CredentialsState.InvalidFormat, validateCredentials("user@domain", "pw"))
        assertEquals(CredentialsState.Valid, validateCredentials(" user@example.com ", "pw"))
    }

    @Test
    fun authenticationResultsMapToSafeStatesAndGenericMessageIds() {
        assertEquals(
            AuthenticationState.Authenticated,
            authStateAfter(AuthenticationResult.Success),
        )
        assertEquals(
            AuthenticationState.InvalidCredentials,
            authStateAfter(AuthenticationResult.InvalidCredentials),
        )
        assertEquals(
            AuthenticationState.Unavailable,
            authStateAfter(AuthenticationResult.Unavailable),
        )
        assertEquals(
            AuthenticationState.UnexpectedFailure,
            authStateAfter(AuthenticationResult.Failure),
        )
        assertEquals(
            "auth_invalid_credentials_message",
            messageForAuthState(AuthenticationState.InvalidCredentials),
        )
        assertEquals(
            "auth_service_unavailable_message",
            messageForAuthState(AuthenticationState.Unavailable),
        )
        assertEquals(
            "auth_generic_error_message",
            messageForAuthState(AuthenticationState.UnexpectedFailure),
        )
    }

    @Test
    fun canSubmitRequiresValidFieldsAndReadyPermissionsButAllowsOfflineAttempt() {
        assertTrue(
            canSubmit(
                CredentialsState.Valid,
                ConnectivityState.Offline,
                PermissionState.Ready,
            ),
        )
        assertTrue(
            canSubmit(
                CredentialsState.Valid,
                ConnectivityState.Connected,
                PermissionState.Ready,
            ),
        )
        assertFalse(
            canSubmit(
                CredentialsState.Empty,
                ConnectivityState.Connected,
                PermissionState.Ready,
            ),
        )
        assertFalse(
            canSubmit(
                CredentialsState.Valid,
                ConnectivityState.Connected,
                PermissionState.BlockedRetryable(setOf("permission")),
            ),
        )
    }

    @Test
    fun protectedScreensRequireAuthenticatedStateAndReadyPermissions() {
        assertTrue(
            canEnterProtectedScreens(
                AuthenticationState.Authenticated,
                PermissionState.Ready,
            ),
        )
        listOf(
            AuthenticationState.SignedOut,
            AuthenticationState.InvalidCredentials,
            AuthenticationState.Unavailable,
            AuthenticationState.UnexpectedFailure,
        ).forEach { state ->
            assertFalse(canEnterProtectedScreens(state, PermissionState.Ready))
        }
        assertFalse(
            canEnterProtectedScreens(
                AuthenticationState.Authenticated,
                PermissionState.BlockedIndefinitely(setOf("permission")),
            ),
        )
    }

    @Test
    fun sessionDeadlineIsFiveMinutesAndInteractionBeforeItCancelsExpiry() {
        val startTime = 1_000L
        val schedule = sessionAction(SessionState.Active, SessionEvent.Backgrounded, startTime)
        assertEquals(SessionAction.ScheduleSignOut(startTime + 300_000L), schedule)

        val waiting = SessionState.AwaitingTimeout(startTime + 300_000L)
        assertEquals(
            SessionAction.CancelScheduledSignOut,
            sessionAction(waiting, SessionEvent.UserInteracted, startTime + 299_999L),
        )
        assertEquals(
            SessionAction.SignOut,
            sessionAction(waiting, SessionEvent.TimeoutReached, startTime + 300_000L),
        )
        assertEquals(
            SessionAction.KeepSession,
            sessionAction(waiting, SessionEvent.TimeoutReached, startTime + 299_999L),
        )
    }

    @Test
    fun explicitLogoutSignsOutAndReinteractionAtOrAfterDeadlineCannotRenewSession() {
        val waiting = SessionState.AwaitingTimeout(300_000L)
        assertEquals(
            SessionAction.SignOut,
            sessionAction(SessionState.Active, SessionEvent.Logout, 0L),
        )
        assertEquals(
            SessionAction.SignOut,
            sessionAction(waiting, SessionEvent.UserInteracted, 300_000L),
        )
        assertEquals(
            SessionAction.KeepSession,
            sessionAction(SessionState.SignedOut, SessionEvent.Backgrounded, 400_000L),
        )
    }

    @Test
    fun persistedSessionDeadlineExpiresOnOrAfterDeadlineOnly() {
        val deadline = 1_300_000L
        assertFalse(isSessionExpired(null, deadline))
        assertFalse(isSessionExpired(0L, deadline))
        assertFalse(isSessionExpired(deadline, deadline - 1L))
        assertTrue(isSessionExpired(deadline, deadline))
        assertTrue(isSessionExpired(deadline, deadline + 1L))
    }
}
