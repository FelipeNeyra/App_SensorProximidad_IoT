package com.example.prototipoeva3.auth

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

/** Android adapter for observing network state; it does not probe Firebase availability. */
class ConnectivityMonitor(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var callback: ConnectivityManager.NetworkCallback? = null
    private var listener: ((ConnectivityState) -> Unit)? = null

    fun currentState(): ConnectivityState {
        val network = connectivityManager.activeNetwork ?: return ConnectivityState.Offline
        return stateFor(connectivityManager.getNetworkCapabilities(network))
    }

    fun start(onStateChanged: (ConnectivityState) -> Unit) {
        stop()
        listener = onStateChanged
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                publish(network)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities,
            ) {
                onStateChanged(
                    connectivityState(
                        hasInternetCapability = networkCapabilities.hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_INTERNET,
                        ),
                        isValidated = networkCapabilities.hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_VALIDATED,
                        ),
                    ),
                )
            }

            override fun onLost(network: Network) {
                onStateChanged(currentState())
            }
        }
        callback = networkCallback
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        onStateChanged(currentState())
    }

    fun stop() {
        callback?.let(connectivityManager::unregisterNetworkCallback)
        callback = null
        listener = null
    }

    private fun publish(network: Network) {
        listener?.invoke(stateFor(connectivityManager.getNetworkCapabilities(network)))
    }

    private fun stateFor(capabilities: NetworkCapabilities?): ConnectivityState =
        connectivityState(
            hasInternetCapability = capabilities?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET,
            ) == true,
            isValidated = capabilities?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_VALIDATED,
            ) == true,
        )
}
