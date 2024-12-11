package com.example.coincraft


import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: ImageView
    private lateinit var signupText: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.loginEmail)
        password = findViewById(R.id.loginPassword)
        loginButton = findViewById(R.id.loginButton)
        signupText = findViewById(R.id.signupText)
        auth = FirebaseAuth.getInstance()

        sharedPreference = getSharedPreferences("my_pref", MODE_PRIVATE)

        val savedUser = sharedPreference.getString("User", null)
        if (savedUser != null) {
            navigateToHome()
        }

        loginButton.setOnClickListener {
            userLogin()
        }

        signupText.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun userLogin() {
        val logEmail = email.text.toString()
        val logPass = password.text.toString()

        if (logEmail.isEmpty() || logPass.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(logEmail, logPass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    retrieveUserData(userId)
                }
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun retrieveUserData(userId: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        // Retrieve User Data from Firebase Realtime Database
        userRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    saveUserData(userId, user)
                    navigateToHome()
                } else {
                    Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error retrieving data: ${exception.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun saveUserData(userID: String, user: User) {
        editor = sharedPreference.edit()
        editor.putString("User", userID)
        editor.putString("username", user.username)
        editor.putString("hp", user.hp.toString())
        editor.putString("xp", user.xp.toString())
        editor.apply()
    }

    private fun navigateToHome() {
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
        // Navigate to the Home Activity
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }
}