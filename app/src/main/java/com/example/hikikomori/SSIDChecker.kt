package com.example.hikikomori

class SSIDChecker {
    private val connectedSSID = "ogatalab"

    fun checkSSID(registedSSID: String): Boolean {
//        あとで実装する
        return registedSSID == this.connectedSSID
    }
}