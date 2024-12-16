package com.example.coincraft

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coincraft.databinding.ActivityBudgetingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class BudgetingActivity : AppCompatActivity() {

    private lateinit var topbackbtn: ImageButton
    private lateinit var monthYear: TextView
    private lateinit var prevMonth: ImageButton
    private lateinit var nextMonth: ImageButton
    private var currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    private var currentYear = Calendar.getInstance().get(Calendar.YEAR)

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var binding: ActivityBudgetingBinding
    private val budgetFragment = BudgetingMainFragment()
    private var bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budgeting)

        topbackbtn = findViewById(R.id.back_button)
        prevMonth = findViewById(R.id.month_prev_btn)
        nextMonth = findViewById(R.id.month_next_btn)
        monthYear = findViewById(R.id.month_year_txt)
        bottomNav = findViewById(R.id.botnav)

        //Setup the current Month
        getMonthName(currentMonth + 1)
        monthYear.text = "${getMonthName(currentMonth)} ${currentYear}"

        bundle.putInt("month", currentMonth)
        bundle.putInt("year", currentYear)
        budgetFragment.arguments = bundle
        binding = ActivityBudgetingBinding.inflate(layoutInflater)
        supportFragmentManager.beginTransaction()
            .replace(R.id.budget_main_fragment, budgetFragment)
            .commit()

        topbackbtn.setOnClickListener {
            val homeAct = Intent(this, Home::class.java)
            startActivity(homeAct)
        }

        prevMonth.setOnClickListener {
            currentMonth--
            if(currentMonth < 0) {
                currentMonth = 11
                currentYear--
            }
            monthYear.text = "${getMonthName(currentMonth)} ${currentYear}"
            refreshFragment()
        }

        nextMonth.setOnClickListener {
            currentMonth++
            if(currentMonth > 11) {
                currentMonth = 0
                currentYear++
            }
            monthYear.text = "${getMonthName(currentMonth)} ${currentYear}"
            refreshFragment()
        }

        // Change intent to your respective activities
        bottomNav.selectedItemId = R.id.navigation_budgeting
        bottomNav.setOnNavigationItemSelectedListener { item ->
            val itemId = item.itemId
            var intent: Intent

            when (itemId) {
                R.id.navigation_home -> {
                    intent = Intent(this@BudgetingActivity, Home::class.java)
                    startActivity(intent)
                }
                R.id.navigation_transaction -> {
                    intent = Intent(this@BudgetingActivity, Transaction::class.java)
                    startActivity(intent)
                }
                R.id.navigation_budgeting-> {
                    if (this !is BudgetingActivity){
                        intent = Intent(this@BudgetingActivity, BudgetingActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.navigation_debt -> {
                    intent = Intent(this@BudgetingActivity, DebtTrackerActivity::class.java)
                    startActivity(intent)
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

    }

    private fun refreshFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.budget_main_fragment)

        if (fragment != null) {
            // Remove the current fragment
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()

            // Wait for the transaction to finish
            supportFragmentManager.executePendingTransactions()

            // Recreate and replace the fragment with updated arguments
            val newFragment = BudgetingMainFragment().apply {
                arguments = Bundle().apply {
                    putInt("month", currentMonth)
                    putInt("year", currentYear)
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.budget_main_fragment, newFragment)
                .commit()
        }
    }

    private fun getMonthName(month: Int): String {
        return when (month + 1) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Invalid Month" // Handle invalid input
        }
    }


}