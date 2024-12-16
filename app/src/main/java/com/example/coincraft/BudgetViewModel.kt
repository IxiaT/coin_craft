package com.example.coincraft

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BudgetViewModel : ViewModel() {
    private val repository = BudgetRepository()
    val transactionUpdated = MutableLiveData<Boolean>()

    // Add a budget
    fun addBudgetNeeds(userId: String, budget: BudgetModel, onComplete: (Boolean, String?) -> Unit) {
        repository.addBudgetNeeds(userId, budget, onComplete)
    }

    fun addBudgetWants(userId: String, budget: BudgetModel, onComplete: (Boolean, String?) -> Unit) {
        repository.addBudgetWants(userId, budget, onComplete)
    }

    // Retrieve all budgets (Needs and Wants separately)
    fun getBudgetNeeds(
        userId: String,
        onComplete: (List<BudgetModel>, String?) -> Unit
    ) {
        repository.getBudgetsNeeds(userId, onComplete)
    }

    fun getBudgetWants(
        userId: String,
        onComplete: (List<BudgetModel>, String?) -> Unit
    ) {
        repository.getBudgetsWants(userId, onComplete)
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

    fun observeBudge(userId: String, onComplete: (MutableList<BudgetModel>, MutableList<BudgetModel>, String?) -> Unit) {
        repository.observeBudgets(userId, onComplete)
    }
}