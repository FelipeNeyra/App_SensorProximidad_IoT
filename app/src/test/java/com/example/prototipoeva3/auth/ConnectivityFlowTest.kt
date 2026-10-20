package com.example.prototipoeva3.auth

import com.example.prototipoeva3.permissions.PermissionState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ConnectivityFlowTest {
    @Test
    fun networkIsConnectedOnlyWhenInternetCapabilityIsValidated() {
        assertEquals(ConnectivityState.Connected, connectivityState(true, true))
        assertEquals(ConnectivityState.Offline, connectivityState(false, true))
        assertEquals(ConnectivityState.Offline, connectivityState(true, false))
        assertEquals(ConnectivityState.Offline, connectivityState(false, false))
    }

    @Test
    fun offlineStateDoesNotDisableExplicitLoginAttemptOrCloseApplication() {
        assertTrue(
            canSubmit(
                CredentialsState.Valid,
                ConnectivityState.Offline,
                PermissionState.Ready,
            ),
        )
        // Connectivity is not part of the protected-route decision: missing identity is.
        assertFalse(
            canEnterProtectedScreens(
                AuthenticationState.SignedOut,
                PermissionState.Ready,
            ),
        )
    }

    @Test
    fun authenticationFailuresAreMappedToGenericSafeResults() {
        assertEquals(
            AuthenticationResult.InvalidCredentials,
            authResultAfterFailure(AuthFailureKind.InvalidCredentials),
        )
        assertEquals(
            AuthenticationResult.Unavailable,
            authResultAfterFailure(AuthFailureKind.NetworkUnavailable),
        )
        assertEquals(
            AuthenticationResult.Unavailable,
            authResultAfterFailure(AuthFailureKind.ServiceUnavailable),
        )
        assertEquals(
            AuthenticationResult.Failure,
            authResultAfterFailure(AuthFailureKind.Unexpected),
        )
    }
}
