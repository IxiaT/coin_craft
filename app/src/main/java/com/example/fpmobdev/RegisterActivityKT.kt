package com.example.fpmobdev

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class RegisterActivityKT : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var email: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var retryPassword: EditText
    private lateinit var registerButton: ImageView

    companion object {
        private const val TAG = "RegisterActivityKT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        enableEdgeToEdge()

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://fp-mobdev-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("UserAccounts")
        Log.d(TAG, "Database reference initialized: $databaseReference")

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

        when {
            TextUtils.isEmpty(mail) -> {
                Toast.makeText(applicationContext, "Please enter email!", Toast.LENGTH_SHORT).show()
                return
            }
            TextUtils.isEmpty(user) -> {
                Toast.makeText(applicationContext, "Please enter Name!", Toast.LENGTH_SHORT).show()
                return
            }
            TextUtils.isEmpty(passP) -> {
                Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_SHORT).show()
                return
            }
        }

        firebaseAuth.createUserWithEmailAndPassword(mail, passP).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User created successfully with email: $mail")
                saveUserToDatabase(mail, user, passP)
            } else {
                Log.e(TAG, "Register failed: ", task.exception)
                Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserToDatabase(email: String, username: String, password: String) {
        firebaseAuth.currentUser?.let { currentUser ->
            val userId = currentUser.uid
            Log.d(TAG, "Attempting to save user with UID: $userId")

            val user = UserKT(email, username, password)
            val userRef = databaseReference.child(userId)

            userRef.child("User").setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User saved successfully to database")

                    // Initialize default tables
                    initializeDefaultTables(userRef)

                    Toast.makeText(this, "Register Successfully", Toast.LENGTH_SHORT).show()
                    val registerIntent = Intent(this, LoginActivityKT::class.java)
                    startActivity(registerIntent)
                    finish()
                } else {
                    Log.e(TAG, "Failed to save user details to database", task.exception)
                    Toast.makeText(this, "Failed to save user details", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: run {
            Log.e(TAG, "User authentication failed; UID is null")
            Toast.makeText(this, "User authentication failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeDefaultTables(userRef: DatabaseReference) {
        val expensesRef = userRef.child("Expenses")
        expensesRef.child("default").setValue(ExpenseKT("Miscellaneous", 0.0, "2024-01-01"))

        val incomeRef = userRef.child("Income")
        incomeRef.child("default").setValue(IncomeKT("Initial Income", 0.0, "2024-01-01"))

        val budgetsRef = userRef.child("Budgets")
        budgetsRef.child("default").setValue(BudgetKT("General", 0.0, 0.0, 0.0))

        val goalsRef = userRef.child("Goals")
        goalsRef.child("default").setValue(GoalKT("New Goal", 0.0, 0.0, "2024-12-31"))

        val debtsRef = userRef.child("Debts")
        debtsRef.child("default").setValue(DebtKT("No one", 0.0, "2024-01-01", "Receivable"))

        Log.d(TAG, "Default tables initialized")
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$"
        return Pattern.compile(passwordPattern).matcher(password).matches()
    }
}
