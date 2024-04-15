package com.example.shreebhagavadgita

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

//what is context here?
//Context is the base class for Activity, Service, Application, etc.
// Context provides information about the environment in which the application is running.
// It also provides the ability to get the resources, databases, shared preferences, etc.
class NetworkManager(context: Context): LiveData<Boolean>() {

    override fun onActive() {
        super.onActive()
        checkNetworkConnectivity()
    }


    override fun onInactive() {
        super.onInactive()
        releaseCheckingNetworkConnectivity()
    }

//context is used to get the system service of connectivity manager to check the network connectivity of the device
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkRequest = NetworkRequest.Builder().apply{
        addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
        addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
    }.build()

    private val networkCallback= object:ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            postValue(false)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }


//___________________________________________________________________________________________________
    private fun checkNetworkConnectivity() {
       if(connectivityManager.activeNetwork==null) {
           postValue(false)
       }

//    registerNetworkCallback() is used to register a callback to be invoked when network is available or unavailable
//    networkRequest is the network request object that is used to check the network capabilities of the device
//    networkCallback is the callback object that is used to check the network status of the device
        connectivityManager.registerNetworkCallback(networkRequest,networkCallback)

    }

//___________________________________________________________________________________________________
    private fun releaseCheckingNetworkConnectivity() {

        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}