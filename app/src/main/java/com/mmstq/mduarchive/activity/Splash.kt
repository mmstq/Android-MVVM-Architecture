package com.mmstq.mduarchive.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mmstq.mduarchive.R
import kotlinx.coroutines.*

class Splash : AppCompatActivity() {
    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        activityScope.launch {
            delay(2000)

            val intent  = Intent(this@Splash, MainActivity::class.java)

            startActivity(intent)

            finish()
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()

    }
}