package com.example.weatherapplication.Activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R
import kotlinx.coroutines.Delay

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed(
            {
            val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            },4000
        )
    }
}