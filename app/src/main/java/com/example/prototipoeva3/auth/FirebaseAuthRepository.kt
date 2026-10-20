package com.example.prototipoeva3.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

/** Firebase SDK adapter. Credentials are passed to the SDK and never retained by this class. */
class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
) : AuthRepository {

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

    override fun currentUser(): AuthenticatedUser? =
        firebaseAuth.currentUser?.uid?.let(::AuthenticatedUser)

    override fun signOut() {
        firebaseAuth.signOut()
    }

    private fun failureKind(exception: Exception?): AuthFailureKind = when (exception) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> AuthFailureKind.InvalidCredentials

        is FirebaseNetworkException,
        is FirebaseTooManyRequestsException -> AuthFailureKind.NetworkUnavailable

        is FirebaseAuthException -> AuthFailureKind.ServiceUnavailable
        else -> AuthFailureKind.Unexpected
    }
}
