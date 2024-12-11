package com.example.coincraft

import androidx.lifecycle.ViewModel

class DebtViewModel : ViewModel() {
    private val repository = DebtRepository()

    // Add a debt
    fun addDebt(userId: String, debtCard: DebtCardModel, onComplete: (Boolean, String?) -> Unit) {
        // Assuming DebtCardModel contains profileImage and coinImage as Int, we use them directly
        val debt = DebtCardModel(
            profileImage = debtCard.profileImage,
            name = debtCard.name,
            date = debtCard.date,
            coinImage = debtCard.coinImage,
            amount = debtCard.amount,
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
                // Map the debts to DebtCardModel
                val debtCardModels = debts.map { debt ->
                    DebtCardModel(
                        profileImage = debt.profileImage,
                        name = debt.name,
                        date = debt.date,
                        coinImage = debt.coinImage,
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
        val updatedDebt = DebtCardModel(
            profileImage = updatedDebtCard.profileImage,
            name = updatedDebtCard.name,
            date = updatedDebtCard.date,
            coinImage = updatedDebtCard.coinImage,
            amount = updatedDebtCard.amount,
            state = updatedDebtCard.state
        )
        repository.updateDebt(userId, debtId, updatedDebt, onComplete)
    }

    // Delete a debt
    fun deleteDebt(userId: String, debtId: String, onComplete: (Boolean, String?) -> Unit) {
        repository.deleteDebt(userId, debtId, onComplete)
    }
}
