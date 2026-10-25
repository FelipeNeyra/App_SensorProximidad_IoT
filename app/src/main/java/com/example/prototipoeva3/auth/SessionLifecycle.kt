package com.example.prototipoeva3.auth

import android.content.Context
import android.os.Handler
import android.os.Looper

/** Persists only the session-expiration deadline; Firebase owns all authentication tokens. */
class SessionLifecycle(
    context: Context,
    private val signOut: () -> Unit,
    private val onExpired: () -> Unit,
    private val nowMillis: () -> Long = System::currentTimeMillis,
) {
    //Variable de almacenamiento del tiempo en milisegundos de la sesión
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    //Variable que permite verificar el tiempo límite en caso de minimizarse la aplicación
    private val handler = Handler(Looper.getMainLooper())
    private var expirationTask: Runnable? = null

    /** Call before restoring a Firebase session, including after process death. */
    //Verificación de estado de sesión cerrada y tiempo de inactividad expirado si la app estaba cerrada
    fun checkExpirationOnLaunch() {
        expireIfDue(preferences.getLong(EXPIRATION_DEADLINE, NO_DEADLINE))
    }

    //Establece el tiempo límite (hora) de cierre de la sesión y la guarda en sharedPreferences
    fun onBackground(isAuthenticated: Boolean) {
        if (!isAuthenticated) {
            clearDeadline()
            return
        }

        val deadline = nowMillis() + SESSION_TIMEOUT_MILLIS
        preferences.edit().putLong(EXPIRATION_DEADLINE, deadline).commit()
        scheduleExpiration(deadline)
    }

    /** Foregrounding counts as renewed interaction if it occurs before the deadline. */
    //Si el usuario vuelve antes del tiempo límite, cancela el temporizador y renueva el tiempo
    fun onForeground() {
        val deadline = preferences.getLong(EXPIRATION_DEADLINE, NO_DEADLINE)
        if (deadline == NO_DEADLINE) return
        if (expireIfDue(deadline)) return
        clearDeadline()
    }

    //Se ejecuta cuando el usuario cierra su sesión, limpiando el temporizador y el tiempo límite
    fun onExplicitSignOut() {
        clearDeadline()
    }

    //Expiración de tiempo límite
    // Ejecución de cierre y almacenamiento de ejecución en handler
    private fun scheduleExpiration(deadline: Long) {
        expirationTask?.let(handler::removeCallbacks)
        val task = Runnable { expireIfDue(deadline) }
        expirationTask = task
        handler.postDelayed(task, (deadline - nowMillis()).coerceAtLeast(0L))
    }

    //Verificación de expiración de tiempo desde AuthFlow
    private fun expireIfDue(deadline: Long): Boolean {
        if (!isSessionExpired(deadline, nowMillis())) return false
        clearDeadline()
        signOut() // Ejecuta authRepository.signOut() (Revoca token en Firebase)
        onExpired() // Dispara callback a MainActivity para actualizar la UI
        return true
    }

    //Elimiación de estado de tiempo en handler y sharedPreferences
    private fun clearDeadline() {
        expirationTask?.let(handler::removeCallbacks)
        expirationTask = null
        preferences.edit().remove(EXPIRATION_DEADLINE).apply()
    }

    //Etiquetas y variables
    private companion object {
        const val PREFERENCES_NAME = "session_lifecycle"
        const val EXPIRATION_DEADLINE = "expiration_deadline_millis"
        const val NO_DEADLINE = 0L
        const val SESSION_TIMEOUT_MILLIS = 5 * 60 * 1000L
    }
}
