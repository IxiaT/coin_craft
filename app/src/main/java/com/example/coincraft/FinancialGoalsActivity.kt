package com.example.coincraft

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth

class FinancialGoalsActivity : AppCompatActivity(), AddGoalDialogFragment.GoalSaveListener, FinancialGoalsAdapter.UpdateBalanceListener, UpdateGoalDialogFragment.OnGoalUpdateListener {

    private lateinit var goalsRecyclerView: RecyclerView
    private lateinit var financialGoalsAdapter: FinancialGoalsAdapter
    private lateinit var goalList: MutableList<FinancialModel>  // Use FinancialModel here
    private lateinit var savedBalanceEdit: EditText
    private lateinit var plannedBalanceEdit: EditText
    private lateinit var backBtn: ImageButton
    private var savedBalance: Double = 0.0
    private var plannedBalance: Double = 0.0
    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var progressTextView: TextView

    private lateinit var financialRepository: FinancialRepository
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_financial_goals)

        // Initialize Firebase and repository
        val currentUser = FirebaseAuth.getInstance().currentUser
        userId = currentUser?.uid ?: ""
        financialRepository = FinancialRepository()

        // Initialize views
        val addGoalBtn: ImageButton = findViewById(R.id.addgoalbtn)
        savedBalanceEdit = findViewById(R.id.saved_balance_edit)
        plannedBalanceEdit = findViewById(R.id.planned_balance_edit)
        progressIndicator = findViewById(R.id.financialGoalProgress)
        progressTextView = findViewById(R.id.progressPercentage)
        backBtn = findViewById(R.id.backbtn)

        backBtn.setOnClickListener {
            val homeAct = Intent(this, Home::class.java)
            startActivity(homeAct)
        }

        // Set the click listener for the Add Goal button
        addGoalBtn.setOnClickListener {
            val dialogFragment = AddGoalDialogFragment()
            dialogFragment.setGoalSaveListener(this)
            dialogFragment.show(supportFragmentManager, "AddGoalDialog")
        }

        // Setup RecyclerView
        goalsRecyclerView = findViewById(R.id.goalsRecyclerView)
        goalsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch the goals data from Firebase
        fetchFinancialGoals()

        // Initialize balance values and listeners
        setupBalanceListeners()
    }

    private fun fetchFinancialGoals() {
        // Fetch data from the repository (Firebase)
        financialRepository.getFinancialGoals(userId) { financialGoals, error ->
            if (error == null) {
                // If no error, update the goal list and refresh the adapter
                goalList = financialGoals.toMutableList()
                financialGoalsAdapter = FinancialGoalsAdapter(this, goalList, supportFragmentManager, this)
                goalsRecyclerView.adapter = financialGoalsAdapter
                updateOverallBalance()
            } else {
                // If there was an error, show a toast message
                Toast.makeText(this, "Failed to fetch goals: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onGoalSaved(newGoal: FinancialModel) {
        // Add the goal to the local list and update the RecyclerView
        goalList.add(newGoal)
        financialGoalsAdapter.notifyItemInserted(goalList.size - 1)
        Toast.makeText(this, "New goal added: ${newGoal.name}", Toast.LENGTH_SHORT).show()
        updateOverallBalance()
    }

    override fun onGoalUpdated(updatedGoal: FinancialModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        // Calling the repository to update the goal in Firebase
        financialRepository.updateFinancialGoal(userId, updatedGoal.key ?: return, updatedGoal) { success, errorMessage ->
            if (success) {
                fetchFinancialGoals()  // Reload the updated list of goals
                Toast.makeText(this, "Goal updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update goal: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onGoalDeleted(goal: FinancialModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        // Calling the repository to delete the goal in Firebase
        financialRepository.deleteFinancialGoal(userId, goal.key ?: return) { success, errorMessage ->
            if (success) {
                goalList.remove(goal)  // Remove the goal from the local list
                Toast.makeText(this, "Goal deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete goal: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
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
            progressIndicator.setIndicatorColor(ContextCompat.getColor(this, R.color.yellow))
        }

        progressTextView.text = "$progressPercentage%"
    }
}
