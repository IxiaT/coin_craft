package com.example.coincraft

import androidx.lifecycle.ViewModel

class FinancialViewModel : ViewModel() {
    private val repository = FinancialRepository()

    // Add a financial goal
    fun addFinancialGoal(userId: String, financialGoal: FinancialModel, onComplete: (Boolean, String?) -> Unit) {
        // Call the repository method to add the financial goal
        repository.addFinancialGoal(userId, financialGoal, onComplete)
    }

    // Get all financial goals
    fun getFinancialGoals(userId: String, onComplete: (List<FinancialModel>, String?) -> Unit) {
        // Call the repository method to fetch financial goals
        repository.getFinancialGoals(userId, onComplete)
    }

    // Update a financial goal
    fun updateFinancialGoal(userId: String, goalId: String, updatedGoal: FinancialModel, onComplete: (Boolean, String?) -> Unit) {
        // Call the repository method to update the financial goal
        repository.updateFinancialGoal(userId, goalId, updatedGoal, onComplete)
    }

    // Delete a financial goal
    fun deleteFinancialGoal(userId: String, goalId: String, onComplete: (Boolean, String?) -> Unit) {
        // Call the repository method to delete the financial goal
        repository.deleteFinancialGoal(userId, goalId, onComplete)
    }
}
