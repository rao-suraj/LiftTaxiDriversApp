package com.example.lifttaxiesdrivers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lifttaxiesdrivers.databinding.ActivityRegisterBinding
import com.google.android.gms.common.internal.Objects
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        binding.registerBt.setOnClickListener{
            val firstName=binding.firstNameText.text.toString()
            val lastName=binding.lastNameText.text.toString()
            val emailId=binding.editEmailAddress.text.toString()
            val phNumber=binding.editTextPhone.text.toString()
            val password=binding.editTextPassword.text.toString()
            val rating=0.0
//            Toast.makeText(this,auth.currentUser!!.uid,Toast.LENGTH_LONG).show()
            database=FirebaseDatabase.getInstance().getReference("DriverInfo")
            val driverInfo=DriverInfo(firstName,lastName,emailId,phNumber,password,rating)
            database.child(auth.currentUser!!.uid).setValue(driverInfo).addOnSuccessListener{
                Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Fail Success",Toast.LENGTH_LONG).show()
            }
        }
    }
}