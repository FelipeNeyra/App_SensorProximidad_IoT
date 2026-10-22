package com.example.prototipoeva3.parking

data class ParkingRecord(
    val id: String,
    val userId: String,
    val userEmail: String,
    val distancia: Int,
    val estado: String,
    val ubicacionModo: String,
    val nota: String,
    val timestamp: Long,
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "userEmail" to userEmail,
            "distancia" to distancia,
            "estado" to estado,
            "ubicacionModo" to ubicacionModo,
            "nota" to nota,
            "timestamp" to timestamp,
        )
    }
}

fun generateParkingDocumentId(userId: String, timestampMs: Long): String {
    val cleanUserId = userId.ifBlank { "anonimo" }
        .replace(Regex("[^a-zA-Z0-9_]"), "_")
    return "aparcamiento_${cleanUserId}_$timestampMs"
}

fun categorizeDistanceState(distanceCm: Float): String {
    return when {
        distanceCm <= 33f -> "PELIGRO"
        distanceCm <= 66f -> "ADVERTENCIA"
        else -> "SEGURO"
    }
}
