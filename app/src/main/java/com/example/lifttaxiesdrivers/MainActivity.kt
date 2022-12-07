package com.example.lifttaxiesdrivers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lifttaxiesdrivers.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        binding.button.setOnClickListener{
            Toast.makeText(this,auth.uid,Toast.LENGTH_LONG).show()
        }

        binding.button2.setOnClickListener{
            auth.signOut()
            val intent=Intent(this,PhoneActivity::class.java)
            startActivity(intent)
        }
    }
}