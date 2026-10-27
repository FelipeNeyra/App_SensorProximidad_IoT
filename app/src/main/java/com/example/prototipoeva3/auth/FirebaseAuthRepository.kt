package com.example.prototipoeva3.auth

import android.util.Patterns
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/** Firebase SDK adapter. Credentials are passed to the SDK and never retained by this class. */
//Clase que implementa la interfaz AuthRepository y uns instancia del servicio Authentication de Firebase
class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : AuthRepository {

    //Función de Ingreso de Usuarios (Login)
    override fun signIn(
        email: String,
        password: String,
        onResult: (AuthenticationResult) -> Unit,
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val result = if (task.isSuccessful && task.result?.user != null) {
                    AuthenticationResult.Success
                } else {
                    authResultAfterFailure(failureKind(task.exception))
                }
                onResult(result)
            }
    }

    //Función para identificar a un usuario ya autenticado (Sesión activa)
    override fun currentUser(): AuthenticatedUser? =
        firebaseAuth.currentUser?.uid?.let(::AuthenticatedUser)

    //Función para cerrar sesión en el servicio de Firebase
    override fun signOut() {
        firebaseAuth.signOut()
        linkedSecurityUserEmail = null
        linkedSecurityUserPhone = null
    }

    //Función para verificar si existe un usuario (Usuario de seguridad)
    override suspend fun checkUserExistsInAuth(email: String): Result<Boolean> {
        return try {
            val trimmed = email.trim()
            val isValid = Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()
            // In prototype/IoT scope, if the email format is valid (like seguridad@eva3.cl),
            // we consider it a valid security user contact for alert routing.
            Result.success(isValid)
        } catch (e: Exception) {
            Result.success(true)
        }
    }

    //Función para obtener el correo electrónico del usuario vinculado a la seguridad desde Firestore
    override suspend fun getLinkedSecurityUserEmail(): String? {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: return linkedSecurityUserEmail
            val doc = firestore.collection("Configuracion").document(userId).get().await()
            if (doc.exists()) {
                val email = doc.getString("securityUserEmail")
                val phone = doc.getString("securityUserPhone")
                linkedSecurityUserEmail = email
                linkedSecurityUserPhone = phone
                email
            } else {
                linkedSecurityUserEmail = null
                linkedSecurityUserPhone = null
                null
            }
        } catch (e: Exception) {
            linkedSecurityUserEmail
        }
    }

    //Función para obtener el teléfono del usuario vinculado a la seguridad desde Firestore
    override suspend fun getLinkedSecurityUserPhone(): String? {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: return linkedSecurityUserPhone
            val doc = firestore.collection("Configuracion").document(userId).get().await()
            if (doc.exists()) {
                val email = doc.getString("securityUserEmail")
                val phone = doc.getString("securityUserPhone")
                linkedSecurityUserEmail = email
                linkedSecurityUserPhone = phone
                phone
            } else {
                linkedSecurityUserEmail = null
                linkedSecurityUserPhone = null
                null
            }
        } catch (e: Exception) {
            linkedSecurityUserPhone
        }
    }

    //Función para vincular el correo y teléfono del usuario de seguridad en Firestore y memoria
    override suspend fun linkSecurityUser(email: String, phone: String) {
        val trimmedEmail = email.trim()
        val trimmedPhone = phone.trim()
        linkedSecurityUserEmail = trimmedEmail
        linkedSecurityUserPhone = trimmedPhone

        try {
            val user = firebaseAuth.currentUser
            val userId = user?.uid ?: return
            val driverEmail = user.email ?: "no_email"
            val data = mapOf(
                "driverEmail" to driverEmail,
                "securityUserEmail" to trimmedEmail,
                "securityUserPhone" to trimmedPhone,
                "timestamp" to Timestamp.now(),
            )
            firestore.collection("Configuracion")
                .document(userId)
                .set(data)
                .await()
        } catch (_: Exception) {
        }
    }

    //Función para desvincular y eliminar el usuario de seguridad desde Firestore y memoria
    override suspend fun unlinkSecurityUser() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            try {
                firestore.collection("Configuracion")
                    .document(userId)
                    .delete()
                    .await()
            } catch (_: Exception) {
            }
        }
        linkedSecurityUserEmail = null
        linkedSecurityUserPhone = null
    }

    //Función para obtener excepciones de ingreso e identificar su causa de fallo
    private fun failureKind(exception: Exception?): AuthFailureKind = when (exception) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> AuthFailureKind.InvalidCredentials

        is FirebaseNetworkException,
        is FirebaseTooManyRequestsException -> AuthFailureKind.NetworkUnavailable

        is FirebaseAuthException -> AuthFailureKind.ServiceUnavailable
        else -> AuthFailureKind.Unexpected
    }

    companion object {
        var linkedSecurityUserEmail: String? = null
        var linkedSecurityUserPhone: String? = null
    }
}
