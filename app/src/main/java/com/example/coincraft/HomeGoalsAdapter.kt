package com.example.coincraft

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

class HomeGoalsAdapter (
    private val financialGoals: List<FinancialModel>,
) : RecyclerView.Adapter<HomeGoalsAdapter.HomeGoalsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGoalsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goals_card_layout, parent,false)
        return HomeGoalsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return financialGoals.size
    }

    override fun onBindViewHolder(holder: HomeGoalsViewHolder, position: Int) {
        val model = financialGoals[position]

        // Set the goal type, name, and other properties from the FinancialModel
        holder.goalType.text = model.name  // Assuming `goalType` is related to the `name`
        holder.goalName.text = model.name  // Goal name (model name)

        // Use `getFormattedDateForDisplay()` for formatted date display
        holder.goalDeadline.text = model.getFormattedDateForDisplay()

        // Update progress information
        holder.goalProgress.text = "${model.saved} / ${model.target} - ${model.percentage}%"

        // Optionally, update other UI elements based on the FinancialModel properties
    }


    class HomeGoalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalName = itemView.findViewById<TextView>(R.id.goal_name)
        val goalDeadline = itemView.findViewById<TextView>(R.id.goal_deadline)
        val goalProgress = itemView.findViewById<TextView>(R.id.goal_progress)
        val goalType = itemView.findViewById<TextView>(R.id.goal_type)


    }
}