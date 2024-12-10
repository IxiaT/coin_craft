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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddExpenseFragment: DialogFragment() {
    private lateinit var amountEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var dateEditText: EditText
    private lateinit var noteEditText: EditText
    private lateinit var addButton: ImageButton
    private lateinit var closeButton: ImageButton
    private lateinit var incomeButton: ImageButton
    private lateinit var expenseButton: ImageButton
    private lateinit var sf: SharedPreferences
    private lateinit var selectedButton: String

    private val calendar = Calendar.getInstance()

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var incomeViewModel: IncomeViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.add_expense_layout, container, false)

        // Initialize SharedPreferenceManager
        sf = requireContext().getSharedPreferences("my_pref", MODE_PRIVATE)
        val userId = sf.getString("User", null)

        if (userId != null) {

            amountEditText = view.findViewById(R.id.txtamount)
            categorySpinner = view.findViewById(R.id.category_spinner)
            dateEditText = view.findViewById(R.id.transaction_date)
            noteEditText = view.findViewById(R.id.note)
            addButton = view.findViewById(R.id.add_button)
            closeButton = view.findViewById(R.id.closebtn)
            incomeButton = view.findViewById(R.id.income_btn)
            expenseButton = view.findViewById(R.id.expense_btn)

            setActiveButton("MONEY_IN")

            expenseViewModel = ViewModelProvider(requireActivity())[ExpenseViewModel::class.java]
            incomeViewModel = ViewModelProvider(requireActivity())[IncomeViewModel::class.java]
            dateEditText.inputType = 0

            // Set up DatePickerDialog
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

            // Set up TimePickerDialog
//        timeEditText.setOnClickListener {
//            TimePickerDialog(
//                requireContext(),
//                { _, hour, minute ->
//                    calendar.set(Calendar.HOUR_OF_DAY, hour)
//                    calendar.set(Calendar.MINUTE, minute)
//                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
//                    timeEditText.setText(timeFormat.format(calendar.time))
//                },
//                calendar.get(Calendar.HOUR_OF_DAY),
//                calendar.get(Calendar.MINUTE),
//                true
//            ).show()
//        }
            incomeButton.setOnClickListener {
                setActiveButton("MONEY_IN")
            }

            expenseButton.setOnClickListener {
                setActiveButton("MONEY_OUT")
            }

            // Save Button
            addButton.setOnClickListener {
                if(amountEditText.text.isEmpty() || dateEditText.text.isEmpty()){
                    amountEditText.error = "Input amount"
                    dateEditText.error = "Input Date yyyy-MM-dd"
                } else {
                    saveTransaction(userId)
                }

            }

            // Cancel Button
            closeButton.setOnClickListener {
                dismiss()
            }

        } else {
            Toast.makeText(requireContext(), "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return view
    }

    private fun setActiveButton(button: String) {
        selectedButton = button

        when (button) {
            "MONEY_IN" -> {
                incomeButton.setImageResource(R.drawable.money_in_active)
                expenseButton.setImageResource(R.drawable.money_out)
                loadSpinnerArray(R.array.income_categories)
            }
            "MONEY_OUT" -> {
                incomeButton.setImageResource(R.drawable.money_in)
                expenseButton.setImageResource(R.drawable.money_out_active)
                loadSpinnerArray(R.array.expense_categories)
            }
        }
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

    private fun saveTransaction(userId: String) {
        val amountText = amountEditText.text.toString()
        val category = categorySpinner.selectedItem.toString()
        val date = dateEditText.text.toString()
        val note = noteEditText.text.toString()

        if (amountText.isBlank() || category.isBlank() || date.isBlank()) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        when (selectedButton) {
            "MONEY_IN" -> {
                val amount = amountText.toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                    return
                }

                val income = IncomeModel(
                    amount = amount,
                    category = category,
                    incomeNote = note,
                    date = date
                )

                incomeViewModel.addIncome(userId, income) { success, error ->
                    if (success) {
                        Toast.makeText(requireContext(), "Income added successfully", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Failed to add income: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            "MONEY_OUT" -> {
                val amount = amountText.toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                    return
                }

                val expense = ExpenseModel(
                    amount = amount,
                    category = category,
                    expenseNote = note,
                    date = date
                )

                expenseViewModel.addExpense(userId, expense) { success, error ->
                    if (success) {
                        Toast.makeText(requireContext(), "Expense added successfully", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Failed to add expense: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}