package com.example.coincraft

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ExpenseRepository {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    // Create
    fun addExpense(userId: String, expense: ExpenseModel, onComplete: (Boolean, String?) -> Unit) {
        val expenseRef = databaseReference.child(userId).child("Expenses").push()
        expenseRef.setValue(expense)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Read
    fun getAllExpenses(userId: String, onComplete: (List<ExpenseModel>, String?) -> Unit) {
        val expenseRef = databaseReference.child(userId).child("Expenses")
        expenseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenseList = mutableListOf<ExpenseModel>()
                for (expenseSnapshot in snapshot.children) {
                    val expense = expenseSnapshot.getValue(ExpenseModel::class.java)
                    expense?.let { expenseList.add(it) }
                }
                onComplete(expenseList, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(emptyList(), error.message)
            }
        })
    }

    // Update
    fun updateExpense(userId: String, expenseId: String, updatedExpense: ExpenseModel, onComplete: (Boolean, String?) -> Unit) {
        val expenseRef = databaseReference.child(userId).child("Expenses").child(expenseId)
        expenseRef.setValue(updatedExpense)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Delete
    fun deleteExpense(userId: String, expenseId: String, onComplete: (Boolean, String?) -> Unit) {
        val expenseRef = databaseReference.child(userId).child("Expenses").child(expenseId)
        expenseRef.removeValue()
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    fun getTotalExpenses(userId: String, onComplete: (Double, String?) -> Unit) {
        val expenseRef = databaseReference.child(userId).child("Expenses")
        expenseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalSum = snapshot.children
                    .mapNotNull { it.getValue(ExpenseModel::class.java) }
                    .sumOf { it.amount }
                onComplete(totalSum, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(0.0, error.message)
            }
        })
    }

    fun observeTotalExpenses(userId: String, onUpdate: (Double) -> Unit) {
        val expenseRef = databaseReference.child(userId).child("Expenses")
        expenseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalSum = snapshot.children
                    .mapNotNull { it.getValue(ExpenseModel::class.java) }
                    .sumOf { it.amount }
                onUpdate(totalSum)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ExpenseRepository", "Error observing expenses: ${error.message}")
            }
        })
    }
}