package com.example.hikikomori

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CallBackActivity : AppCompatActivity() , CoroutineScope{

    //コルーチンを使うための準備
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_back)

        val backButton = findViewById<Button>(R.id.button_back)

        val uri = intent.data                //認証情報の受け取り
        if (uri != null && uri.toString().startsWith("callback://CallBackActivity")){
            launch {
                val mtoken = async(context = Dispatchers.IO){           //asyncは値を返せる
                    val varif = uri.getQueryParameter("oauth_verifier")
                    val token = MainActivity.mOauth.getOAuthAccessToken(MainActivity.mRequest, varif)
                    return@async token
                }.await()

                saveData(mtoken.token, mtoken.tokenSecret)


            }
        }

        backButton.setOnClickListener {
            val intent = Intent(application, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun saveData(token: String, tokenSecret: String) {
        println("SAVE")

        getSharedPreferences("my_settings", Context.MODE_PRIVATE).edit().apply {
            putString("token",token)
            putString("tokenSecret", tokenSecret)
            commit()
        }
    }
}