package com.example.hikikomori

import android.content.Context
import android.net.wifi.WifiManager


class SSIDChecker(private val context: Context) {
    val wifiManager: WifiManager get() = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    fun checkSSID(registeredSSID: String): Boolean {

        if (wifiManager.wifiState == WifiManager.WIFI_STATE_ENABLED) {
            var results = wifiManager.scanResults
            if (!results.isEmpty()) {
                var connectedSSID = results[1].SSID
                println("connectedSSID: $connectedSSID")
                return Regex(registeredSSID).containsMatchIn(connectedSSID)
            }
            return false
        } else {
            println("connectedSSID: none")
            return false
        }

    }
}