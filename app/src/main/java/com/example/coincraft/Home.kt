package com.example.coincraft

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var financialViewModel: FinancialViewModel
    private lateinit var userId: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("my_pref", MODE_PRIVATE)
        userId = sharedPreferences.getString("User", null).toString()
        val hp = sharedPreferences.getString("hp", null)?.toInt()
        val xp = sharedPreferences.getString("xp", null)?.toInt()

        val hpBar = findViewById<ProgressBar>(R.id.hp_bar)
        val xpBar = findViewById<ProgressBar>(R.id.xp_bar)
        val earnedBar = findViewById<ProgressBar>(R.id.earned_bar)
        val spentBar = findViewById<ProgressBar>(R.id.spent_bar)
        val balance = findViewById<TextView>(R.id.balance_amount)
        val earnedAmount = findViewById<TextView>(R.id.earned_amount)
        val spentAmount = findViewById<TextView>(R.id.spent_amount)
        val plusBtn = findViewById<FloatingActionButton>(R.id.plus_button)
        val bottomNav = findViewById<BottomNavigationView>(R.id.botnav)
        val spentBtn = findViewById<LinearLayout>(R.id.spent)
        val finacialCard = findViewById<CardView>(R.id.finacial_card)

        //Recycler view

        val goal1 = FinancialModel("Buy a Car", "Savings", 10000.00, 100.00, "2024-12-31")
        val goal2 = FinancialModel("Holiday Trip", "Savings", 5000.00, 3540.00,"2024-06-15")
        val goal3 = FinancialModel("Emergency Fund", "Savings", 2000.00, 500.00,"2024-03-01")

//        val goals: List<FinancialModel> = financialViewModel.getFinancialGoals(userId, null) { success, error ->
//            if (success) {
//                    Toast.makeText(this, "Fin added successfully", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Failed to add expense: $error", Toast.LENGTH_SHORT).show()
//                }
//        }
//
//
//        if(userId != null) {
//            financialViewModel.addFinancialGoal(userId, goal1) { success, error ->
//                if (success) {
//                    Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Failed to add expense: $error", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

//                financialViewModel.addFinancialGoal(userId, goal2){ success, error ->
//                    if (success) {
//                        Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this, "Failed to add expense: $error", Toast.LENGTH_SHORT).show()
//                    }
//
//                    financialViewModel.addFinancialGoal(userId, goal3){ success, error ->
//                        if (success) {
//                            Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(this, "Failed to add expense: $error", Toast.LENGTH_SHORT).show()
//                        }
//        }

//        val goal = financialViewModel.getFinancialGoals(userId) { success, error ->
//            if (success) {
//                Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Failed to add expense: $error", Toast.LENGTH_SHORT).show()
//            }

//        val goalsRV = findViewById<RecyclerView>(R.id.horizontal_finacial_goal_rv)
//        val adapter = HomeGoalsAdapter()
//        goalsRV.adapter = adapter
//        goalsRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        plusBtn.setOnClickListener {
            val dialog = AddExpenseFragment()
            dialog.show(supportFragmentManager, "AddExpenseDialog")
        }

        spentBtn.setOnClickListener {
            val expense = Intent(this@Home, ExpenseTracker::class.java)
            startActivity(expense)
            finish()
        }

        if (hp != null) {
            hpBar.setProgress(hp, true)
        }

        if (xp != null) {
            xpBar.setProgress(xp, true)
        }

        finacialCard.setOnClickListener{
//            val fin = Intent(this@Home, )
//            startActivity(fin)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchFinancialGoals() {
        financialViewModel.getFinancialGoals(userId) { financialGoals, error ->
            if (error == null) {
                // Successfully retrieved goals
                Log.d("FinancialGoalsFragment", "Goals: $financialGoals")

                // Update the UI, e.g., RecyclerView
                val recyclerView: RecyclerView = findViewById(R.id.horizontal_finacial_goal_rv)!!
                recyclerView.adapter = HomeGoalsAdapter(financialGoals)
            } else {
                // Handle error
                Log.e("FinancialGoalsFragment", "Error fetching goals: $error")
                Toast.makeText(this, "Error fetching financial goals", Toast.LENGTH_SHORT).show()
            }
        }
    }

}