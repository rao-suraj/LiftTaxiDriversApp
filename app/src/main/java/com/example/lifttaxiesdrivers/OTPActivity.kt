package com.example.lifttaxiesdrivers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.lifttaxiesdrivers.databinding.ActivityOtpactivityBinding
import com.example.lifttaxiesdrivers.databinding.ActivityPhoneBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.*
import kotlinx.coroutines.delay
import java.util.*
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    companion object{
        lateinit var OTP:String
    }

    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var phoneNumber: String
    private lateinit var database:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().getReference("DriverInfo")
        binding.otpProgressBar.visibility=View.INVISIBLE
        OTP = intent.getStringExtra("OTP").toString()
        resendingToken= intent.getParcelableExtra("resendToken")!!
        phoneNumber =intent.getStringExtra("phoneNumber")!!

        addTextChangeListener()
        resendOTPTyVisibility()

        binding.resendTextView.setOnClickListener{

            resendVerificationCode()
            resendOTPTyVisibility()
        }

        binding.verifyOTPBtn.setOnClickListener{
            val typedOTP = (binding.otpEditText1.text.toString()+binding.otpEditText2.text.toString()+binding.otpEditText3.text.toString()+binding.otpEditText4.text.toString()+
                    binding.otpEditText5.text.toString()+ binding.otpEditText6.text.toString())

            if(typedOTP.isNotEmpty()){
                if(typedOTP.length == 6){
                    val credential:PhoneAuthCredential=PhoneAuthProvider.getCredential(
                        OTP ,typedOTP
                    )
                    binding.otpProgressBar.visibility=View.VISIBLE
                    signInWithPhoneAuthCredential(credential)
                }else{
                    Toast.makeText(this,"Please Enter Correct OTP",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"Please Enter OTP",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun resendOTPTyVisibility(){
        binding.otpEditText1.setText("")
        binding.otpEditText2.setText("")
        binding.otpEditText3.setText("")
        binding.otpEditText4.setText("")
        binding.otpEditText5.setText("")
        binding.otpEditText6.setText("")
        binding.resendTextView.visibility=View.INVISIBLE
        binding.resendTextView.isEnabled=false

        Handler(Looper.myLooper()!!).postDelayed({
            binding.resendTextView.visibility=View.VISIBLE
            binding.resendTextView.isEnabled=true
        }, 60000)
    }

    private fun resendVerificationCode(){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)// OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendingToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.

            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.


            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG","onVerificationFailed:$e")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG","onVerificationFailed:$e")
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.


            // Save verification ID and resending token so we can use them later
            OTP =verificationId
            resendingToken=token

        }
    }


        inner class EditTextWatcher(private val view:View):TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val text=s.toString()
                when(view.id){
                    R.id.otpEditText1 ->if(text.length == 1) binding.otpEditText2.requestFocus()
                    R.id.otpEditText2 ->if(text.length == 1) binding.otpEditText3.requestFocus() else if(text.isEmpty()) binding.otpEditText1.requestFocus()
                    R.id.otpEditText3 ->if(text.length == 1) binding.otpEditText4.requestFocus() else if(text.isEmpty()) binding.otpEditText2.requestFocus()
                    R.id.otpEditText4 ->if(text.length == 1) binding.otpEditText5.requestFocus() else if(text.isEmpty()) binding.otpEditText3.requestFocus()
                    R.id.otpEditText5 ->if(text.length == 1) binding.otpEditText6.requestFocus() else if(text.isEmpty()) binding.otpEditText4.requestFocus()
                    R.id.otpEditText6 ->if(text.isEmpty()) binding.otpEditText5.requestFocus()
                }
            }

        }

    private fun addTextChangeListener(){
        binding.otpEditText1.addTextChangedListener(EditTextWatcher(binding.otpEditText1))
        binding.otpEditText2.addTextChangedListener(EditTextWatcher(binding.otpEditText2))
        binding.otpEditText3.addTextChangedListener(EditTextWatcher(binding.otpEditText3))
        binding.otpEditText4.addTextChangedListener(EditTextWatcher(binding.otpEditText4))
        binding.otpEditText5.addTextChangedListener(EditTextWatcher(binding.otpEditText5))
        binding.otpEditText6.addTextChangedListener(EditTextWatcher(binding.otpEditText6))
    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    binding.otpProgressBar.visibility=View.INVISIBLE
                    readData()
//                    sendToMain();
                    finish()

                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }

    private fun readData() {
        database.child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if(datasnapshot.exists()) {
                    sendToMain()
                    finish()
                }else {
                    sendTORegister()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OTPActivity,error.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun sendTORegister() {
        val intent=Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }


    private fun sendToMain(){
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }


}