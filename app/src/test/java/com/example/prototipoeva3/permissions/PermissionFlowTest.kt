package com.example.prototipoeva3.permissions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PermissionFlowTest {
    @Test
    fun permissionsToRequestReturnsOnlyMissingPermissions() {
        assertEquals(
            setOf("bluetooth", "location"),
            permissionsToRequest(
                required = setOf("bluetooth", "wifi", "location"),
                granted = setOf("wifi")
            )
        )
    }

    @Test
    fun noMissingPermissionsProducesEmptyRequestSet() {
        assertTrue(permissionsToRequest(emptySet(), emptySet()).isEmpty())
    }

    @Test
    fun grantedRequestTransitionsToReady() {
        assertEquals(
            PermissionState.Ready,
            nextPermissionState(
                PermissionState.Requesting(isRetry = false),
                mapOf("permission" to true)
            )
        )
    }

    @Test
    fun firstDenialTransitionsToRetryableBlock() {
        assertEquals(
            PermissionState.BlockedRetryable(setOf("permission")),
            nextPermissionState(
                PermissionState.Requesting(isRetry = false),
                mapOf("permission" to false)
            )
        )
    }

    @Test
    fun denialAfterRetryTransitionsToIndefiniteBlock() {
        assertEquals(
            PermissionState.BlockedIndefinitely(setOf("permission")),
            nextPermissionState(
                PermissionState.Requesting(isRetry = true),
                mapOf("permission" to false)
            )
        )
    }

    @Test
    fun nonRequestingStateDoesNotChangeFromPermissionResult() {
        val state = PermissionState.BlockedRetryable(setOf("permission"))
        assertEquals(state, nextPermissionState(state, mapOf("permission" to true)))
    }

    @Test
    fun onlyReadyAllowsNavigation() {
        assertTrue(canNavigate(PermissionState.Ready))
        assertFalse(canNavigate(PermissionState.Requesting(isRetry = false)))
        assertFalse(canNavigate(PermissionState.BlockedRetryable(setOf("permission"))))
        assertFalse(canNavigate(PermissionState.BlockedIndefinitely(setOf("permission"))))
    }

    @Test
    fun eachPermissionStateUsesExpectedSpanishMessageResource() {
        assertEquals("", messageFor(PermissionState.Ready))
        assertEquals(
            "permission_request_message",
            messageFor(PermissionState.Requesting(isRetry = false))
        )
        assertEquals(
            "permission_denied_message",
            messageFor(PermissionState.BlockedRetryable(setOf("permission")))
        )
        assertEquals(
            "permission_blocked_indefinitely_message",
            messageFor(PermissionState.BlockedIndefinitely(setOf("permission")))
        )
    }
}
