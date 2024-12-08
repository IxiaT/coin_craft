package com.example.coincraft

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat

class GoalsAdapter(
    private val context: Context,
    private val goals: List<Goal>
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
        holder.goalRemaining.text = "-${goal.remaining}"

        // Set the progress of the progress bar based on the percentage
        holder.goalProgressBar.progress = goal.percentage

        // Set the background color of the progress bar
        holder.goalProgressBar.progressDrawable.setColorFilter(
            ContextCompat.getColor(context, R.color.default_color), // Background color (always the same)
            android.graphics.PorterDuff.Mode.SRC_IN
        )

        // Check if the progress is 100% to change the color to green
        if (goal.percentage >= 100) {
            holder.goalProgressBar.progressDrawable.setColorFilter(
                ContextCompat.getColor(context, R.color.green), // Set to green for completed progress
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }


    override fun getItemCount(): Int = goals.size

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalIcon: ImageView = itemView.findViewById(R.id.goal_icon)
        val goalName: TextView = itemView.findViewById(R.id.goal_name)
        val goalProgress: TextView = itemView.findViewById(R.id.goal_progress)
        val goalRemaining: TextView = itemView.findViewById(R.id.goal_remaining)
        val goalProgressBar: ProgressBar = itemView.findViewById(R.id.goal_progress_bar)
    }
}
