package com.example.coincraft

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DebtCardAdapterS(
    private val context: Context,
    private val cardList: List<DebtCardModelS>
) : RecyclerView.Adapter<DebtCardAdapterS.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.debtcard_small, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        holder.profileImg.setImageResource(card.profileImage)
        holder.txtName.text = card.name
        holder.txtDate.text = card.date
        holder.coinImg.setImageResource(card.coinImage)
        holder.txtAmount.text = card.amount

        holder.btnSelect.setOnClickListener {
            // Add your functionality for the plus button
        }

    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImg: ImageView = itemView.findViewById(R.id.profileimg_s)
        val txtName: TextView = itemView.findViewById(R.id.txtname_s)
        val txtDate: TextView = itemView.findViewById(R.id.txtdate_s)
        val coinImg: ImageView = itemView.findViewById(R.id.imgcoin_s)
        val txtAmount: TextView = itemView.findViewById(R.id.txtamount_s)
        val btnSelect: ImageButton = itemView.findViewById(R.id.imgbtn_select)
    }

}