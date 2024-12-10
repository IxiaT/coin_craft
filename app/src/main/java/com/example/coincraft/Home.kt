package com.example.coincraft

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var financialViewModel: FinancialViewModel
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var incomeViewModel: IncomeViewModel
    private lateinit var userId: String

    private lateinit var hpBar: ProgressBar
    private lateinit var xpBar: ProgressBar
    private lateinit var earnedBar: ProgressBar
    private lateinit var spentBar: ProgressBar
    private lateinit var balance: TextView
    private lateinit var earnedAmount: TextView
    private lateinit var spentAmount: TextView
    private lateinit var emptyTextView: TextView
    private lateinit var emptyImage: ImageView
    private lateinit var incomeCard: CardView
    private lateinit var spentCard: CardView
    private lateinit var finacialCard: CardView
    private lateinit var goalsRV: RecyclerView
    private lateinit var plusBtn: FloatingActionButton
    private lateinit var bottomNav: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("my_pref", MODE_PRIVATE)
        userId = sharedPreferences.getString("User", null).toString()
        val hp = sharedPreferences.getString("hp", null)?.toInt()
        val xp = sharedPreferences.getString("xp", null)?.toInt()

        hpBar = findViewById(R.id.hp_bar)
        xpBar = findViewById(R.id.xp_bar)
        earnedBar = findViewById(R.id.earned_bar)
        spentBar = findViewById(R.id.spent_bar)
        balance = findViewById(R.id.balance_amount)
        earnedAmount = findViewById(R.id.earned_amount)
        spentAmount = findViewById(R.id.spent_amount)
        plusBtn = findViewById(R.id.plus_button)
        bottomNav = findViewById(R.id.botnav)
        finacialCard = findViewById(R.id.finacial_card)
        emptyImage = findViewById(R.id.empty_list_icon)
        emptyTextView = findViewById(R.id.empty_list_text)

        //Initialize ViewModels
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]
        financialViewModel = ViewModelProvider(this)[FinancialViewModel::class.java]
        incomeViewModel = ViewModelProvider(this)[IncomeViewModel::class.java]
       
        //Recycler view
        fetchFinancialGoals()

        //Total Spent
        fetchExpenses()

        plusBtn.setOnClickListener {
            val dialog = AddExpenseFragment()
            dialog.show(supportFragmentManager, "AddExpenseDialog")
        }

//        spentBtn.setOnClickListener {
//            val expense = Intent(this@Home, ExpenseTracker::class.java)
//            startActivity(expense)
//            finish()
//        }

        if (hp != null) {
            hpBar.setProgress(hp, true)
        }

        if (xp != null) {
            xpBar.setProgress(xp, true)
        }

        finacialCard.setOnClickListener{
            val financialg = Intent(this@Home, FinancialGoalsActivity::class.java)
            startActivity(financialg)
        }
        stngsBox.setOnClickListener {
            showLogoutDialog()
        }


        // Change intent to your respective activities
        bottomNav.setOnNavigationItemSelectedListener { item ->
            val itemId = item.itemId
            var intent: Intent? = null

            when (itemId) {
                R.id.navigation_home -> {
                    intent = Intent(this@Home, Home::class.java)
                    startActivity(intent)
                }
                R.id.navigation_discover -> {
//                    intent = Intent(this@Home, Transaction::class.java)
//                    startActivity(intent)
                }
                R.id.navigation_likes -> {
//                    intent = Intent(this@Home, Budgeting::class.java)
//                    startActivity(intent)
                }
                R.id.navigation_account -> {
//                    val intent = Intent(this@Home, Debt::class.java)
//                    startActivity(intent)
                }
            }

            val selectedItem = bottomNav.findViewById<View>(item.itemId)
            selectedItem?.animate()?.apply {
                scaleX(1.2f)
                scaleY(1.2f)
                duration = 100
                withEndAction {
                    selectedItem.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
            }?.start()

            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logoutUser()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logoutUser() {
        sharedPreferences.edit().clear().apply() // Clear all stored preferences
        val intent = Intent(this@Home, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close the Home activity
    }


    private fun fetchFinancialGoals() {
        financialViewModel.getFinancialGoals(userId) { financialGoals, error ->
            if (error == null) {
                // Successfully retrieved goals
                Log.d("FinancialGoalsFragment", "Goals: $financialGoals")

                if(financialGoals.isEmpty()){
                    emptyImage.visibility = View.VISIBLE
                    emptyTextView.visibility = View.VISIBLE
                } else {
                    emptyImage.visibility = View.GONE
                    emptyTextView.visibility = View.GONE
                }

                // Update the UI, e.g., RecyclerView
                goalsRV = findViewById(R.id.horizontal_finacial_goal_rv)!!
                val adapter = HomeGoalsAdapter(financialGoals)
                goalsRV.adapter = adapter
                goalsRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            } else {
                // Handle error
                Log.e("FinancialGoalsFragment", "Error fetching goals: $error")
                Toast.makeText(this, "Error fetching financial goals", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchExpenses() {
        expenseViewModel.getTotalExpenses(userId) { totalSpent, error ->
            if (error == null) {
                // Successfully retrieved expenses
                Log.d("ExpensesActivity", "Expenses: $totalSpent")
                spentAmount.text = totalSpent.toString()
            } else {
                // Handle error
                Log.e("ExpensesActivity", "Error fetching expenses: $error")
                Toast.makeText(this, "Error fetching expenses", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculatePercentage() {
        expenseViewModel.getTotalExpenses(userId) { totalExpenses, expenseError ->
            if (expenseError != null) {
                Log.e("ExpensesActivity", "Error fetching expenses: $expenseError")
                return@getTotalExpenses
            }

            incomeViewModel.getTotalIncome(userId) { totalIncome, incomeError ->
                if (incomeError != null) {
                    Log.e("ExpensesActivity", "Error fetching income: $incomeError")
                    return@getTotalIncome
                }
                spentAmount.text = totalExpenses.toString()
                earnedAmount.text = totalIncome.toString()

                // Calculate percentage
                val percentage = if (totalIncome > 0) {
                    val diff = totalIncome - totalExpenses
                    (diff / totalIncome * 100).coerceAtLeast(0.0) // Ensure percentage is not negative
                } else {
                    0.0
                }

                Log.d("ExpensesActivity", "Percentage: $percentage%")
                spentBar.setProgress(percentage.toInt(), true)
                earnedBar.setProgress(percentage.toInt(), true)
            }
        }
    }

}