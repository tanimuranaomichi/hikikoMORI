package com.example.hikikomori

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import twitter4j.TwitterFactory
import twitter4j.auth.OAuthAuthorization
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationBuilder
import twitter4j.conf.ConfigurationContext
import java.lang.Runnable
import kotlin.coroutines.CoroutineContext

val handler = Handler()
var timeValue = 0

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val ssidChecker = SSIDChecker(this)
    private val levelManager = LevelManager()
    private var accessToken = ""
    private var accessTokenSecret = ""


    //コルーチンを使うための準備
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    //認証オブジェクトの作成
    companion object {
        val mOauth = OAuthAuthorization(ConfigurationContext.getInstance())
        lateinit var mRequest: RequestToken
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val totalTimeText = findViewById<TextView>(R.id.totalTime)
        val levelText = findViewById<TextView>(R.id.level)
        val treeImage = findViewById<ImageView>(R.id.treeLayer)
        val tweetButton = findViewById<ImageButton>(R.id.tweetButton)

        loadData()

        timeToText(timeValue)?.let {
            totalTimeText.text = "ひきこ森タイム" + it
        }
        levelManager.timeToLevel(timeValue)?.let {
            levelText.text = "ひきこ森レベル" + it.toString() + "ha"
        }

        val runnable = object : Runnable {
            override fun run() {
                if (ssidChecker.checkSSID()) {
                    timeValue++
                }
                timeToText(timeValue)?.let {
                    totalTimeText.text = "ひきこ森タイム" + it
                }
                levelManager.timeToLevel(timeValue)?.let {
                    levelText.text = "ひきこ森レベル" + it.toString() + "ha"
                }
                treeImage.setImageResource(levelManager.getTreeImage())

                handler.postDelayed(this, 1000)
            }
        }

        getPermission()
        handler.post(runnable)

        tweetButton.setOnClickListener { tweet() }
    }

    override fun onPause() {
        super.onPause()

        saveTimeValue()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting_SSID -> {
                val intent = Intent(application, RegisterSSIDActivity::class.java)
                startActivity(intent)
            }
            R.id.login_twitter -> {
                loginTwitterAccount()
            }
        }
        return true
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

    private fun getPermission() {
        val PERMISSIONS_REQUEST_CODE = 1

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
    }

    private fun tweet() {
        launch {

            async(context = Dispatchers.IO) {
                val cb = ConfigurationBuilder()
                cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("5URc3izBLHpEPzZMr8z9AqSby")
                    .setOAuthConsumerSecret("psCEExAKIcfViAdF6ougrI13DCJig8zYLCoyYtMQDf2LSW4gfd")
                    .setOAuthAccessToken(accessToken)
                    .setOAuthAccessTokenSecret(accessTokenSecret)                //各種キーの設定

                val tf = TwitterFactory(cb.build())
                val twitter = tf.getInstance()
                twitter.updateStatus("私のひきこ森レベルは" + levelManager.level + " haです！ #hikikoMORI")    //ツイートの投稿
            }.await()
        }
    }

    private fun loginTwitterAccount() {
        launch {
            mOauth.setOAuthConsumer(
                "5URc3izBLHpEPzZMr8z9AqSby",
                "psCEExAKIcfViAdF6ougrI13DCJig8zYLCoyYtMQDf2LSW4gfd"
            )
            mOauth.oAuthAccessToken = null
            var uri: Uri?

            async(context = Dispatchers.IO) {
                mRequest = mOauth.getOAuthRequestToken("callback://CallBackActivity")    //コールバックの設定

                var intent = Intent(Intent.ACTION_VIEW)
                uri = Uri.parse(mRequest.authenticationURL)
                intent.data = uri
                startActivityForResult(intent, 0)            //暗黙インテントでWebブラウザを呼び出して認証
            }                                                //認証情報を受け取るためにstartActivityForResult()
        }
    }

    private fun loadData() {
        getSharedPreferences("my_settings", Context.MODE_PRIVATE).apply {
            timeValue = getInt("timeValue", 0)
            ssidChecker.registeredSSID = getString("registeredSSID", "").toString()
            accessToken = getString("token", "").toString()
            accessTokenSecret = getString("tokenSecret", "").toString()
        }
    }

    private fun saveTimeValue() {
        println("SAVE")

        getSharedPreferences("my_settings", Context.MODE_PRIVATE).edit().apply {
            putInt("timeValue", timeValue)
            commit()
        }
    }

}