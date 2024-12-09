package com.example.coincraft

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.*
import androidx.fragment.app.DialogFragment
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

    private lateinit var goal: Goal
    private var onGoalUpdateListener: OnGoalUpdateListener? = null

    // Temporary variables to store updates
    private var tempSavedAmount: Double = 0.0
    private var tempGoalDate: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the custom layout
        val view = LayoutInflater.from(activity).inflate(R.layout.popup_update_goal_layout, null)

        // Get references to the input fields
        tvCurrentBalance = view.findViewById(R.id.tv_current_balance)
        etAmount = view.findViewById(R.id.et_amount)
        etGoalDate = view.findViewById(R.id.goal_date)
        btnWithdraw = view.findViewById(R.id.btn_withdraw)
        btnDeposit = view.findViewById(R.id.btn_deposit)
        btnSave = view.findViewById(R.id.btn_save)
        btnDelete = view.findViewById(R.id.btn_delete)

        // Populate initial goal data
        tvCurrentBalance.text = "Current Balance: $${goal.saved}"
        etGoalDate.setText(goal.getFormattedDateForDialog())  // Set the goal date in the format MM/dd/yyyy

        // Initialize temp variables
        tempSavedAmount = goal.saved
        tempGoalDate = goal.date

        // Configure the date input field
        etGoalDate.inputType = InputType.TYPE_NULL

        // Set up the date picker dialog for the goal date
        etGoalDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    // Format the selected date as MM/dd/yyyy for the dialog
                    etGoalDate.setText(SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(selectedDate.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Setup button listeners
        setupButtonListeners()

        // Create the dialog and return it
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

            // Apply temp changes to the goal
            goal.saved = tempSavedAmount
            goal.date = tempGoalDate

            // Notify listener of updates
            onGoalUpdateListener?.onGoalUpdated(goal)
            dismiss()
        }

        btnDelete.setOnClickListener {
            onGoalUpdateListener?.onGoalDeleted(goal)  // Notify that the goal was deleted
            dismiss()
        }
    }

    // Parse user input date (MM/dd/yyyy) to storage format (yyyy-MM-dd)
    private fun parseDateToStorageFormat(dateInput: String): String? {
        return try {
            val inputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate: Date = inputFormat.parse(dateInput) ?: return null
            outputFormat.format(parsedDate)
        } catch (e: Exception) {
            null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // Setter methods for goal and listener
    fun setGoal(goal: Goal) {
        this.goal = goal
    }

    fun setOnGoalUpdateListener(listener: OnGoalUpdateListener) {
        this.onGoalUpdateListener = listener
    }

    // Interface to notify the parent activity of updates or deletions
    interface OnGoalUpdateListener {
        fun onGoalUpdated(updatedGoal: Goal)
        fun onGoalDeleted(goal: Goal)
    }
}
