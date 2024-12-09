package com.example.coincraft

import androidx.lifecycle.ViewModel

class FinancialViewModel : ViewModel() {
    private val repository = FinancialRepository()

    // Add a financial goal
    fun addFinancialGoal(userId: String, financialGoal: FinancialModel, onComplete: (Boolean, String?) -> Unit) {
        repository.addFinancialGoal(userId, financialGoal, onComplete)
    }

    // Get all financial goals
    fun getFinancialGoals(userId: String, onComplete: (List<FinancialModel>, String?) -> Unit) {
        repository.getFinancialGoals(userId, onComplete)
    }

    // Update a financial goal
    fun updateFinancialGoal(userId: String, goalId: String, updatedGoal: FinancialModel, onComplete: (Boolean, String?) -> Unit) {
        repository.updateFinancialGoal(userId, goalId, updatedGoal, onComplete)
    }

    // Delete a financial goal
    fun deleteFinancialGoal(userId: String, goalId: String, onComplete: (Boolean, String?) -> Unit) {
        repository.deleteFinancialGoal(userId, goalId, onComplete)
    }
}