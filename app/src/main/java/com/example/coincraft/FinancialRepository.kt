package com.example.coincraft

import com.google.firebase.database.*

class FinancialRepository {

    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("Users") // Ensure consistent path naming

    // Add Financial Goal with Firebase-generated ID
    fun addFinancialGoal(userId: String, goal: FinancialModel, callback: (Boolean, String?) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("goals")
        val newGoalRef = databaseReference.push() // Generate a unique key
        val key = newGoalRef.key // Retrieve the key

        if (key != null) {
            goal.key = key // Assign the key to the goal object
            newGoalRef.setValue(goal)
                .addOnSuccessListener {
                    callback(true, null)
                }
                .addOnFailureListener { error ->
                    callback(false, error.message)
                }
        } else {
            callback(false, "Failed to generate a unique key")
        }
    }

    // Get Financial Goals
    fun getFinancialGoals(userId: String, onComplete: (List<FinancialModel>, String?) -> Unit) {
        val financialRef = databaseReference.child(userId).child("FinancialGoals")
        financialRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val financialList = mutableListOf<FinancialModel>()
                for (goalSnapshot in snapshot.children) {
                    val financialGoal = goalSnapshot.getValue(FinancialModel::class.java)
                    // Attach the Firebase key as a property on the model (temporary)
                    financialGoal?.let {
                        it.key = goalSnapshot.key // Get the Firebase-generated key
                        financialList.add(it)
                    }
                }
                onComplete(financialList, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(emptyList(), error.message)
            }
        })
    }

    // Update a Financial Goal by its Firebase ID
    fun updateFinancialGoal(userId: String, goalId: String, updatedGoal: FinancialModel, callback: (Boolean, String?) -> Unit) {
        val goalRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("FinancialGoals").child(goalId)
        goalRef.setValue(updatedGoal)
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message)
            }
    }

    fun deleteFinancialGoal(userId: String, goalId: String, callback: (Boolean, String?) -> Unit) {
        val goalRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("FinancialGoals").child(goalId)
        goalRef.removeValue()
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message)
            }
    }

}
