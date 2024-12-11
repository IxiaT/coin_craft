package com.example.coincraft

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateTransaction: DialogFragment() {

    private lateinit var amountEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var dateEditText: EditText
    private lateinit var noteEditText: EditText
    private lateinit var updateButton: ImageButton
    private lateinit var closeButton: ImageButton
    private lateinit var deleteButton: ImageButton

    private val calendar = Calendar.getInstance()
    private lateinit var item: TransactionModel
    private lateinit var type: String
    private lateinit var sf: SharedPreferences
    private lateinit var userId: String

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var incomeViewModel: IncomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = it.getSerializable("item") as TransactionModel
            type = it.getString("type") as String
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.update_transaction_layout, container, false)
        sf = requireContext().getSharedPreferences("my_pref", MODE_PRIVATE)
        userId = sf.getString("User", null).toString()

        amountEditText = view.findViewById(R.id.txtamount)
        categorySpinner = view.findViewById(R.id.category_spinner)
        dateEditText = view.findViewById(R.id.transaction_date)
        noteEditText = view.findViewById(R.id.note)
        updateButton = view.findViewById(R.id.update_button)
        closeButton = view.findViewById(R.id.closebtn)
        deleteButton = view.findViewById(R.id.delete_button)

        expenseViewModel = ViewModelProvider(requireActivity())[ExpenseViewModel::class.java]
        incomeViewModel = ViewModelProvider(requireActivity())[IncomeViewModel::class.java]
        dateEditText.inputType = 0

        if(type == "spent") {
            loadSpinnerArray(R.array.expense_categories)
            val selectedCategory = item.category
            val categories = resources.getStringArray(R.array.expense_categories).toList()
            val index = categories.indexOf(selectedCategory)
            if (index >= 0) {
                categorySpinner.setSelection(index)
            }
        }
        if(type == "earned") {
            loadSpinnerArray(R.array.income_categories)
            val selectedCategory = item.category
            val categories = resources.getStringArray(R.array.income_categories).toList()
            val index = categories.indexOf(selectedCategory)
            if (index >= 0) {
                categorySpinner.setSelection(index)
            }
        }


        amountEditText.setText(item.amount.toString())
        noteEditText.setText(item.note)
        dateEditText.setText(item.date)


        dateEditText.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    dateEditText.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        updateButton.setOnClickListener {
            updateTransaction()
        }

        deleteButton.setOnClickListener {
            deleteTransaction()
        }

        closeButton.setOnClickListener {
            dismiss()
        }
        return view
    }

    private fun loadSpinnerArray(arrayResId: Int) {
        // Get the appropriate array (income or expense)
        val categories = resources.getStringArray(arrayResId)

        // Set up the spinner adapter with the new array
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_dropdown_item,
            categories
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

    private fun updateTransaction() {
        val itemId = item.id
        val amountText = amountEditText.text.toString()
        val category = categorySpinner.selectedItem.toString()
        val date = dateEditText.text.toString()
        val note = noteEditText.text.toString()

        if (amountText.isBlank() || date.isBlank()) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve the transaction being updated
        if (item == null) {
            Toast.makeText(requireContext(), "Failed to retrieve transaction data", Toast.LENGTH_SHORT).show()
            return
        }

        // Create an updated transaction object
        val updatedTransaction = TransactionModel(
            id = itemId,
            amount = amount,
            category = category,
            note = note,
            date = date
        )

        when (type) {
            "earned" -> {
                incomeViewModel.updateIncome(userId, item.id, updatedTransaction) { success, error ->
                    if (success) {
                        Toast.makeText(requireContext(), "Income updated successfully", Toast.LENGTH_SHORT).show()
                        dismiss()
                        incomeViewModel.transactionUpdated.postValue(true)
                    } else {
                        Toast.makeText(requireContext(), "Failed to update income: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            "spent" -> {
                expenseViewModel.updateExpense(userId, item.id, updatedTransaction) { success, error ->
                    if (success) {
                        Toast.makeText(requireContext(), "Expense updated successfully", Toast.LENGTH_SHORT).show()
                        dismiss()
                        expenseViewModel.transactionUpdated.postValue(true)
                    } else {
                        Toast.makeText(requireContext(), "Failed to update expense: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun deleteTransaction() {
        when (type) {
            "earned" -> {
                incomeViewModel.deleteIncome(userId, item.id) { success, error ->
                    if (success) {
                        Toast.makeText(
                            requireContext(),
                            "Expense updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        incomeViewModel.transactionUpdated.postValue(true)
                        dismiss()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to update expense: $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            "spent" -> {
                expenseViewModel.deleteExpense(userId, item.id) { success, error ->
                    if (success) {
                        Toast.makeText(
                            requireContext(),
                            "Expense updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        expenseViewModel.transactionUpdated.postValue(true)
                        dismiss()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to update expense: $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

}