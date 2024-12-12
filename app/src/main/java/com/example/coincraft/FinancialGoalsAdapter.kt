package com.example.coincraft

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class FinancialGoalsAdapter(
    private val context: Context,
    private val goals: MutableList<FinancialModel>,  // Use FinancialModel here
    private val fragmentManager: FragmentManager,
    private val updateBalanceListener: UpdateBalanceListener
) : RecyclerView.Adapter<FinancialGoalsAdapter.GoalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.goal_item, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]

        // Display remaining balance as a whole number
        val remaining = if (goal.remaining < 0) 0.0 else goal.remaining
        holder.goalRemaining.text = String.format("%.0f", remaining)

        holder.goalProgressBar.progress = goal.percentage

        // Set color of the progress bar
        val progressColor = if (goal.percentage >= 100) R.color.green else R.color.yellow
        holder.goalProgressBar.progressDrawable.setColorFilter(
            ContextCompat.getColor(context, progressColor),
            android.graphics.PorterDuff.Mode.SRC_IN
        )

        // Set text color for remaining balance
        if (goal.percentage >= 100) {
            holder.goalRemaining.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else {
            holder.goalRemaining.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        // Dynamically set goal icon using the map
        val goalTypeToDrawableMap = mapOf(
            "Investment" to R.drawable.ic_investment,
            "Holiday" to R.drawable.ic_holiday,
            "Gadget" to R.drawable.ic_gadget,
            "Destination" to R.drawable.ic_destination,
            "Shoes" to R.drawable.ic_shoes,
            "Clothes" to R.drawable.ic_clothes,
            "Other" to R.drawable.ic_others
        )

        val iconResId = goalTypeToDrawableMap[goal.type] ?: R.drawable.ic_others // Default to "Other" icon
        holder.goalIcon.setImageResource(iconResId)

        holder.goalName.text = goal.name
        holder.goalProgress.text = "${goal.saved} / ${goal.target} - ${goal.percentage}%"

        // Format and display goal date
        holder.goalDate.text = formatDateForDisplay(goal.date)

        // Item click listener to open update dialog fragment
        holder.itemView.setOnClickListener {
            openUpdateDialog(goal, position)
        }
    }

    override fun getItemCount(): Int = goals.size

    private fun openUpdateDialog(goal: FinancialModel, position: Int) {
        val updateDialog = UpdateGoalDialogFragment()
        updateDialog.setGoal(goal)

        // Use the setOnGoalUpdateListener method to handle updates
        updateDialog.setOnGoalUpdateListener(object : UpdateGoalDialogFragment.OnGoalUpdateListener {
            override fun onGoalUpdated(updatedGoal: FinancialModel) {
                // Check if the goal's percentage has reached 100% and update the state
                if (updatedGoal.percentage >= 100) {
                    updatedGoal.state = true // Mark as completed
                } else {
                    updatedGoal.state = false // Mark as not completed
                }

                // Update the goal in the list
                val position = goals.indexOfFirst { it.id == updatedGoal.id } // Find the position of the updated goal
                if (position != -1) {
                    goals[position] = updatedGoal
                    notifyItemChanged(position) // Notify that specific item has changed
                }

                // Update the balance in the parent activity
                updateBalanceListener.onGoalUpdated(updatedGoal)
            }

            override fun onGoalDeleted(deletedGoal: FinancialModel) {
                // Find the position of the goal in the list and remove it
                val positionToRemove = goals.indexOf(deletedGoal)
                if (positionToRemove != -1) {
                    goals.removeAt(positionToRemove)
                    notifyItemRemoved(positionToRemove) // Notify that specific item has been removed
                    notifyItemRangeChanged(positionToRemove, goals.size) // Adjust the rest of the list
                    updateBalanceListener.onGoalDeleted(deletedGoal) // Notify the parent activity of delete
                }
            }
        })

        // Show the dialog
        updateDialog.show(fragmentManager, "UpdateGoalDialog")
    }

    private fun formatDateForDisplay(date: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val parsedDate = inputFormat.parse(date) ?: return date
            outputFormat.format(parsedDate)
        } catch (e: Exception) {
            date // Fallback to the original date string in case of parsing error
        }
    }

    // Interface to notify updates
    interface UpdateBalanceListener {
        fun onGoalUpdated(updatedGoal: FinancialModel)
        fun onGoalDeleted(goal: FinancialModel)
    }

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalIcon: ImageView = itemView.findViewById(R.id.goal_icon)
        val goalName: TextView = itemView.findViewById(R.id.goal_name)
        val goalProgress: TextView = itemView.findViewById(R.id.goal_progress)
        val goalDate: TextView = itemView.findViewById(R.id.goal_date)
        val goalProgressBar: ProgressBar = itemView.findViewById(R.id.goal_progress_bar)
        val goalRemaining: TextView = itemView.findViewById(R.id.goal_remaining)
    }
}