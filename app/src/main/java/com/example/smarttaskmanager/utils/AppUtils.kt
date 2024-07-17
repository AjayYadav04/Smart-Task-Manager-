package com.example.smarttaskmanager.utils

import android.content.Context
import android.net.ConnectivityManager

object  AppUtils {
    var appContext: Context? = null
    fun setContext(context: Context) {
        appContext = context
    }

    fun isOnline(): Boolean {
        val connectivityManager =
            appContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}