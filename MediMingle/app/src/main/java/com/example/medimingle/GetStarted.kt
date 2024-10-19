package com.example.medimingle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.medimingle.presentation.MainActivity

class GetStarted : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        val userName = intent.getStringExtra("NAME_KEY") ?: "User"
        val welcomeTextView = findViewById<TextView>(R.id.welcomeTextView)

        welcomeTextView.text = "Hello $userName!"

        val getStart = findViewById<Button>(R.id.started)
        getStart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    } }