package com.example.coincraft

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BudgetRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("UserAccounts")

    // Add Budget (Decides table based on budgetType)
    fun addBudget(userId: String, budget: BudgetModel, onComplete: (Boolean, String?) -> Unit) {
        val budgetTypeTable = if (budget.budgetType.equals("Needs", ignoreCase = true)) {
            "BudgetNeeds"
        } else if (budget.budgetType.equals("Wants", ignoreCase = true)) {
            "BudgetWants"
        } else {
            onComplete(false, "Invalid budget type. Use 'Needs' or 'Wants'")
            return
        }

        val budgetRef = databaseReference.child(userId).child(budgetTypeTable).push()
        budgetRef.setValue(budget)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Retrieve All Budgets (Needs or Wants)
    fun getBudgets(
        userId: String,
        onComplete: (List<BudgetModel>, List<BudgetModel>, String?) -> Unit
    ) {
        val needsRef = databaseReference.child(userId).child("BudgetNeeds")
        val wantsRef = databaseReference.child(userId).child("BudgetWants")

        val needsList = mutableListOf<BudgetModel>()
        val wantsList = mutableListOf<BudgetModel>()

        var needsCompleted = false
        var wantsCompleted = false

        // Fetch Needs
        needsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (budgetSnapshot in snapshot.children) {
                    val budget = budgetSnapshot.getValue(BudgetModel::class.java)
                    budget?.let { needsList.add(it) }
                }
                needsCompleted = true
                if (wantsCompleted) {
                    onComplete(needsList, wantsList, null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                needsCompleted = true
                if (wantsCompleted) {
                    onComplete(emptyList(), emptyList(), error.message)
                }
            }
        })

        // Fetch Wants
        wantsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (budgetSnapshot in snapshot.children) {
                    val budget = budgetSnapshot.getValue(BudgetModel::class.java)
                    budget?.let { wantsList.add(it) }
                }
                wantsCompleted = true
                if (needsCompleted) {
                    onComplete(needsList, wantsList, null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                wantsCompleted = true
                if (needsCompleted) {
                    onComplete(emptyList(), emptyList(), error.message)
                }
            }
        })
    }

    // Compute Total Limit (Needs or Wants)
    fun computeTotalLimit(
        userId: String,
        budgetType: String,
        onComplete: (Double, String?) -> Unit
    ) {
        val budgetRef = when (budgetType.lowercase()) {
            "needs" -> databaseReference.child(userId).child("BudgetNeeds")
            "wants" -> databaseReference.child(userId).child("BudgetWants")
            else -> {
                onComplete(0.0, "Invalid budget type. Use 'Needs' or 'Wants'")
                return
            }
        }

        budgetRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalLimit = snapshot.children
                    .mapNotNull { it.getValue(BudgetModel::class.java) }
                    .sumOf { it.budgetLimit }
                onComplete(totalLimit, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(0.0, error.message)
            }
        })
    }

    // Update Budget
    fun updateBudget(userId: String, budgetType: String, budgetId: String, updatedBudget: BudgetModel, onComplete: (Boolean, String?) -> Unit) {
        val budgetTypeTable = if (budgetType.equals("Needs", ignoreCase = true)) {
            "BudgetNeeds"
        } else if (budgetType.equals("Wants", ignoreCase = true)) {
            "BudgetWants"
        } else {
            onComplete(false, "Invalid budget type. Use 'Needs' or 'Wants'")
            return
        }

        val budgetRef = databaseReference.child(userId).child(budgetTypeTable).child(budgetId)
        budgetRef.setValue(updatedBudget)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    // Delete Budget
    fun deleteBudget(userId: String, budgetType: String, budgetId: String, onComplete: (Boolean, String?) -> Unit) {
        val budgetTypeTable = if (budgetType.equals("Needs", ignoreCase = true)) {
            "BudgetNeeds"
        } else if (budgetType.equals("Wants", ignoreCase = true)) {
            "BudgetWants"
        } else {
            onComplete(false, "Invalid budget type. Use 'Needs' or 'Wants'")
            return
        }

        val budgetRef = databaseReference.child(userId).child(budgetTypeTable).child(budgetId)
        budgetRef.removeValue()
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { onComplete(false, it.message) }
    }

    fun observeBudgets(
        userId: String,
        onChange: (MutableList<BudgetModel>, MutableList<BudgetModel>, String?) -> Unit
    ) {
        val needsRef = databaseReference.child(userId).child("BudgetNeeds")
        val wantsRef = databaseReference.child(userId).child("BudgetWants")

        val needsList = mutableListOf<BudgetModel>()
        val wantsList = mutableListOf<BudgetModel>()

        // Observe Needs
        needsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                needsList.clear()
                for (budgetSnapshot in snapshot.children) {
                    val budget = budgetSnapshot.getValue(BudgetModel::class.java)
                    budget?.let { needsList.add(it) }
                }
                onChange(needsList, wantsList, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onChange(mutableListOf(), mutableListOf(), error.message)
            }
        })

        // Observe Wants
        wantsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                wantsList.clear()
                for (budgetSnapshot in snapshot.children) {
                    val budget = budgetSnapshot.getValue(BudgetModel::class.java)
                    budget?.let { wantsList.add(it) }
                }
                onChange(needsList, wantsList, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onChange(mutableListOf(), mutableListOf(), error.message)
            }
        })
    }
}