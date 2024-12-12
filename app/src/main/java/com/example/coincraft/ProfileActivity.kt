package com.example.coincraft

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailField: EditText = findViewById(R.id.edittxt_email)
        val passwordField: EditText = findViewById(R.id.edittxt_password)
        val saveButton: ImageButton = findViewById(R.id.imgbtn_save)

        saveButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (isValidEmail(email) && isValidPassword(password)) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                // Save logic here
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        // Password regex: At least one digit, one lowercase, one uppercase, one special character, and 8-16 characters long
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,16}\$"
        return password.matches(passwordRegex.toRegex())
    }
}
