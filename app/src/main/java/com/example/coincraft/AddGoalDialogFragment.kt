package com.example.coincraft

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class AddGoalDialogFragment : DialogFragment() {



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the custom layout
        val view = LayoutInflater.from(activity).inflate(R.layout.popup_goal_layout, null)

        // Get references to the input fields
        val goalNameInput: EditText = view.findViewById(R.id.goal_name_input)
        val goalTypeSpinner: Spinner = view.findViewById(R.id.goal_type)
        val goalAmountInput: EditText = view.findViewById(R.id.goal_target_input)
        val goalDateButton: Button = view.findViewById(R.id.goal_date)
        val addGoalButton: ImageButton = view.findViewById(R.id.add_goal_button)

        // Set up the goal type dropdown (Spinner)
        val goalTypes = arrayOf("Investment", "Holiday", "Gadget", "Destination", "Other")
        val adapter = ArrayAdapter(requireContext(), R.layout.new_goal_spinner, goalTypes)
        adapter.setDropDownViewResource(R.layout.new_goal_spinner) // Use the same or a different layout for dropdown
        goalTypeSpinner.adapter = adapter

        // Variable to hold the selected deadline date
        var selectedDate = ""

        // Date Picker Dialog for deadline
        goalDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    selectedDate = "$dayOfMonth/${month + 1}/$year"
                    goalDateButton.text = selectedDate
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

            // Validate the inputs
            if (goalName.isEmpty() || goalAmount.isEmpty() || selectedDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Handle saving the goal (pass data back to the activity or save it in a database)
            val goalData = mapOf(
                "name" to goalName,
                "type" to goalType,
                "amount" to goalAmount,
                "deadline" to selectedDate
            )
            // Example: (you can replace with your logic)
            (activity as? GoalSaveListener)?.onGoalSaved(goalData)

            dismiss() // Dismiss the dialog when done
        }

        // Create the dialog and return it
        return android.app.AlertDialog.Builder(requireActivity())
            .setView(view)
            .create()
    }

    // Interface for saving goal data
    interface GoalSaveListener {
        fun onGoalSaved(goalData: Map<String, String>)
    }
}
