package com.example.medimingle

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.medimingle.presentation.MainActivity

class Signup : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editTextName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        editTextName = findViewById(R.id.editTextText45)
        val continueBtn = findViewById<Button>(R.id.continueBtn)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        continueBtn.setOnClickListener {
            val userName = editTextName.text.toString().trim()
            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else {
                saveUserName(userName)
                val intent = Intent(this, GetStarted::class.java)
                intent.putExtra("NAME_KEY", userName)  // Pass the name to OnboardScreen3
                startActivity(intent)
            }
        }
    }

    private fun saveUserName(userName: String) {
        val editor = sharedPreferences.edit()
        editor.putString("Name", userName)
        editor.apply()
    }
}