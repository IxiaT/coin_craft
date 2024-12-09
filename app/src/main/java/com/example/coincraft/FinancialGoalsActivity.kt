package com.example.coincraft

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

class FinancialGoalsActivity : AppCompatActivity(), AddGoalDialogFragment.GoalSaveListener, GoalsAdapter.UpdateBalanceListener {

    private lateinit var goalsRecyclerView: RecyclerView
    private lateinit var goalsAdapter: GoalsAdapter
    private lateinit var goalList: MutableList<Goal>
    private lateinit var savedBalanceEdit: EditText
    private lateinit var plannedBalanceEdit: EditText
    private var savedBalance: Double = 0.0
    private var plannedBalance: Double = 0.0

    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var progressTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_financial_goals)

        // Initialize views
        val addGoalBtn: ImageButton = findViewById(R.id.addgoalbtn)
        savedBalanceEdit = findViewById(R.id.saved_balance_edit)
        plannedBalanceEdit = findViewById(R.id.planned_balance_edit)
        progressIndicator = findViewById(R.id.financialGoalProgress)
        progressTextView = findViewById(R.id.progressPercentage)

        // Set the click listener for the Add Goal button
        addGoalBtn.setOnClickListener {
            val dialogFragment = AddGoalDialogFragment()
            dialogFragment.setGoalSaveListener(this)
            dialogFragment.show(supportFragmentManager, "AddGoalDialog")
        }

        // Setup RecyclerView
        goalsRecyclerView = findViewById(R.id.goalsRecyclerView)
        goalsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize goal data
        goalList = mutableListOf(
            Goal("Mother's Day", R.drawable.ic_holiday, 1000.0, 1000.0, "2024-12-08"),
            Goal("Boracay", R.drawable.ic_destination, 1500.0, 3000.0, "2024-12-08"),
            Goal("iPhone 16", R.drawable.ic_gadget, 7500.0, 30000.0, "2024-12-08"),
            Goal("Shoes", R.drawable.ic_shoes, 3250.0, 5000.0, "2024-12-08")
        )

        // Set up adapter with FragmentManager and listener
        goalsAdapter = GoalsAdapter(this, goalList, supportFragmentManager, this)
        goalsRecyclerView.adapter = goalsAdapter

        // Initialize balance values and listeners
        setupBalanceListeners()
        updateOverallBalance()
    }

    override fun onGoalSaved(newGoal: Goal) {
        goalList.add(newGoal)
        goalsAdapter.notifyItemInserted(goalList.size - 1)
        Toast.makeText(this, "New goal added: ${newGoal.name}", Toast.LENGTH_SHORT).show()
        updateOverallBalance()
    }

    override fun onGoalUpdated() {
        // Called when a goal is updated in the adapter
        updateOverallBalance()
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

    private fun updateOverallBalance() {
        savedBalance = goalList.sumOf { it.saved }
        plannedBalance = goalList.sumOf { it.target }
        savedBalanceEdit.setText(savedBalance.toString())
        plannedBalanceEdit.setText(plannedBalance.toString())
        updateProgress()
    }

    private fun updateProgress() {
        val progressPercentage = if (plannedBalance != 0.0) {
            ((savedBalance / plannedBalance) * 100).toInt()
        } else {
            0
        }

        progressIndicator.progress = progressPercentage

        if (progressPercentage >= 100) {
            progressIndicator.setIndicatorColor(ContextCompat.getColor(this, R.color.green))
        } else {
            progressIndicator.setIndicatorColor(ContextCompat.getColor(this, R.color.default_color))
        }

        progressTextView.text = "$progressPercentage%"
    }
}