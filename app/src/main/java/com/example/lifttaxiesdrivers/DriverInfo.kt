package com.example.lifttaxiesdrivers

import android.media.Rating
import android.text.Editable
import android.widget.EditText
import android.widget.TextView

data class DriverInfo(
    var firstName: String,
    var lastName: String,
    var emailID: String,
    var phoneNumber: String,
    val password: String,
    var rating: Double
)
