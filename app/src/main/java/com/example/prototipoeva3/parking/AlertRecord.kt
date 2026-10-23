package com.example.prototipoeva3.parking

import com.google.firebase.Timestamp

const val DEFAULT_ALERT_MESSAGE = "Prueba de mensaje de alerta - App Arduino."

data class AlertRecord(
    val id: String,
    val userId: String,
    val securityUserEmail: String,
    val referenceDocumentId: String,
    val referenceCollection: String,
    val estado: String,
    val mensaje: String,
    val timestamp: Timestamp,
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "securityUserEmail" to securityUserEmail,
            "referenceDocumentId" to referenceDocumentId,
            "referenceCollection" to referenceCollection,
            "estado" to estado,
            "mensaje" to mensaje,
            "timestamp" to timestamp,
        )
    }
}

fun generateAlertDocumentId(userId: String, timestampMs: Long): String {
    val cleanUserId = userId.ifBlank { "anonimo" }
        .replace(Regex("[^a-zA-Z0-9_]"), "_")
    return "alerta_${cleanUserId}_$timestampMs"
}
