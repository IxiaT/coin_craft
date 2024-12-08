package com.example.coincraft

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

class FinancialGoalsActivity : AppCompatActivity() {

    private lateinit var goalsRecyclerView: RecyclerView
    private lateinit var goalsAdapter: GoalsAdapter
    private lateinit var goalList: MutableList<Goal>
    private lateinit var savedBalanceEdit: EditText
    private lateinit var plannedBalanceEdit: EditText
    private var savedBalance: Double = 34661.0
    private var plannedBalance: Double = 50000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_financial_goals)

        // Initialize views
        val progressIndicator = findViewById<CircularProgressIndicator>(R.id.financialGoalProgress)
        val progressText = findViewById<TextView>(R.id.progressPercentage)
        savedBalanceEdit = findViewById(R.id.saved_balance_edit)
        plannedBalanceEdit = findViewById(R.id.planned_balance_edit)

        // Set initial balance values
        savedBalanceEdit.setText(savedBalance.toString())
        plannedBalanceEdit.setText(plannedBalance.toString())

        // Setup balance listeners
        setupBalanceListeners()

        // Calculate and display initial progress
        updateProgress()

        // Setup RecyclerView for goals
        goalsRecyclerView = findViewById(R.id.goalsRecyclerView)
        goalsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Sample goal data
        goalList = mutableListOf(
            Goal("Mother's Day", R.drawable.ic_goal_icon, 1000.0, 1000.0),
            Goal("Boracay", R.drawable.ic_goal_icon, 1500.0, 3000.0),
            Goal("iPhone 16", R.drawable.ic_goal_icon, 7500.0, 30000.0),
            Goal("Shoes", R.drawable.ic_goal_icon, 3250.0, 5000.0)
        )

        // Set up adapter
        goalsAdapter = GoalsAdapter(this, goalList)
        goalsRecyclerView.adapter = goalsAdapter
    }

    private fun setupBalanceListeners() {
        // Listener for Saved Balance
        savedBalanceEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                savedBalance = s?.toString()?.toDoubleOrNull() ?: 0.0
                updateProgress()
            }
        })

        // Listener for Planned Balance
        plannedBalanceEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                plannedBalance = s?.toString()?.toDoubleOrNull() ?: 0.0
                updateProgress()
            }
        })
    }

    private fun updateProgress() {
        // Calculate progress percentage
        val progressPercentage = if (plannedBalance != 0.0) {
            ((savedBalance / plannedBalance) * 100).toInt()
        } else {
            0
        }

        // Update progress text
        val progressTextView = findViewById<TextView>(R.id.progressPercentage)
        progressTextView.text = "$progressPercentage%"

        // Update progress indicator
        val progressIndicator = findViewById<CircularProgressIndicator>(R.id.financialGoalProgress)
        progressIndicator.progress = progressPercentage

        // Change color of the progress bar to green if 100%
        if (progressPercentage >= 100) {
            progressIndicator.setIndicatorColor(getColor(R.color.green)) // Ensure you have a green color in your resources
        } else {
            progressIndicator.setIndicatorColor(getColor(R.color.default_color)) // Reset to default color
        }
    }
}