package com.example.coincraft

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val database = Database()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

            val user = User("example@test.com", "TestUser", "password123",100.0,0.0)
        database.saveUser("user123", user, object : Database.OnDatabaseActionCompleteListener {
            override fun onSuccess() {
                println("User saved successfully!")
            }

            override fun onFailure(e: Exception) {
                println("Error saving user: ${e.message}")
            }
        })
    }
}