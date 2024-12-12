package com.example.coincraft

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class AddGoalDialogFragment : DialogFragment() {

    private var goalSaveListener: GoalSaveListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.popup_goal_layout, null)

        val goalNameInput: EditText = view.findViewById(R.id.goal_name_input)
        val goalTypeSpinner: Spinner = view.findViewById(R.id.goal_type)
        val goalAmountInput: EditText = view.findViewById(R.id.goal_target_input)
        val goalDateInput: EditText = view.findViewById(R.id.goal_date)
        val addGoalButton: ImageButton = view.findViewById(R.id.add_goal_button)

        val goalTypes = arrayOf("Investment", "Holiday", "Gadget", "Destination", "Shoes", "Clothes", "Other")
        val adapter = ArrayAdapter(requireContext(), R.layout.new_goal_spinner, goalTypes)
        goalTypeSpinner.adapter = adapter

        // Set up the date input field
        val calendar = Calendar.getInstance()
        goalDateInput.inputType = InputType.TYPE_NULL
        goalDateInput.setText(SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(calendar.time))

        // Set up the date picker dialog
        goalDateInput.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    goalDateInput.setText(SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(selectedDate.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Setup the add goal button click listener
        addGoalButton.setOnClickListener {
            val goalName = goalNameInput.text.toString()
            val goalType = goalTypeSpinner.selectedItem.toString()
            val goalAmount = goalAmountInput.text.toString()
            val deadline = goalDateInput.text.toString()

            if (goalName.isEmpty() || goalAmount.isEmpty() || deadline.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Parse the date as yyyy-MM-dd format
            val formattedDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(deadline)?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
            } ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val newGoal = FinancialModel(
                name = goalName,
                saved = 0.0,
                target = goalAmount.toDouble(),
                date = formattedDate,
                type = goalType
            )

            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            val repository = FinancialRepository()

            repository.addFinancialGoal(userId, newGoal) { success, error ->
                if (success) {
                    goalSaveListener?.onGoalSaved(newGoal)
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Failed to save goal: ${error ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return android.app.AlertDialog.Builder(requireActivity())
            .setView(view)
            .create()
    }

    fun setGoalSaveListener(listener: GoalSaveListener) {
        goalSaveListener = listener
    }

    interface GoalSaveListener {
        fun onGoalSaved(newGoal: FinancialModel)
    }
}