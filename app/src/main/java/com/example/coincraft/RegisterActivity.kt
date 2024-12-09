package com.example.coincraft
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coincraft.LoginActivity
import com.example.coincraft.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var retryPassword: EditText
    private lateinit var registerButton: ImageView
    private lateinit var database: Database
    private val repository = UserRepository()

    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()
        database = Database()

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        retryPassword = findViewById(R.id.retryPassword)
        username = findViewById(R.id.userName)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            if (password.text.toString() == retryPassword.text.toString()) {
                if (isValidPassword(password.text.toString().trim())) {
                    registerNewUser()
                } else {
                    Toast.makeText(applicationContext, "Password is Invalid", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerNewUser() {
        val mail = email.text.toString()
        val passP = password.text.toString()
        val user = username.text.toString()

        if (mail.isEmpty()) {
            Toast.makeText(applicationContext, "Please enter email!", Toast.LENGTH_SHORT).show()
            return
        }

        if (user.isEmpty()) {
            Toast.makeText(applicationContext, "Please enter username!", Toast.LENGTH_SHORT).show()
            return
        }

        if (passP.isEmpty()) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(mail, passP).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User created successfully with email: $mail")
                val userId = firebaseAuth.currentUser?.uid

                userId?.let { uid ->
                    val newUser = User(
                        email = mail,
                        username = user,
                        password = passP,
                        hp = 100,
                        xp = 0
                    )

//                    repository.saveUser(uid, newUser, object : Database.OnDatabaseActionCompleteListener {
//                        override fun onSuccess() {
//                            Log.d(TAG, "User data saved successfully to Firebase Database")
//                            Toast.makeText(this@RegisterActivity, "Register Successfully", Toast.LENGTH_SHORT).show()
//                            val registerIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
//                            startActivity(registerIntent)
//                            finish()
//                        }
//
//                        override fun onFailure(e: Exception) {
//                            Log.e(TAG, "Failed to save user data: ", e)
//                            Toast.makeText(this@RegisterActivity, "Failed to save user data", Toast.LENGTH_SHORT).show()
//                        }
//                    })

                    repository.saveUser(uid, newUser) { success, error ->
                        if (success) {
                            Log.d(TAG, "User data saved successfully to Firebase Database")
                            Toast.makeText(
                                this@RegisterActivity,
                                "Register Successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Navigate to LoginActivity
                            val registerIntent =
                                Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(registerIntent)
                            finish()
                        } else {
                            Log.e(TAG, "Failed to save user data: $error")
                            Toast.makeText(
                                this@RegisterActivity,
                                "Failed to save user data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Log.e(TAG, "Register failed: ", task.exception)
                Toast.makeText(this@RegisterActivity, "Register Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$"
        return password.matches(PASSWORD_PATTERN.toRegex())
    }
}
