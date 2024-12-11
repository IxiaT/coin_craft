package com.example.coincraft

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class EditDebtDialogFragment : DialogFragment() {

    interface OnDebtEditedListener {
        fun onDebtEdited(newAmount: String, newDate: String, position: Int, adapterType: String)
    }

    private var listener: OnDebtEditedListener? = null
    private var adapterType: String? = null
    private var position: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDebtEditedListener) {
            listener = context
        }
    }

    private lateinit var closeButton: ImageButton
    private lateinit var debtOwnerTextView: TextView
    private lateinit var debtStatusTextView: TextView
    private lateinit var currentAmountTextView: TextView
    private lateinit var debtAmountEditText: EditText
    private lateinit var debtDateEditText: EditText
    private lateinit var minusButton: ImageButton
    private lateinit var plusButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        // Inflate the custom dialog layout
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val dialogView: View = inflater.inflate(R.layout.edit_debt_fragment, null)

        // Initialize views from the layout
        closeButton = dialogView.findViewById(R.id.closebtn)
        debtOwnerTextView = dialogView.findViewById(R.id.txt_debtowner)
        debtStatusTextView = dialogView.findViewById(R.id.txt_debtstatus)
        currentAmountTextView = dialogView.findViewById(R.id.txt_currentamount)
        debtAmountEditText = dialogView.findViewById(R.id.edittxt_debtamount)
        debtDateEditText = dialogView.findViewById(R.id.edittxt_changedate)
        minusButton = dialogView.findViewById(R.id.imgbtn_minus)
        plusButton = dialogView.findViewById(R.id.imagebtn_plus)

        // Retrieve arguments passed to the dialog (if any)
        val debtOwner = arguments?.getString("debtOwner") ?: "Unknown"
        val debtStatus = arguments?.getString("debtStatus") ?: "Unknown"
        var currentAmount = arguments?.getString("currentAmount") ?: "0.00"
        adapterType = arguments?.getString("adapterType")
        position = arguments?.getInt("debtPosition", -1) ?: -1

        // Set initial values for the text fields
        debtOwnerTextView.text = debtOwner
        debtStatusTextView.text = debtStatus
        currentAmountTextView.text = currentAmount

        updateDebtStatusColors(debtStatus)

        // Close button listener
        closeButton.setOnClickListener {
            dismiss()
        }

        // Parse the initial current amount
        var currentDebtAmount = currentAmount.toDoubleOrNull() ?: 0.0

        // Helper function to handle changes and dismiss the dialog
        fun handleAmountChange(newAmount: Double) {
            currentDebtAmount = newAmount
            currentAmountTextView.text = String.format("%.2f", currentDebtAmount)

            listener?.onDebtEdited(
                newAmount = String.format("%.2f", currentDebtAmount),
                newDate = debtDateEditText.text.toString(),
                position = position,
                adapterType = adapterType ?: "Unknown"
            )
            dismiss()
        }

        // Plus button listener
        plusButton.setOnClickListener {
            val addedAmount = debtAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
            if (addedAmount > 0) {
                val newDebtAmount = currentDebtAmount + addedAmount
                handleAmountChange(newDebtAmount)
            }
        }

        // Minus button listener
        minusButton.setOnClickListener {
            val deductedAmount = debtAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
            if (deductedAmount > 0 && deductedAmount <= currentDebtAmount) {
                val newDebtAmount = currentDebtAmount - deductedAmount
                handleAmountChange(newDebtAmount)
            }
        }

        debtDateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        builder.setView(dialogView)

        return builder.create()
    }

    private fun updateDebtStatusColors(debtStatus: String) {
        if (debtStatus.equals("To Pay", ignoreCase = true)) {
            debtStatusTextView.setTextColor(Color.parseColor("#F24E1E"))
            currentAmountTextView.setTextColor(Color.parseColor("#F24E1E"))
        } else if (debtStatus.equals("To Receive", ignoreCase = true)) {
            debtStatusTextView.setTextColor(Color.parseColor("#0ACF83"))
            currentAmountTextView.setTextColor(Color.parseColor("#0ACF83"))
        }
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
                debtDateEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}
