package com.example.coincraft

import androidx.lifecycle.ViewModel

class ExpenseViewModel : ViewModel() {
    private val repository = ExpenseRepository()

    // Add an expense
    fun addExpense(userId: String, expense: ExpenseModel, onComplete: (Boolean, String?) -> Unit) {
        repository.addExpense(userId, expense, onComplete)
    }

    // Retrieve all expenses
    fun getAllExpenses(userId: String, onComplete: (List<ExpenseModel>, String?) -> Unit) {
        repository.getAllExpenses(userId, onComplete)
    }

    // Update an existing expense
    fun updateExpense(
        userId: String,
        expenseID: String,
        updatedExpense: ExpenseModel,
        onComplete: (Boolean, String?) -> Unit
    ) {
        repository.updateExpense(userId, expenseID, updatedExpense, onComplete)
    }

    // Delete an expense
    fun deleteExpense(userId: String, expenseID: String, onComplete: (Boolean, String?) -> Unit) {
        repository.deleteExpense(userId, expenseID, onComplete)
    }

    fun getTotalExpenses(userId: String, onComplete: (Double, String?) -> Unit) {
        repository.getTotalExpenses(userId, onComplete)
    }
}