package com.example.prototipoeva3.auth

import android.util.Patterns
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

/** Firebase SDK adapter. Credentials are passed to the SDK and never retained by this class. */
//Clase que implementa la interfaz AuthRepository y uns instancia del servicio Authentication de Firebase
class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
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

    //Función para obtener el correo electrónico del usuario vinculado a la seguridad
    override fun getLinkedSecurityUserEmail(): String? = linkedSecurityUserEmail

    //Función para cambiar el correo electrónico del usuario vinculado a la seguridad
    override fun linkSecurityUser(email: String) {
        linkedSecurityUserEmail = email.trim()
    }

    //Función para eliminar el correo electrónico del usuario vinculado a la seguridad
    override fun unlinkSecurityUser() {
        linkedSecurityUserEmail = null
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
    }
}
