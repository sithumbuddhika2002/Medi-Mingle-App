package com.example.medimingle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.medimingle.presentation.MainActivity

class OnboardScreen3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard_screen3)

        val userName = intent.getStringExtra("NAME_KEY") ?: "User"
        val welcomeTextView = findViewById<TextView>(R.id.welcomeTextView)

        welcomeTextView.text = "Welcome, $userName!"

        val getStart = findViewById<Button>(R.id.getstarted)
        getStart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}