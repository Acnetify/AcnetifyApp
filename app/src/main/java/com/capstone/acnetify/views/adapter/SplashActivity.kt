package com.capstone.acnetify.views.adapter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.capstone.acnetify.AcnetifyApplication
import com.capstone.acnetify.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 3000L // Durasi dalam milidetik (3 detik)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            // Navigasi ke MainActivity setelah durasi splash screen
            val mainIntent = Intent(this@SplashActivity, AcnetifyApplication::class.java)
            startActivity(mainIntent)
            finish()
        }, SPLASH_DISPLAY_LENGTH)
    }
}
