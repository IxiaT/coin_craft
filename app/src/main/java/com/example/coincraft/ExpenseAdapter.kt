package com.example.coincraft

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

abstract class ExpenseAdapter (
    private val context: Context,
    private val expenseList: List<TransactionModel>
) : RecyclerView.Adapter<ExpenseAdapter.CardViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.expense_layout, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val model = expenseList[position]
        holder.expenseAmount.text = model.amount.toString()
        holder.expenseCat.text = model.category

        holder.detailBtn.setOnClickListener {
            Toast.makeText(context, "${holder.expenseCat.text} Clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expenseAmount = itemView.findViewById<TextView>(R.id.expense_category)
        val expenseCat = itemView.findViewById<TextView>(R.id.earned_amount)
        val detailBtn = itemView.findViewById<ImageButton>(R.id.details_button)
    }


}