package com.example.coincraft

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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
    private var isToPayActive = false   // Default state

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

        // Set up DatePicker on the EditText field
        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        toReceiveButton.setOnClickListener {
            if (!isToReceiveActive) {
                isToReceiveActive = true
                isToPayActive = false
                updateButtonStates()
            }
        }

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
                val amount = amountText.toDoubleOrNull()
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
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId != null) {
                            // Generate a unique key for the debt
                            val database = FirebaseDatabase.getInstance().reference
                            val debtId = database.child("debts").child(userId).push().key

                            if (debtId != null) {
                                val debtData = mapOf(
                                    "id" to debtId,
                                    "profileImage" to R.drawable.avatar,
                                    "name" to name,
                                    "date" to date,
                                    "coinImage" to R.drawable.coin,
                                    "amount" to amount.toString(),
                                    "state" to state
                                )

                                database.child("debts").child(userId).child(debtId).setValue(debtData)
                                    .addOnSuccessListener {
                                        (activity as? OnDebtAddedListener)?.onDebtAdded(
                                            amount.toString(),
                                            name,
                                            date,
                                            state
                                        )
                                        dismiss()
                                    }
                                    .addOnFailureListener {
                                        amountEditText.error = "Failed to add debt"
                                    }
                            } else {
                                amountEditText.error = "Failed to generate debt ID"
                            }
                        } else {
                            amountEditText.error = "User not logged in"
                        }
                    }
                }
            }
        }

        closeButton.setOnClickListener {
            dismiss()
        }

        builder.setView(dialogView)

        return builder.create()
    }

    private fun updateButtonStates() {
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
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun isPositiveNumber(str: String): Boolean {
        return try {
            val number = str.toDouble()
            number > 0
        } catch (e: NumberFormatException) {
            false
        }
    }
}
