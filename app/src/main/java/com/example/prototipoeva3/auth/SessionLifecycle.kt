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
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val handler = Handler(Looper.getMainLooper())
    private var expirationTask: Runnable? = null

    /** Call before restoring a Firebase session, including after process death. */
    fun checkExpirationOnLaunch() {
        expireIfDue(preferences.getLong(EXPIRATION_DEADLINE, NO_DEADLINE))
    }

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
    fun onForeground() {
        val deadline = preferences.getLong(EXPIRATION_DEADLINE, NO_DEADLINE)
        if (deadline == NO_DEADLINE) return
        if (expireIfDue(deadline)) return
        clearDeadline()
    }

    fun onExplicitSignOut() {
        clearDeadline()
    }

    private fun scheduleExpiration(deadline: Long) {
        expirationTask?.let(handler::removeCallbacks)
        val task = Runnable { expireIfDue(deadline) }
        expirationTask = task
        handler.postDelayed(task, (deadline - nowMillis()).coerceAtLeast(0L))
    }

    private fun expireIfDue(deadline: Long): Boolean {
        if (!isSessionExpired(deadline, nowMillis())) return false
        clearDeadline()
        signOut()
        onExpired()
        return true
    }

    private fun clearDeadline() {
        expirationTask?.let(handler::removeCallbacks)
        expirationTask = null
        preferences.edit().remove(EXPIRATION_DEADLINE).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "session_lifecycle"
        const val EXPIRATION_DEADLINE = "expiration_deadline_millis"
        const val NO_DEADLINE = 0L
        const val SESSION_TIMEOUT_MILLIS = 5 * 60 * 1000L
    }
}
