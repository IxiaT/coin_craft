package com.example.coincraft

import android.util.Log
import androidx.room.util.copy
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExpenseRepository {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    // Create
    fun addExpense(userId: String, expense: TransactionModel, onComplete: (Boolean, String?) -> Unit) {
        val expenseRef = databaseReference.child(userId).child("Expenses").push()
        val expenseId = expenseRef.key // Get the generated unique ID for the node

        if (expenseId != null) {
            expense.id = expenseId // Copy the expense with the ID set
            expenseRef.setValue(expense)
                .addOnSuccessListener { onComplete(true, null) }
                .addOnFailureListener { onComplete(false, it.message) }
        } else {
            onComplete(false, "Failed to generate expense ID")
        }
    }

    // Read
    fun getAllExpenses(userId: String, onComplete: (MutableList<TransactionModel>, String?) -> Unit) {
        val expenseRef = databaseReference.child(userId).child("Expenses")
        expenseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenseList = mutableListOf<TransactionModel>()
                for (expenseSnapshot in snapshot.children) {
                    val expense = expenseSnapshot.getValue(TransactionModel::class.java)
                    expense?.let { expenseList.add(it) }
                }
                onComplete(expenseList, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(mutableListOf(), error.message)
            }
        })
    }

    // Update
    fun updateExpense(userId: String, expenseId: String, updatedExpense: TransactionModel, onComplete: (Boolean, String?) -> Unit) {
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
                    .mapNotNull { it.getValue(TransactionModel::class.java) }
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
                    .mapNotNull { it.getValue(TransactionModel::class.java) }
                    .sumOf { it.amount }
                onUpdate(totalSum)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ExpenseRepository", "Error observing expenses: ${error.message}")
            }
        })
    }

    fun getSpendingForWeek(userId: String, onComplete: (Map<String, Double>, String?) -> Unit) {
        val expensesRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Expenses")
        expensesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dailySpending = mutableMapOf<String, Double>()
                for (expenseSnapshot in snapshot.children) {
                    val expense = expenseSnapshot.getValue(TransactionModel::class.java)
                    val date = expense?.date ?: continue
                    val day = getDayOfWeek(date)

                    val amount = expense.amount
                    dailySpending[day] = dailySpending.getOrDefault(day, 0.0) + amount
                }
                onComplete(dailySpending, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(emptyMap(), error.message)
            }
        })
    }

    private fun getDayOfWeek(date: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(date) ?: Date()
        return SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
    }

    fun getExpensesByMonth(userId: String, selectedMonth: Int, onComplete: (MutableList<TransactionModel>, String?) -> Unit) {
        val expensesRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Expenses")

        expensesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenses = mutableListOf<TransactionModel>()
                for (expenseSnapshot in snapshot.children) {
                    val expense = expenseSnapshot.getValue(TransactionModel::class.java)
                    val expenseDate = expense?.date ?: continue

                    // Parse the expense date to check if it matches the selected month
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val expenseCalendar = Calendar.getInstance()
                    expenseCalendar.time = dateFormat.parse(expenseDate) ?: continue

                    if (expenseCalendar.get(Calendar.MONTH) == selectedMonth) {
                        expenses.add(expense)
                    }
                }
                onComplete(expenses, null)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(mutableListOf(), error.message)
            }
        })
    }
}