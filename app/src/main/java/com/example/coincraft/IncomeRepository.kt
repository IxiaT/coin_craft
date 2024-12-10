package com.example.coincraft

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class IncomeRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("Users")

    // Create
    fun addIncome(userId: String, income: IncomeModel, onComplete: (Boolean, String?) -> Unit) {
        val incomeRef = databaseReference.child(userId).child("Income").push()
        incomeRef.setValue(income)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Read
    fun getAllIncome(userId: String, onComplete: (List<IncomeModel>, String?) -> Unit) {
        val incomeRef = databaseReference.child(userId).child("Income")
        incomeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val incomeList = mutableListOf<IncomeModel>()
                for (incomeSnapshot in snapshot.children) {
                    val income = incomeSnapshot.getValue(IncomeModel::class.java)
                    income?.let { incomeList.add(it) }
                }
                onComplete(incomeList, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(emptyList(), error.message)
            }
        })
    }

    // Update
    fun updateIncome(userId: String, incomeId: String, updatedIncome: IncomeModel, onComplete: (Boolean, String?) -> Unit) {
        val incomeRef = databaseReference.child(userId).child("Income").child(incomeId)
        incomeRef.setValue(updatedIncome)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Delete
    fun deleteIncome(userId: String, incomeId: String, onComplete: (Boolean, String?) -> Unit) {
        val incomeRef = databaseReference.child(userId).child("Income").child(incomeId)
        incomeRef.removeValue()
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    fun getTotalIncome(userId: String, onComplete: (Double, String?) -> Unit) {
        val incomeRef = databaseReference.child(userId).child("Income")
        incomeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalSum = snapshot.children
                    .mapNotNull { it.getValue(IncomeModel::class.java) }
                    .sumOf { it.amount }
                onComplete(totalSum, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(0.0, error.message)
            }
        })
    }

    fun observeTotalIncome(userId: String, onUpdate: (Double) -> Unit) {
        val incomeRef = databaseReference.child(userId).child("Income")
        incomeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalSum = snapshot.children
                    .mapNotNull { it.getValue(IncomeModel::class.java) }
                    .sumOf { it.amount }
                onUpdate(totalSum)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("IncomeRepository", "Error observing income: ${error.message}")
            }
        })
    }
}