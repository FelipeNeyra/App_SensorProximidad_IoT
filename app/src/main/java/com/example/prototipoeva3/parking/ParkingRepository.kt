package com.example.prototipoeva3.parking

import com.example.prototipoeva3.auth.FirebaseAuthRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface ParkingRepository {
    suspend fun saveParkingRecord(
        distanceCm: Float,
        ubicacionModo: String,
        nota: String,
    ): Result<ParkingRecord>

    suspend fun saveSuddenChangeRecord(
        initialDistanceCm: Float,
        finalDistanceCm: Float,
        timeDeltaMs: Long,
        ubicacionModo: String,
        nota: String,
    ): Result<SuddenChangeRecord>

    suspend fun saveAlertRecord(
        referenceDocumentId: String,
        referenceCollection: String,
        securityUserEmail: String,
        customMessage: String = DEFAULT_ALERT_MESSAGE,
    ): Result<AlertRecord>
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
            val currentTimestamp = Timestamp.now()
            val timestampMs = currentTimestamp.toDate().time
            val documentId = generateParkingDocumentId(userId, timestampMs)
            val estado = categorizeDistanceState(distanceCm)

            val record = ParkingRecord(
                id = documentId,
                userId = userId,
                userEmail = userEmail,
                distancia = distanceCm.toInt(),
                estado = estado,
                ubicacionModo = ubicacionModo,
                nota = nota.trim(),
                timestamp = currentTimestamp,
            )

            firestore.collection("Aparcamiento")
                .document(documentId)
                .set(record.toMap())
                .await()

            if (estado == "ADVERTENCIA") {
                val securityEmail = FirebaseAuthRepository.linkedSecurityUserEmail ?: "no_vinculado"
                saveAlertRecord(
                    referenceDocumentId = documentId,
                    referenceCollection = "Aparcamiento",
                    securityUserEmail = securityEmail,
                )
            }

            Result.success(record)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveSuddenChangeRecord(
        initialDistanceCm: Float,
        finalDistanceCm: Float,
        timeDeltaMs: Long,
        ubicacionModo: String,
        nota: String,
    ): Result<SuddenChangeRecord> {
        return try {
            val user = auth.currentUser
            val userId = user?.uid ?: "anonimo"
            val userEmail = user?.email ?: "no_email"
            val currentTimestamp = Timestamp.now()
            val timestampMs = currentTimestamp.toDate().time
            val documentId = generateSuddenChangeDocumentId(userId, timestampMs)
            val estado = categorizeDistanceState(finalDistanceCm)
            val variationCm = (initialDistanceCm - finalDistanceCm).coerceAtLeast(0f).toInt()

            val record = SuddenChangeRecord(
                id = documentId,
                userId = userId,
                userEmail = userEmail,
                distanciaInicial = initialDistanceCm.toInt(),
                distanciaFinal = finalDistanceCm.toInt(),
                variacionDistancia = variationCm,
                tiempoMs = timeDeltaMs,
                estado = estado,
                ubicacionModo = ubicacionModo,
                nota = nota.trim(),
                timestamp = currentTimestamp,
            )

            firestore.collection("Cambio_Brusco")
                .document(documentId)
                .set(record.toMap())
                .await()

            if (estado == "ADVERTENCIA") {
                val securityEmail = FirebaseAuthRepository.linkedSecurityUserEmail ?: "no_vinculado"
                saveAlertRecord(
                    referenceDocumentId = documentId,
                    referenceCollection = "Cambio_Brusco",
                    securityUserEmail = securityEmail,
                )
            }

            Result.success(record)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveAlertRecord(
        referenceDocumentId: String,
        referenceCollection: String,
        securityUserEmail: String,
        customMessage: String,
    ): Result<AlertRecord> {
        return try {
            val user = auth.currentUser
            val userId = user?.uid ?: "anonimo"
            val currentTimestamp = Timestamp.now()
            val timestampMs = currentTimestamp.toDate().time
            val documentId = generateAlertDocumentId(userId, timestampMs)

            val alert = AlertRecord(
                id = documentId,
                userId = userId,
                securityUserEmail = securityUserEmail,
                referenceDocumentId = referenceDocumentId,
                referenceCollection = referenceCollection,
                estado = "ADVERTENCIA",
                mensaje = customMessage,
                timestamp = currentTimestamp,
            )

            firestore.collection("Alerta")
                .document(documentId)
                .set(alert.toMap())
                .await()

            Result.success(alert)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
