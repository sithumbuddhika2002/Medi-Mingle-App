package com.example.medimingle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OnboardScreen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard_screen2)

        // Find the "Next" button by its ID
        val nextButton = findViewById<Button>(R.id.Next2)
        nextButton.setOnClickListener {
            // Navigate to OnboardScreen2 when the button is clicked
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }
}