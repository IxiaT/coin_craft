package com.example.coincraft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val itemList: List<TransactionModel>, private val fragmentManager: FragmentManager, private val type: String) :
    RecyclerView.Adapter<TransactionAdapter.TransactionsViewHolder>() {

    private val earned = R.drawable.purchase

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item_layout, parent, false)
        return TransactionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        val item = itemList[position]
        holder.transactionCat.text = item.category
        holder.transactionAmount.text = item.amount.toString()

        if(type == "spent"){
            holder.Icon.setImageResource(earned)
        }

        holder.itemView.setOnClickListener {
            val dialog = UpdateTransaction()
            val bundle = Bundle()
            bundle.putSerializable("item", item)
            bundle.putString("type", type)
            dialog.arguments = bundle
            dialog.show(fragmentManager, "UpdateTransactionDialog")
        }

    }

    override fun getItemCount(): Int = itemList.size

    class TransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionCat: TextView = itemView.findViewById(R.id.transaction_category)
        val transactionAmount: TextView = itemView.findViewById(R.id.transaction_amount)
        val Icon: ImageView = itemView.findViewById(R.id.transaction_icon)
    }
}