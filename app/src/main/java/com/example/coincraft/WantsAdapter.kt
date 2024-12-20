package com.example.coincraft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WantsAdapter (private val dataList:ArrayList<NeedWantDataClass>): RecyclerView.Adapter<WantsAdapter.ViewHolderClass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_need_want,parent,false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvWantsCategory.text = currentItem.categoryTitle
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvWantsCategory: TextView = itemView.findViewById(R.id.itemnametxt)
    }
}