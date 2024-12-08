package com.example.coincraft

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.example.coincraft.R

class NewDebtDialogFragment : DialogFragment() {

    interface OnDebtAddedListener {
        fun onDebtAdded(amount: String, name: String, date: String)
    }

    private lateinit var amountEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var addButton: ImageButton
    private lateinit var closeButton: ImageButton

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        // Inflate the custom dialog layout
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val dialogView: View = inflater.inflate(R.layout.new_debt_fragment, null)

        // Initialize views from the layout
        amountEditText = dialogView.findViewById(R.id.txtamount)
        nameEditText = dialogView.findViewById(R.id.txtname)
        dateEditText = dialogView.findViewById(R.id.goal_date)
        addButton = dialogView.findViewById(R.id.imageButton5)
        closeButton = dialogView.findViewById(R.id.closebtn)

        // Handle Add Button Click
        addButton.setOnClickListener {
            val amount = amountEditText.text.toString()
            val name = nameEditText.text.toString()
            val date = dateEditText.text.toString()

            if (amount.isNotBlank() && name.isNotBlank() && date.isNotBlank()) {
                // Send the data back to the listener
                (activity as? OnDebtAddedListener)?.onDebtAdded(amount, name, date)
                dismiss() // Close the dialog
            } else {
                // Display an error message (optional)
                amountEditText.error = "Amount is required"
                nameEditText.error = "Name is required"
                dateEditText.error = "Date is required"
            }
        }

        // Handle Close Button Click
        closeButton.setOnClickListener {
            dismiss() // Close the dialog
        }

        // Set the custom layout as the dialog view
        builder.setView(dialogView)

        return builder.create()
    }
}