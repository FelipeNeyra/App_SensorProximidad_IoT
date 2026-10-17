package com.example.prototipoeva3.permissions

/** Estados independientes de Android para el flujo de permisos. */
sealed interface PermissionState {
    data object Ready : PermissionState

    data class Requesting(val isRetry: Boolean) : PermissionState

    data class BlockedRetryable(val deniedPermissions: Set<String>) : PermissionState

    data class BlockedIndefinitely(val deniedPermissions: Set<String>) : PermissionState
}

/** Devuelve los permisos requeridos que aún no han sido concedidos. */
fun permissionsToRequest(required: Set<String>, granted: Set<String>): Set<String> =
    required - granted

/**
 * Calcula el siguiente estado a partir de los resultados del último diálogo.
 * El mapa contiene el nombre de cada permiso solicitado y si fue concedido.
 */
fun nextPermissionState(
    current: PermissionState,
    result: Map<String, Boolean>
): PermissionState {
    if (current !is PermissionState.Requesting) return current

    val denied = result.filterValues { granted -> !granted }.keys
    if (denied.isEmpty()) return PermissionState.Ready

    return if (current.isRetry) {
        PermissionState.BlockedIndefinitely(denied)
    } else {
        PermissionState.BlockedRetryable(denied)
    }
}

/** Solo se permite navegar cuando todos los permisos aplicables están concedidos. */
fun canNavigate(state: PermissionState): Boolean = state == PermissionState.Ready

/** Devuelve el nombre de recurso Android que corresponde al estado. */
fun messageFor(state: PermissionState): String = when (state) {
    PermissionState.Ready -> ""
    is PermissionState.Requesting -> "permission_request_message"
    is PermissionState.BlockedRetryable -> "permission_denied_message"
    is PermissionState.BlockedIndefinitely -> "permission_blocked_indefinitely_message"
}
