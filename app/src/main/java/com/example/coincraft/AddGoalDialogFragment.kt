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

class AddGoalDialogFragment : DialogFragment() {

    private var goalSaveListener: GoalSaveListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the custom layout
        val view = LayoutInflater.from(activity).inflate(R.layout.popup_goal_layout, null)

        // Get references to the input fields
        val goalNameInput: EditText = view.findViewById(R.id.goal_name_input)
        val goalTypeSpinner: Spinner = view.findViewById(R.id.goal_type)
        val goalAmountInput: EditText = view.findViewById(R.id.goal_target_input)
        val goalDateInput: EditText = view.findViewById(R.id.goal_date)
        val addGoalButton: ImageButton = view.findViewById(R.id.add_goal_button)

        // Set up the goal type dropdown (Spinner)
        val goalTypes = arrayOf("Investment", "Holiday", "Gadget", "Destination", "Other")
        val adapter = ArrayAdapter(requireContext(), R.layout.new_goal_spinner, goalTypes)
        adapter.setDropDownViewResource(R.layout.new_goal_spinner)
        goalTypeSpinner.adapter = adapter

        // Configure the date input field
        val calendar = Calendar.getInstance()
        goalDateInput.inputType = InputType.TYPE_NULL

        // Set initial date in goalDateInput using current date (formatted as MM/dd/yyyy)
        goalDateInput.setText(SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(calendar.time))

        // Set up the date picker dialog
        goalDateInput.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    // Format the selected date as MM/dd/yyyy for the dialog
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

            // Validate the inputs
            if (goalName.isEmpty() || goalAmount.isEmpty() || deadline.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Pass the data back to the activity, including the goal date in yyyy-MM-dd format
            val formattedDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(deadline)?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
            } ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            goalSaveListener?.onGoalSaved(
                Goal(
                    name = goalName,
                    icon = R.drawable.ic_goal_icon, // Example icon
                    saved = 0.0, // Initially no savings
                    target = goalAmount.toDouble(),
                    date = formattedDate // Pass the date in yyyy-MM-dd format
                )
            )
            dismiss() // Dismiss the dialog when done
        }

        // Create the dialog and return it
        return android.app.AlertDialog.Builder(requireActivity())
            .setView(view)
            .create()
    }

    fun setGoalSaveListener(listener: GoalSaveListener) {
        goalSaveListener = listener
    }

    // Interface for saving goal data
    interface GoalSaveListener {
        fun onGoalSaved(newGoal: Goal)
    }
}
