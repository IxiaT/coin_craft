package com.example.coincraft

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FinancialRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("Users")

    // Create or Add Financial Goal
    fun addFinancialGoal(userId: String, financialGoal: FinancialModel, onComplete: (Boolean, String?) -> Unit) {
        val financialRef = databaseReference.child(userId).child("FinancialGoals").push()
        financialRef.setValue(financialGoal)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Read All Financial Goals for a User
    fun getFinancialGoals(userId: String, onComplete: (List<FinancialModel>, String?) -> Unit) {
        val financialRef = databaseReference.child(userId).child("FinancialGoals")
        financialRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val financialList = mutableListOf<FinancialModel>()
                for (goalSnapshot in snapshot.children) {
                    val financialGoal = goalSnapshot.getValue(FinancialModel::class.java)
                    financialGoal?.let { financialList.add(it) }
                }
                onComplete(financialList, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(emptyList(), error.message)
            }
        })
    }

    // Update a Financial Goal
    fun updateFinancialGoal(userId: String, goalId: String, updatedGoal: FinancialModel, onComplete: (Boolean, String?) -> Unit) {
        val financialRef = databaseReference.child(userId).child("FinancialGoals").child(goalId)
        financialRef.setValue(updatedGoal)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Delete a Financial Goal
    fun deleteFinancialGoal(userId: String, goalId: String, onComplete: (Boolean, String?) -> Unit) {
        val financialRef = databaseReference.child(userId).child("FinancialGoals").child(goalId)
        financialRef.removeValue()
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }
}