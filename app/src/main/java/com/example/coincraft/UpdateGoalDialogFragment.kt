package com.example.coincraft

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class UpdateGoalDialogFragment : DialogFragment() {

    private lateinit var tvCurrentBalance: TextView
    private lateinit var etAmount: EditText
    private lateinit var etGoalDate: EditText
    private lateinit var btnWithdraw: ImageButton
    private lateinit var btnDeposit: ImageButton
    private lateinit var btnSave: ImageButton
    private lateinit var btnDelete: ImageButton

    private lateinit var goal: FinancialModel
    private var onGoalUpdateListener: OnGoalUpdateListener? = null

    private var tempSavedAmount: Double = 0.0
    private var tempGoalDate: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.popup_update_goal_layout, null)

        tvCurrentBalance = view.findViewById(R.id.tv_current_balance)
        etAmount = view.findViewById(R.id.et_amount)
        etGoalDate = view.findViewById(R.id.goal_date)
        btnWithdraw = view.findViewById(R.id.btn_withdraw)
        btnDeposit = view.findViewById(R.id.btn_deposit)
        btnSave = view.findViewById(R.id.btn_save)
        btnDelete = view.findViewById(R.id.btn_delete)

        tvCurrentBalance.text = "Current Balance: $${goal.saved}"
        etGoalDate.setText(formatDateForDisplay(goal.date))

        tempSavedAmount = goal.saved
        tempGoalDate = goal.date

        // Set up the date picker for goal date
        etGoalDate.inputType = InputType.TYPE_NULL
        etGoalDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    etGoalDate.setText(SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(selectedDate.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        setupButtonListeners()

        return android.app.AlertDialog.Builder(requireActivity())
            .setView(view)
            .create()
    }

    private fun setupButtonListeners() {
        btnWithdraw.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull()
            if (amount == null || amount <= 0) {
                showToast("Please enter a valid amount.")
                return@setOnClickListener
            }

            if (tempSavedAmount >= amount) {
                tempSavedAmount -= amount
                tvCurrentBalance.text = "Current Balance: $${tempSavedAmount}"
            } else {
                showToast("Insufficient funds.")
            }
        }

        btnDeposit.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull()
            if (amount == null || amount <= 0) {
                showToast("Please enter a valid amount.")
                return@setOnClickListener
            }

            tempSavedAmount += amount
            tvCurrentBalance.text = "Current Balance: $${tempSavedAmount}"
        }

        btnSave.setOnClickListener {
            val newDateInput = etGoalDate.text.toString()
            val newDate = parseDateToStorageFormat(newDateInput)
            if (newDate == null) {
                showToast("Invalid date format. Please use MM/dd/yyyy.")
                return@setOnClickListener
            }

            tempGoalDate = newDate
            goal.saved = tempSavedAmount
            goal.date = tempGoalDate

            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            val goalId = goal.id ?: return@setOnClickListener

            val repository = FinancialRepository()
            repository.updateFinancialGoal(userId, goalId, goal) { success, error ->
                if (success) {
                    onGoalUpdateListener?.onGoalUpdated(goal)
                    dismiss()
                } else {
                    showToast("Failed to update goal: ${error ?: "Unknown error"}")
                }
            }
        }

        btnDelete.setOnClickListener {
            val goalId = goal.id ?: return@setOnClickListener
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener

            val repository = FinancialRepository()
            repository.deleteFinancialGoal(userId, goalId) { success, error ->
                if (success) {
                    onGoalUpdateListener?.onGoalDeleted(goal)
                    dismiss()
                } else {
                    showToast("Failed to delete goal: ${error ?: "Unknown error"}")
                }
            }
        }
    }

    private fun parseDateToStorageFormat(dateInput: String): String? {
        return try {
            val inputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate = inputFormat.parse(dateInput) ?: return null
            outputFormat.format(parsedDate)
        } catch (e: Exception) {
            null
        }
    }

    private fun formatDateForDisplay(date: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val parsedDate = inputFormat.parse(date) ?: return date
            outputFormat.format(parsedDate)
        } catch (e: Exception) {
            date
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun setGoal(goal: FinancialModel) {
        this.goal = goal
    }

    fun setOnGoalUpdateListener(listener: OnGoalUpdateListener) {
        this.onGoalUpdateListener = listener
    }

    interface OnGoalUpdateListener {
        fun onGoalUpdated(updatedGoal: FinancialModel)
        fun onGoalDeleted(goal: FinancialModel)
    }
}

