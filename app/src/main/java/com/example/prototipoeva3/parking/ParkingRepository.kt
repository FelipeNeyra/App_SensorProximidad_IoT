package com.example.prototipoeva3.parking

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface ParkingRepository {
    suspend fun saveParkingRecord(
        distanceCm: Float,
        ubicacionModo: String,
        nota: String,
    ): Result<ParkingRecord>
}

class FirebaseParkingRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : ParkingRepository {

    override suspend fun saveParkingRecord(
        distanceCm: Float,
        ubicacionModo: String,
        nota: String,
    ): Result<ParkingRecord> {
        return try {
            val user = auth.currentUser
            val userId = user?.uid ?: "anonimo"
            val userEmail = user?.email ?: "no_email"
            val timestamp = System.currentTimeMillis()
            val documentId = generateParkingDocumentId(userId, timestamp)
            val estado = categorizeDistanceState(distanceCm)

            val record = ParkingRecord(
                id = documentId,
                userId = userId,
                userEmail = userEmail,
                distancia = distanceCm.toInt(),
                estado = estado,
                ubicacionModo = ubicacionModo,
                nota = nota.trim(),
                timestamp = timestamp,
            )

            firestore.collection("Aparcamiento")
                .document(documentId)
                .set(record.toMap())
                .await()

            Result.success(record)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
