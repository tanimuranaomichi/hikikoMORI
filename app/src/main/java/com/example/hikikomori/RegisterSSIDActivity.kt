package com.example.hikikomori

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class RegisterSSIDActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_ssid)

        val ssidChecker = SSIDChecker(this)

        val connectedSSIDText = findViewById<TextView>(R.id.text_connectedSSID)
        val SSIDText = findViewById<EditText>(R.id.text_SSID)
        val registerButton = findViewById<Button>(R.id.button_register)

        connectedSSIDText.text = ssidChecker.getConnectedSSID()

        registerButton.setOnClickListener {

            getSharedPreferences("my_settings", Context.MODE_PRIVATE).edit().apply {
                putString("registeredSSID",SSIDText.text.toString())
                commit()
            }
            val intent = Intent(application, MainActivity::class.java)
            startActivity(intent)
        }
    }
}