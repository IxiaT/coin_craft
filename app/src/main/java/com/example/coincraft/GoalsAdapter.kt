package com.example.coincraft

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class GoalsAdapter(
    private val context: Context,
    private val goals: MutableList<Goal>
) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.goal_item, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]

        // Set the goal data
        holder.goalIcon.setImageResource(goal.icon)
        holder.goalName.text = goal.name
        holder.goalProgress.text = "${goal.saved} / ${goal.target} - ${goal.percentage}%"
        holder.goalRemaining.text = "${goal.remaining}"
        holder.goalProgressBar.progress = goal.percentage

        // Set color of the progress bar
        val color = if (goal.percentage >= 100) R.color.green else R.color.default_color
        holder.goalProgressBar.progressDrawable.setColorFilter(
            ContextCompat.getColor(context, color),
            android.graphics.PorterDuff.Mode.SRC_IN
        )

        // Item click listener
        holder.itemView.setOnClickListener {
            openDialog(goal, position)
        }
    }

    override fun getItemCount(): Int = goals.size

    private fun openDialog(goal: Goal, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.popup_update_goal_layout, null)
        val dialog = AlertDialog.Builder(context).setView(dialogView).create()

        val tvCurrentBalance = dialogView.findViewById<TextView>(R.id.tv_current_balance)
        val etAmount = dialogView.findViewById<EditText>(R.id.et_amount)
        val btnWithdraw = dialogView.findViewById<Button>(R.id.btn_withdraw)
        val btnDeposit = dialogView.findViewById<Button>(R.id.btn_deposit)
        val btnDelete = dialogView.findViewById<Button>(R.id.btn_delete)

        // Set initial balance
        tvCurrentBalance.text = "Current Balance: ${goal.saved}"

        // Withdraw button
        btnWithdraw.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull()
            if (amount != null && amount > 0 && goal.saved >= amount) {
                goal.saved -= amount
                goal.percentage = ((goal.saved / goal.target) * 100).toInt()
                goal.remaining = goal.target - goal.saved
                tvCurrentBalance.text = "Current Balance: ${goal.saved}"
                // Notify item change so it updates in RecyclerView
                notifyItemChanged(position)
            } else {
                Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show()
            }
        }

        // Deposit button
        btnDeposit.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull()
            if (amount != null && amount > 0) {
                goal.saved += amount
                goal.percentage = ((goal.saved / goal.target) * 100).toInt()
                goal.remaining = goal.target - goal.saved
                tvCurrentBalance.text = "Current Balance: ${goal.saved}"
                // Notify item change so it updates in RecyclerView
                notifyItemChanged(position)
            } else {
                Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete button
        btnDelete.setOnClickListener {
            // Remove the goal from the list and notify the adapter
            goals.removeAt(position)
            // Notify RecyclerView that an item has been removed
            notifyItemRemoved(position)
            dialog.dismiss()

            // Optionally, if you want to refresh the entire list
            notifyDataSetChanged() // Uncomment this if needed
        }

        dialog.show()
    }

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalIcon: ImageView = itemView.findViewById(R.id.goal_icon)
        val goalName: TextView = itemView.findViewById(R.id.goal_name)
        val goalProgress: TextView = itemView.findViewById(R.id.goal_progress)
        val goalRemaining: TextView = itemView.findViewById(R.id.goal_remaining)
        val goalProgressBar: ProgressBar = itemView.findViewById(R.id.goal_progress_bar)
    }
}

