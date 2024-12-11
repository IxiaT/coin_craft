import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coincraft.DebtCardModel
import com.example.coincraft.DebtRepository

class DebtViewModel : ViewModel() {
    private val repository = DebtRepository()

    private val _toReceiveDebts = MutableLiveData<List<DebtCardModel>>()
    val toReceiveDebts: LiveData<List<DebtCardModel>> get() = _toReceiveDebts

    private val _toPayDebts = MutableLiveData<List<DebtCardModel>>()
    val toPayDebts: LiveData<List<DebtCardModel>> get() = _toPayDebts

    // Add a debt
    fun addDebt(userId: String, debt: DebtCardModel, onComplete: (Boolean, String?) -> Unit) {
        repository.addDebt(userId, debt, onComplete)
    }

    // Get all debts
    fun getAllDebts(userId: String) {
        repository.getAllDebts(userId) { debts, error ->
            if (error == null) {
                // Separate debts into "to receive" and "to pay" lists
                val toReceive = debts.filter { it.state == "to receive" }
                val toPay = debts.filter { it.state == "to pay" }
                _toReceiveDebts.postValue(toReceive)
                _toPayDebts.postValue(toPay)
            } else {
                // Handle error
            }
        }
    }

    // Update a debt
    fun updateDebt(userId: String, debtId: String, updatedDebt: DebtCardModel, onComplete: (Boolean, String?) -> Unit) {
        repository.updateDebt(userId, debtId, updatedDebt, onComplete)
    }

    // Delete a debt
    fun deleteDebt(userId: String, debtId: String, onComplete: (Boolean, String?) -> Unit) {
        repository.deleteDebt(userId, debtId, onComplete)
    }
}
