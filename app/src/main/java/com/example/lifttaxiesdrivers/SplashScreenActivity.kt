package com.example.lifttaxiesdrivers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent =Intent(this,PhoneActivity::class.java)
        startActivity(intent)
        finish()
    }
    }