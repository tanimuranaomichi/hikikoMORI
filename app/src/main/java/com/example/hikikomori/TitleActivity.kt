package com.example.hikikomori

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class TitleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)

        val startButton = findViewById<ImageButton>(R.id.button_start)
        startButton.setOnClickListener {
            val intent = Intent(application, MainActivity::class.java)
            startActivity(intent)
        }
    }
}