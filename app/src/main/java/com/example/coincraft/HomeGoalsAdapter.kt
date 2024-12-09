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
        holder.goalType.text = model.goalType
        holder.goalName.text = model.goalName
        holder.goalDeadline.text = model.date
        holder.goalProgress.text = "${model.currentProgress}/${model.amount}"
    }

    class HomeGoalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalName = itemView.findViewById<TextView>(R.id.goal_name)
        val goalDeadline = itemView.findViewById<TextView>(R.id.goal_deadline)
        val goalProgress = itemView.findViewById<TextView>(R.id.goal_progress)
        val goalType = itemView.findViewById<TextView>(R.id.goal_type)


    }
}