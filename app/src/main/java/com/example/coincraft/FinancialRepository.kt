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

    fun insertDummyFinancialGoals(userId: String, onComplete: (Boolean, String?) -> Unit) {
        val financialRef = databaseReference.child(userId).child("FinancialGoals")

        val dummyGoals = listOf(
            FinancialModel(goalName = "Buy a Car", goalType = "Personal", amount = 5000.0, date = "2025-01-01"),
            FinancialModel(goalName = "Vacation Fund", goalType = "Savings", amount = 3000.0, date = "2024-06-15"),
            FinancialModel(goalName = "Emergency Fund", goalType = "Savings", amount = 10000.0, date = "2024-12-31"),
            FinancialModel(goalName = "Start a Business", goalType = "Business", amount = 15000.0, date = "2025-05-01")
        )

        // Add each goal to the database
        dummyGoals.forEach { goal ->
            val goalRef = financialRef.push()
            goalRef.setValue(goal)
                .addOnSuccessListener { onComplete(true, null) }
                .addOnFailureListener { onComplete(false, it.message) }
        }
    }
}