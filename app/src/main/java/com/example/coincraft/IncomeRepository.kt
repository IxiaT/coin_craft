package com.example.coincraft

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IncomeRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("Users")

    // Create
    fun addIncome(userId: String, income: TransactionModel, onComplete: (Boolean, String?) -> Unit) {
        val incomeRef = databaseReference.child(userId).child("Income").push()
        val incomeId = incomeRef.key // Get the generated unique ID for the node

        if (incomeId != null) {
            income.id = incomeId // Copy the expense with the ID set
            incomeRef.setValue(income)
                .addOnSuccessListener { onComplete(true, null) }
                .addOnFailureListener { onComplete(false, it.message) }
        } else {
            onComplete(false, "Failed to generate income ID")
        }
    }

    // Read
    fun getAllIncome(userId: String, onComplete: (MutableList<TransactionModel>, String?) -> Unit) {
        val incomeRef = databaseReference.child(userId).child("Income")
        incomeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val incomeList = mutableListOf<TransactionModel>()
                for (incomeSnapshot in snapshot.children) {
                    val income = incomeSnapshot.getValue(TransactionModel::class.java)
                    income?.let { incomeList.add(it) }
                }
                onComplete(incomeList, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(mutableListOf(), error.message)
            }
        })
    }

    // Update
    fun updateIncome(userId: String, incomeId: String, updatedIncome: TransactionModel, onComplete: (Boolean, String?) -> Unit) {
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
                    .mapNotNull { it.getValue(TransactionModel::class.java) }
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
                    .mapNotNull { it.getValue(TransactionModel::class.java) }
                    .sumOf { it.amount }
                onUpdate(totalSum)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("IncomeRepository", "Error observing income: ${error.message}")
            }
        })
    }

    fun getIncomeByMonth(userId: String, selectedMonth: Int, onComplete: (MutableList<TransactionModel>, String?) -> Unit) {
        val incomeRef = databaseReference.child(userId).child("Income")

        incomeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val incomeList = mutableListOf<TransactionModel>()
                    for (incomeSnapshot in snapshot.children) {
                        val income = incomeSnapshot.getValue(TransactionModel::class.java)
                        if (income == null) {
                            Log.e("getIncomeByMonth", "Invalid income entry: $incomeSnapshot")
                            continue
                        }

                        val incomeDate = income.date
                        if (incomeDate.isNullOrEmpty()) {
                            Log.e("getIncomeByMonth", "Missing date for income: $income")
                            continue
                        }

                        // Parse and validate the income date
                        try {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val incomeCalendar = Calendar.getInstance()
                            incomeCalendar.time = dateFormat.parse(incomeDate) ?: continue

                            // Check if the income is in the selected month
                            if (incomeCalendar.get(Calendar.MONTH) == selectedMonth) {
                                incomeList.add(income)
                            }
                        } catch (e: Exception) {
                            Log.e("getIncomeByMonth", "Error parsing date: $incomeDate", e)
                        }
                    }
                    onComplete(incomeList, null)
                } catch (e: Exception) {
                    Log.e("getIncomeByMonth", "Error processing snapshot", e)
                    onComplete(mutableListOf(), e.message)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("getIncomeByMonth", "Database error: ${error.message}")
                onComplete(mutableListOf(), error.message)
            }
        })
    }
}