package com.example.coincraft

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import kotlin.properties.Delegates

class Transaction : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var monthSpentSpinner: Spinner
    private lateinit var monthEarnedSpinner: Spinner
    private lateinit var spentRV: RecyclerView
    private lateinit var earnedRV: RecyclerView
    private lateinit var totalSpentAmount: TextView
    private lateinit var totalEarnedAmount: TextView

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var incomeViewModel: IncomeViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId: String
    private var spentSelectedMonth: Int = 0
    private var earnedSelectedMonth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaction)

        sharedPreferences = getSharedPreferences("my_pref", MODE_PRIVATE)
        userId = sharedPreferences.getString("User", null).toString()

        backButton = findViewById(R.id.back_button)
        totalEarnedAmount = findViewById(R.id.earned_total)
        totalSpentAmount = findViewById(R.id.spent_total)
        monthSpentSpinner = findViewById(R.id.spent_month_spinner)
        monthEarnedSpinner = findViewById(R.id.earned_month_spinner)

        //Initialize ViewModel
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]
        incomeViewModel = ViewModelProvider(this)[IncomeViewModel::class.java]

        //Spinner initialize
        loadSpinnerArray(R.array.months_array)

        monthSpentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Convert selected text to month index
                spentSelectedMonth = position
                fetchSpent(spentSelectedMonth)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        monthEarnedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Convert selected text to month index
                earnedSelectedMonth = position
                fetchEarned(earnedSelectedMonth)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        backButton.setOnClickListener {
            intent = Intent(this@Transaction, Home::class.java)
            startActivity(intent)
            finish()
        }

        expenseViewModel.transactionUpdated.observe(this) { updated ->
            if (updated) {
                fetchSpent(spentSelectedMonth)
                expenseViewModel.transactionUpdated.postValue(false) // Reset state
            }
        }

        incomeViewModel.transactionUpdated.observe(this) { updated ->
            if (updated) {
                fetchEarned(earnedSelectedMonth) // Reload your RecyclerView or data
                incomeViewModel.transactionUpdated.postValue(false) // Reset state
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadSpinnerArray(arrayResId: Int) {
        // Get the appropriate array (income or expense)
        val months = resources.getStringArray(arrayResId)

        // Set up the spinner adapter with the new array
        val adapter = ArrayAdapter(
            this,
            R.layout.custom_spinner_dropdown_item,
            months
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        monthSpentSpinner.adapter = adapter
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        monthEarnedSpinner.adapter = adapter

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        monthSpentSpinner.setSelection(currentMonth)
        monthEarnedSpinner.setSelection(currentMonth)
    }

    private fun fetchSpent(selectedMonth: Int) {
        expenseViewModel.getExpensesByMonth(userId, selectedMonth) { expenses, expenseError ->
            if (expenseError != null) {
                Log.e("Transaction", "Error fetching expenses: $expenseError")
                return@getExpensesByMonth
            }

            updateTotalSpent(expenses)
            spentRV = findViewById(R.id.spent_rv)!!
            val spentAdapter = TransactionAdapter(expenses, supportFragmentManager, "spent")
            spentRV.adapter = spentAdapter
            spentRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun fetchEarned(selectedMonth: Int) {
        incomeViewModel.getIncomeByMonth(userId, selectedMonth) { income, error ->
            if (error == null) {
                // Successfully retrieved goals
                Log.d("Income", "Income: $income")
            } else {
                // Handle error
                Log.e("Income", "Error fetching income: $error")
                Toast.makeText(this, "Error fetching income", Toast.LENGTH_SHORT).show()
            }
            updateTotalEarned(income)
            // Update the UI, e.g., RecyclerView
            earnedRV = findViewById(R.id.earned_rv)!!
            val earnedAdapter = TransactionAdapter(income, supportFragmentManager, "earned")
            earnedRV.adapter = earnedAdapter
            earnedRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        }
    }

    private fun updateTotalEarned(income: List<TransactionModel>) {
        val totalEarned = income.sumOf { it.amount }
        totalEarnedAmount.text = if (income.isEmpty()) "0.0" else totalEarned.toString()
    }

    private fun updateTotalSpent(expense: List<TransactionModel>) {
        val totalEarned = expense.sumOf { it.amount }
        totalSpentAmount.text = if (expense.isEmpty()) "0.0" else totalEarned.toString()
    }
}