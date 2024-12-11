package com.example.coincraft

import DebtViewModel
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.coincraft.R
import java.util.*

class NewDebtDialogFragment : DialogFragment() {

    interface OnDebtAddedListener {
        fun onDebtAdded(amount: String, name: String, date: String, state: String)
    }

    private lateinit var toReceiveButton: ImageButton
    private lateinit var toPayButton: ImageButton
    private lateinit var amountEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var addButton: ImageButton
    private lateinit var closeButton: ImageButton

    private var isToReceiveActive = false // Default state
    private var isToPayActive = false    // Default state

    private lateinit var viewModel: DebtViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        // Inflate the custom dialog layout
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val dialogView: View = inflater.inflate(R.layout.new_debt_fragment, null)

        // Initialize views from the layout
        toReceiveButton = dialogView.findViewById(R.id.toreceive_btn)
        toPayButton = dialogView.findViewById(R.id.topay_btn)
        amountEditText = dialogView.findViewById(R.id.txtamount)
        nameEditText = dialogView.findViewById(R.id.txtname)
        dateEditText = dialogView.findViewById(R.id.goal_date)
        addButton = dialogView.findViewById(R.id.imageButton5)
        closeButton = dialogView.findViewById(R.id.closebtn)

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity()).get(DebtViewModel::class.java)

        // Set up DatePicker on the EditText field
        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        // Handle "To Receive" Button click
        toReceiveButton.setOnClickListener {
            if (!isToReceiveActive) {
                isToReceiveActive = true
                isToPayActive = false
                updateButtonStates()
            }
        }

        // Handle "To Pay" Button click
        toPayButton.setOnClickListener {
            if (!isToPayActive) {
                isToReceiveActive = false
                isToPayActive = true
                updateButtonStates()
            }
        }

        addButton.setOnClickListener {
            val amountText = amountEditText.text.toString()
            val name = nameEditText.text.toString()
            val date = dateEditText.text.toString()

            if (amountText.isBlank()) {
                amountEditText.error = "Amount is required"
            } else if (!isPositiveNumber(amountText)) {
                amountEditText.error = "Amount must be a positive number"
            } else {
                val amount = amountText.toDoubleOrNull() // Convert to Double
                if (amount == null) {
                    amountEditText.error = "Invalid number format"
                    return@setOnClickListener
                }
                if (name.isBlank()) {
                    nameEditText.error = "Name is required"
                } else if (date.isBlank()) {
                    dateEditText.error = "Date is required"
                } else {
                    val state = when {
                        isToPayActive -> "to pay"
                        isToReceiveActive -> "to receive"
                        else -> null
                    }
                    if (state != null) {
                        // Create DebtCardModel
                        val debt = DebtCardModel(
                            id = generateUniqueId(), // You can implement your own logic for generating unique IDs
                            profileImage = R.drawable.avatar, // Example, replace with actual image resource
                            name = name,
                            date = date,
                            coinImage = R.drawable.coin, // Example, replace with actual image resource
                            amount = amount.toString(),
                            state = state
                        )
                        // Pass the individual fields to onDebtAdded
                        (activity as? OnDebtAddedListener)?.onDebtAdded(
                            debt.amount,
                            debt.name,
                            debt.date,
                            debt.state
                        )

                        // Call ViewModel's addDebt method
                        val userId = "user_id_example" // Replace with actual user ID
                        viewModel.addDebt(userId, debt) { success, message ->
                            if (success) {
                                dismiss()
                            } else {
                                // Handle error (e.g., show a message)
                            }
                        }
                    }
                }
            }
        }

        // Handle Close Button click
        closeButton.setOnClickListener {
            dismiss() // Close the dialog
        }

        // Set the custom layout as the dialog view
        builder.setView(dialogView)

        return builder.create()
    }

    private fun updateButtonStates() {
        // Update the drawables for the buttons based on their states
        toReceiveButton.setImageResource(
            if (isToReceiveActive) R.drawable.toreceive_active else R.drawable.toreceive_inactive
        )
        toPayButton.setImageResource(
            if (isToPayActive) R.drawable.topay_active else R.drawable.topay_inactive
        )
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date and set it to the EditText
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun generateUniqueId(): String {
        // Generate a unique ID for the debt, for example, by using Firebase's push method or any other strategy
        return UUID.randomUUID().toString() // You can replace this with your own logic
    }

    private fun isPositiveNumber(str: String): Boolean {
        return try {
            val number = str.toDouble()
            number > 0 // Check if the number is positive
        } catch (e: NumberFormatException) {
            false // Return false if it's not a valid number
        }
    }
}
