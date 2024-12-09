package com.example.coincraft

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("Users")

    // Create or Update User
    fun saveUser(userId: String, user: User, onComplete: (Boolean, String?) -> Unit) {
        val userRef = databaseReference.child(userId)
        userRef.setValue(user)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Read User
    fun getUser(userId: String, onComplete: (User?, String?) -> Unit) {
        val userRef = databaseReference.child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                onComplete(user, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null, error.message)
            }
        })
    }

    // Update User Properties (Partial Update)
    fun updateUser(userId: String, updates: Map<String, Any>, onComplete: (Boolean, String?) -> Unit) {
        val userRef = databaseReference.child(userId)
        userRef.updateChildren(updates)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Delete User
    fun deleteUser(userId: String, onComplete: (Boolean, String?) -> Unit) {
        val userRef = databaseReference.child(userId)
        userRef.removeValue()
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }
}