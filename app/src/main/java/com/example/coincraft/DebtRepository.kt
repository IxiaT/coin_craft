package com.example.coincraft

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DebtRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("Users")

    // Create
    fun addDebt(userId: String, debt: DebtCardModel, onComplete: (Boolean, String?) -> Unit) {
        val debtRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Debts")
        debtRef.setValue(debt)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Read
    fun getAllDebts(userId: String, onComplete: (List<DebtCardModel>, String?) -> Unit) {
        val debtRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Debts")
        debtRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val debtList = mutableListOf<DebtCardModel>()
                for (debtSnapshot in snapshot.children) {
                    val debt = debtSnapshot.getValue(DebtCardModel::class.java)
                    debt?.let { debtList.add(it) }
                }
                onComplete(debtList, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(emptyList(), error.message)
            }
        })
    }

    // Update
    fun updateDebt(userId: String, debtId: String, updatedDebt: DebtCardModel, onComplete: (Boolean, String?) -> Unit) {
        val debtRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Debts")
        debtRef.setValue(updatedDebt)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Delete
    fun deleteDebt(userId: String, debtId: String, onComplete: (Boolean, String?) -> Unit) {
        val debtRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Debts")
        debtRef.removeValue()
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }
}