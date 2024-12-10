package com.example.coincraft

import androidx.lifecycle.ViewModel

class BudgetViewModel : ViewModel() {
    private val repository = BudgetRepository()

    // Add a budget
    fun addBudget(userId: String, budget: BudgetModel, onComplete: (Boolean, String?) -> Unit) {
        repository.addBudget(userId, budget, onComplete)
    }

    // Retrieve all budgets (Needs and Wants separately)
    fun getBudgets(
        userId: String,
        onComplete: (List<BudgetModel>, List<BudgetModel>, String?) -> Unit
    ) {
        repository.getBudgets(userId, onComplete)
    }

    // Compute total limit for a specific budget type (Needs or Wants)
    fun computeTotalLimit(
        userId: String,
        budgetType: String,
        onComplete: (Double, String?) -> Unit
    ) {
        repository.computeTotalLimit(userId, budgetType, onComplete)
    }

    // Update a budget
    fun updateBudget(
        userId: String,
        budgetType: String,
        budgetId: String,
        updatedBudget: BudgetModel,
        onComplete: (Boolean, String?) -> Unit
    ) {
        repository.updateBudget(userId, budgetType, budgetId, updatedBudget, onComplete)
    }

    // Delete a budget
    fun deleteBudget(
        userId: String,
        budgetType: String,
        budgetId: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        repository.deleteBudget(userId, budgetType, budgetId, onComplete)
    }
}