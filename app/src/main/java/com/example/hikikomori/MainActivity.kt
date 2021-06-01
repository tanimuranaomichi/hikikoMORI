package com.example.hikikomori

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

val handler = Handler()
var timeValue = 0

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 1
    private val ssidChecker = SSIDChecker(this)
    private val levelManager = LevelManager()
    private var stateOfConnection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val totalTime = findViewById<TextView>(R.id.totalTime)
        val level = findViewById<TextView>(R.id.level)
        val imageOfTree = findViewById<ImageView>(R.id.treeLayer)

        loadData()

        val runnable = object : Runnable {
            override fun run() {
                if (!stateOfConnection && ssidChecker.checkSSID("ogatalab")) {
                    stateOfConnection = true
                }
                if (stateOfConnection && !ssidChecker.checkSSID("ogatalab")) {
                    stateOfConnection = false
                }
                if (stateOfConnection == true) {
                    timeValue++

                    timeToText(timeValue)?.let {
                        totalTime.text = "ひきこ森タイム" + it
                    }

                    levelManager.timeToLevel(timeValue)?.let {
                        level.text = "ひきこ森レベル" + it.toString() + "ha"
                    }
                }

                imageOfTree.setImageResource(levelManager.getImageOfTree())
                handler.postDelayed(this, 1000)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE
                    ),
                    PERMISSIONS_REQUEST_CODE
                );
            }
        }
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()

        saveData()
    }

    private fun timeToText(time: Int = 0): String? {
        return if (time < 0) {
            null
        } else if (time == 0) {
            "00:00:00"
        } else {
            val h = time / 3600
            val m = time % 3600 / 60
            val s = time % 60
            "%1$02d:%2$02d:%3$02d".format(h, m, s)
        }
    }

    private fun saveData() {
        println("SAVE")

        getSharedPreferences("my_settings", Context.MODE_PRIVATE).edit().apply {
            putInt("timeValue", timeValue)
            commit()
        }
    }

    private fun loadData() {
        println("LOAD")

        getSharedPreferences("my_settings", Context.MODE_PRIVATE).apply {
            timeValue = getInt("timeValue",0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
            }
        }
        return true
    }

}