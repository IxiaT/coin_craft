package com.example.coincraft

import androidx.lifecycle.ViewModel

class DebtViewModel : ViewModel() {
    private val repository = DebtRepository()

    // Add a debt
    fun addDebt(userId: String, debtCard: DebtCardModel, onComplete: (Boolean, String?) -> Unit) {
        val debt = DebtCardModel( // Create a DebtModel instance from DebtCardModel
            name = debtCard.name,
            amount = debtCard.amount,
            date = debtCard.date,
            state = debtCard.state
        )
        repository.addDebt(userId, debt, onComplete)
    }

    // Get all debts
    fun getAllDebts(userId: String, onComplete: (List<DebtCardModel>, String?) -> Unit) {
        repository.getAllDebts(userId) { debts, error ->
            if (error != null) {
                onComplete(emptyList(), error)
            } else {
                // Convert DebtModel to DebtCardModel
                val debtCardModels = debts.map { debt ->
                    DebtCardModel(
                        profileImage = R.drawable.avatar, // Add your image logic here
                        name = debt.name,
                        date = debt.date,
                        coinImage = R.drawable.coin, // Add your coin image logic here
                        amount = debt.amount,
                        state = debt.state
                    )
                }
                onComplete(debtCardModels, null)
            }
        }
    }

    // Update a debt
    fun updateDebt(userId: String, debtId: String, updatedDebtCard: DebtCardModel, onComplete: (Boolean, String?) -> Unit) {
        val updatedDebt = DebtModel(
            name = updatedDebtCard.name,
            amount = updatedDebtCard.amount,
            date = updatedDebtCard.date,
            state = updatedDebtCard.state
        )
        repository.updateDebt(userId, debtId, updatedDebt, onComplete)
    }

    // Delete a debt
    fun deleteDebt(userId: String, debtId: String, onComplete: (Boolean, String?) -> Unit) {
        repository.deleteDebt(userId, debtId, onComplete)
    }
}