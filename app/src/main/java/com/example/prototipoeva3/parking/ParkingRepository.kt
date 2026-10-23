package com.example.prototipoeva3.parking

import com.example.prototipoeva3.auth.FirebaseAuthRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    suspend fun getRecentAlerts(): Result<List<AlertRecord>>

    suspend fun getRecentParkingRecords(): Result<List<ParkingRecord>>
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

            if (estado == "ADVERTENCIA" || estado == "PELIGRO") {
                val securityEmail = FirebaseAuthRepository.linkedSecurityUserEmail ?: "no_vinculado"
                saveAlertRecord(
                    referenceDocumentId = documentId,
                    referenceCollection = "Aparcamiento",
                    securityUserEmail = securityEmail,
                    customMessage = "Alerta de proximidad [$estado]: Distancia ${distanceCm.toInt()} cm en modo $ubicacionModo",
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

            val securityEmail = FirebaseAuthRepository.linkedSecurityUserEmail ?: "no_vinculado"
            saveAlertRecord(
                referenceDocumentId = documentId,
                referenceCollection = "Cambio_Brusco",
                securityUserEmail = securityEmail,
                customMessage = "¡CAMBIO BRUSCO DETECTADO! Variación de $variationCm cm en $timeDeltaMs ms.",
            )

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

    override suspend fun getRecentAlerts(): Result<List<AlertRecord>> {
        return try {
            val snapshot = firestore.collection("Alerta")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(15)
                .get()
                .await()

            val alerts = snapshot.documents.mapNotNull { doc ->
                val id = doc.getString("id") ?: doc.id
                val userId = doc.getString("userId") ?: ""
                val securityUserEmail = doc.getString("securityUserEmail") ?: ""
                val referenceDocumentId = doc.getString("referenceDocumentId") ?: ""
                val referenceCollection = doc.getString("referenceCollection") ?: ""
                val estado = doc.getString("estado") ?: ""
                val mensaje = doc.getString("mensaje") ?: ""
                val timestamp = doc.getTimestamp("timestamp") ?: Timestamp.now()
                AlertRecord(id, userId, securityUserEmail, referenceDocumentId, referenceCollection, estado, mensaje, timestamp)
            }
            Result.success(alerts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecentParkingRecords(): Result<List<ParkingRecord>> {
        return try {
            val snapshot = firestore.collection("Aparcamiento")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(15)
                .get()
                .await()

            val records = snapshot.documents.mapNotNull { doc ->
                val id = doc.getString("id") ?: doc.id
                val userId = doc.getString("userId") ?: ""
                val userEmail = doc.getString("userEmail") ?: ""
                val distancia = doc.getLong("distancia")?.toInt() ?: 0
                val estado = doc.getString("estado") ?: ""
                val ubicacionModo = doc.getString("ubicacionModo") ?: ""
                val nota = doc.getString("nota") ?: ""
                val timestamp = doc.getTimestamp("timestamp") ?: Timestamp.now()
                ParkingRecord(id, userId, userEmail, distancia, estado, ubicacionModo, nota, timestamp)
            }
            Result.success(records)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
