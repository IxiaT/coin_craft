package com.example.coincraft

import androidx.lifecycle.ViewModel

class DebtViewModel : ViewModel() {
    private val repository = DebtRepository()

    // Add a debt
    fun addDebt(userId: String, debt: DebtModel, onComplete: (Boolean, String?) -> Unit) {
        repository.addDebt(userId, debt, onComplete)
    }

    // Get all debts
    fun getAllDebts(userId: String, onComplete: (List<DebtModel>, String?) -> Unit) {
        repository.getAllDebts(userId, onComplete)
    }

    // Update a debt
    fun updateDebt(userId: String, debtId: String, updatedDebt: DebtModel, onComplete: (Boolean, String?) -> Unit) {
        repository.updateDebt(userId, debtId, updatedDebt, onComplete)
    }

    // Delete a debt
    fun deleteDebt(userId: String, debtId: String, onComplete: (Boolean, String?) -> Unit) {
        repository.deleteDebt(userId, debtId, onComplete)
    }
}