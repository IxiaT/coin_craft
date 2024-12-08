package com.example.coincraft

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Database {

    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance("https://fp-mobdev-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .getReference("UserAccounts")

    init {
        Log.d(TAG, "Database reference initialized: $databaseReference")
    }

    fun saveUser(userId: String, user: User, listener: OnDatabaseActionCompleteListener) {
        val userRef = databaseReference.child(userId).child("User")
        userRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User saved successfully to database")
                initializeDefaultTables(userId)
                listener.onSuccess()
            } else {
                Log.e(TAG, "Failed to save user details to database", task.exception)
                listener.onFailure(task.exception ?: Exception("Unknown error"))
            }
        }
    }

    private fun initializeDefaultTables(userId: String) {
        val userRef = databaseReference.child(userId)

        userRef.child("Expenses").child("default").setValue(Expense("Miscellaneous", 0.0, "2024-01-01"))
        userRef.child("Income").child("default").setValue(Income("Initial Income", 0.0, "2024-01-01"))
        userRef.child("Budgets").child("default").setValue(Budget("General", 0.0, 0.0, 0.0))
        userRef.child("Goals").child("default").setValue(Goal("New Goal", 0.0, 0.0, "2024-12-31"))
        userRef.child("Debts").child("default").setValue(Debt("No one", 0.0, "2024-01-01", "Receivable"))

        Log.d(TAG, "Default tables initialized")
    }

    interface OnDatabaseActionCompleteListener {
        fun onSuccess()
        fun onFailure(e: Exception)
    }

    companion object {
        private const val TAG = "DatabaseHelper"
    }
}