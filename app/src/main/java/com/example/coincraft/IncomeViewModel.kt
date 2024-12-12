package com.example.coincraft

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IncomeViewModel : ViewModel() {
    private val repository = IncomeRepository()
    val transactionUpdated = MutableLiveData<Boolean>()

    // Add an income
    fun addIncome(userId: String, income: TransactionModel, onComplete: (Boolean, String?) -> Unit) {
        repository.addIncome(userId, income, onComplete)
    }

    // Get all incomes
    fun getAllIncome(userId: String, onComplete: (List<TransactionModel>, String?) -> Unit) {
        repository.getAllIncome(userId, onComplete)
    }

    // Update an income
    fun updateIncome(userId: String, incomeId: String, updatedIncome: TransactionModel, onComplete: (Boolean, String?) -> Unit) {
        repository.updateIncome(userId, incomeId, updatedIncome, onComplete)
    }

    // Delete an income
    fun deleteIncome(userId: String, incomeId: String, onComplete: (Boolean, String?) -> Unit) {
        repository.deleteIncome(userId, incomeId, onComplete)
    }

    fun getTotalIncome(userId: String, onComplete: (Double, String?) -> Unit) {
        repository.getTotalIncome(userId, onComplete)
    }

    fun observeTotalIncome(userId: String, onUpdate: (Double) -> Unit) {
        repository.observeTotalIncome(userId, onUpdate)
    }

    fun getIncomeByMonth(userId: String, selectedMonth: Int, onComplete: (MutableList<TransactionModel>, String?) -> Unit) {
        repository.getIncomeByMonth(userId, selectedMonth, onComplete)
    }
}