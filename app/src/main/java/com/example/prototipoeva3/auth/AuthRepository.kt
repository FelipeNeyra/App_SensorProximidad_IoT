package com.example.prototipoeva3.auth

/** Minimal identity information needed to restore a previously authenticated session. */
data class AuthenticatedUser(val uid: String)

/** Provider-independent boundary between the application flow and Firebase Authentication. */
interface AuthRepository {
    fun signIn(email: String, password: String, onResult: (AuthenticationResult) -> Unit)

    fun currentUser(): AuthenticatedUser?

    fun signOut()

    suspend fun checkUserExistsInAuth(email: String): Result<Boolean>

    suspend fun getLinkedSecurityUserEmail(): String?
    suspend fun getLinkedSecurityUserPhone(): String?

    suspend fun linkSecurityUser(email: String, phone: String)

    suspend fun unlinkSecurityUser()
}
