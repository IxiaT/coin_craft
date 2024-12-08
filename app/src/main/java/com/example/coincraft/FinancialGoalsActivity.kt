package com.example.coincraft

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

class FinancialGoalsActivity : AppCompatActivity(), AddGoalDialogFragment.GoalSaveListener {

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

        // Find the Add Goal Button
        val addGoalBtn: ImageButton = findViewById(R.id.addgoalbtn)

        // Set the click listener for the button
        addGoalBtn.setOnClickListener {
            // Show the Add Goal Dialog
            val dialogFragment = AddGoalDialogFragment()
            dialogFragment.setGoalSaveListener(this) // Set this activity as the listener
            dialogFragment.show(supportFragmentManager, "AddGoalDialog")
        }

        // Initialize views
        savedBalanceEdit = findViewById(R.id.saved_balance_edit)
        plannedBalanceEdit = findViewById(R.id.planned_balance_edit)

        // Set initial balance values
        savedBalanceEdit.setText(savedBalance.toString())
        plannedBalanceEdit.setText(plannedBalance.toString())

        // Setup balance listeners
        setupBalanceListeners()

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

    override fun onGoalSaved(newGoal: Goal) {
        // Add the new goal to the list and update the adapter
        goalList.add(newGoal)
        goalsAdapter.notifyItemInserted(goalList.size - 1)
        Toast.makeText(this, "New goal added: ${newGoal.name}", Toast.LENGTH_SHORT).show()
    }

    private fun setupBalanceListeners() {
        savedBalanceEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                savedBalance = s?.toString()?.toDoubleOrNull() ?: 0.0
                updateProgress()
            }
        })

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
        val progressPercentage = if (plannedBalance != 0.0) {
            ((savedBalance / plannedBalance) * 100).toInt()
        } else {
            0
        }
        val progressTextView = findViewById<TextView>(R.id.progressPercentage)
        progressTextView.text = "$progressPercentage%"
        val progressIndicator = findViewById<CircularProgressIndicator>(R.id.financialGoalProgress)
        progressIndicator.progress = progressPercentage
    }
}
