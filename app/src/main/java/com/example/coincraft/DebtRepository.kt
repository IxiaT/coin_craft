package com.example.coincraft

import android.util.Log
import com.google.firebase.database.*

class DebtRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("UserAccounts")

    // Create
    fun addDebt(userId: String, debt: DebtCardModel, onComplete: (Boolean, String?) -> Unit) {
        val debtRef = databaseReference.child(userId).child("Debt").push()

        // Convert DebtCardModel to a map
        val debtMap = mapOf(
            "id" to debt.id,
            "profileImage" to debt.profileImage,
            "name" to debt.name,
            "date" to debt.date,
            "coinImage" to debt.coinImage,
            "amount" to debt.amount,
            "state" to debt.state
        )

        debtRef.setValue(debtMap)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Read
    fun getAllDebts(userId: String, onComplete: (List<DebtCardModel>, String?) -> Unit) {
        val debtRef = databaseReference.child(userId).child("Debt")
        debtRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val debtList = mutableListOf<DebtCardModel>()
                Log.d("DebtRepository", "Snapshot: $snapshot")
                for (debtSnapshot in snapshot.children) {
                    val debtMap = debtSnapshot.getValue(Map::class.java) ?: continue
                    Log.d("DebtRepository", "Debt data: $debtMap")
                    val debt = DebtCardModel(
                        id = debtMap["id"] as? String ?: "",
                        profileImage = debtMap["profileImage"] as? Int ?: 0,
                        name = debtMap["name"] as? String ?: "Unknown",
                        date = debtMap["date"] as? String ?: "Unknown",
                        coinImage = debtMap["coinImage"] as? Int ?: 0,
                        amount = debtMap["amount"] as? String ?: "0",
                        state = debtMap["state"] as? String ?: "Unknown"
                    )
                    debtList.add(debt)
                }
                onComplete(debtList, null)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DebtRepository", "Error reading debts: ${error.message}")
                onComplete(emptyList(), error.message)
            }
        })
    }

    // Update
    fun updateDebt(userId: String, debtId: String, updatedDebt: DebtCardModel, onComplete: (Boolean, String?) -> Unit) {
        val debtRef = databaseReference.child(userId).child("Debt").child(debtId)

        // Convert DebtCardModel to a map
        val debtMap = mapOf(
            "id" to updatedDebt.id,
            "profileImage" to updatedDebt.profileImage,
            "name" to updatedDebt.name,
            "date" to updatedDebt.date,
            "coinImage" to updatedDebt.coinImage,
            "amount" to updatedDebt.amount,
            "state" to updatedDebt.state
        )

        debtRef.setValue(debtMap)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Delete
    fun deleteDebt(userId: String, debtId: String, onComplete: (Boolean, String?) -> Unit) {
        val debtRef = databaseReference.child(userId).child("Debt").child(debtId)
        debtRef.removeValue()
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }
}
