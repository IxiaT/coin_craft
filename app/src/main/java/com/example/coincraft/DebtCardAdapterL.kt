package com.example.coincraft

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DebtCardAdapterL(
    private val context: Context,
    private val cardList: List<DebtCardModelL>
) : RecyclerView.Adapter<DebtCardAdapterL.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.debtcard_large, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        holder.profileImg.setImageResource(card.profileImage)
        holder.txtName.text = card.name
        holder.txtDate.text = card.date
        holder.coinImg.setImageResource(card.coinImage)
        holder.txtAmount.text = card.amount

        holder.btnPlus.setOnClickListener {
            // Add your functionality for the plus button
        }

        holder.btnMinus.setOnClickListener {
            // Add your functionality for the minus button
        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImg: ImageView = itemView.findViewById(R.id.profileimg_s)
        val txtName: TextView = itemView.findViewById(R.id.txtname)
        val txtDate: TextView = itemView.findViewById(R.id.txtdate)
        val coinImg: ImageView = itemView.findViewById(R.id.coinimg)
        val txtAmount: TextView = itemView.findViewById(R.id.txtdate)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnplus)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnminus)
    }
}
