package com.example.coincraft

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExpenseViewModel : ViewModel() {
    private val repository = ExpenseRepository()
    val transactionUpdated = MutableLiveData<Boolean>()

    // Add an expense
    fun addExpense(userId: String, expense: TransactionModel, onComplete: (Boolean, String?) -> Unit) {
        repository.addExpense(userId, expense, onComplete)
    }

    // Retrieve all expenses
    fun getAllExpenses(userId: String, onComplete: (List<TransactionModel>, String?) -> Unit) {
        repository.getAllExpenses(userId, onComplete)
    }

    // Update an existing expense
    fun updateExpense(
        userId: String,
        expenseID: String,
        updatedExpense: TransactionModel,
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

    fun observeTotalExpenses(userId: String, onUpdate: (Double) -> Unit) {
        repository.observeTotalExpenses(userId, onUpdate)
    }

    fun getExpensesByMonth(userId: String, selectedMonth: Int, onComplete: (MutableList<TransactionModel>, String?) -> Unit) {
        repository.getExpensesByMonth(userId, selectedMonth,onComplete)
    }
}