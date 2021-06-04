package com.example.hikikomori

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class RegisterSSIDActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_ssid)

        val SSIDText = findViewById<EditText>(R.id.text_SSID)
        val registerButton = findViewById<Button>(R.id.button_register)

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