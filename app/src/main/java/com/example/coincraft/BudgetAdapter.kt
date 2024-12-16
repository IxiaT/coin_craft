package com.example.coincraft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class BudgetAdapter(private val List:List<BudgetModel>, private val fragmentManager: FragmentManager): RecyclerView.Adapter<BudgetAdapter.ViewHolderClass>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_need_want,parent,false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return List.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = List[position]
        holder.Category.text = currentItem.budgetCategory
        holder.Amounts.text = currentItem.budgetLimit.toString()

//        holder.itemView.setOnClickListener {
//            val dialog = UpdateBudget()
//            val bundle = Bundle()
//            bundle.putSerializable("item", item)
//            bundle.putString("type", type)
//            dialog.arguments = bundle
//            dialog.show(fragmentManager, "UpdateBudgetDialog")
//        }
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val Category = itemView.findViewById<TextView>(R.id.itemnametxt)
        val Amounts = itemView.findViewById<TextView>(R.id.category_amount)
    }
}