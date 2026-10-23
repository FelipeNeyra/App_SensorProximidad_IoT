package com.example.prototipoeva3.parking

import com.google.firebase.Timestamp

data class SuddenChangeRecord(
    val id: String,
    val userId: String,
    val userEmail: String,
    val distanciaInicial: Int,
    val distanciaFinal: Int,
    val variacionDistancia: Int,
    val tiempoMs: Long,
    val estado: String,
    val ubicacionModo: String,
    val nota: String,
    val timestamp: Timestamp,
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "userEmail" to userEmail,
            "distanciaInicial" to distanciaInicial,
            "distanciaFinal" to distanciaFinal,
            "variacionDistancia" to variacionDistancia,
            "tiempoMs" to tiempoMs,
            "estado" to estado,
            "ubicacionModo" to ubicacionModo,
            "nota" to nota,
            "timestamp" to timestamp,
        )
    }
}

fun generateSuddenChangeDocumentId(userId: String, timestampMs: Long): String {
    val cleanUserId = userId.ifBlank { "anonimo" }
        .replace(Regex("[^a-zA-Z0-9_]"), "_")
    return "cambio_brusco_${cleanUserId}_$timestampMs"
}
