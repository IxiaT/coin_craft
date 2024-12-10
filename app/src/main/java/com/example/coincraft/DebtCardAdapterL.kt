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
    private var cardList: List<DebtCardModel>,
    private val onItemClicked: (DebtCardModel, Int) -> Unit
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

        // Change text color based on the state
        val color = if (card.state == "to pay") {
            android.graphics.Color.parseColor("#F24E1E") // Red for "to pay"
        } else {
            android.graphics.Color.parseColor("#0ACF83") // Green for "to receive"
        }
        holder.txtAmount.setTextColor(color)

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemClicked(card, position)
        }

        holder.btnPlus.setOnClickListener {
            // Add your functionality for the plus button
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
        val txtAmount: TextView = itemView.findViewById(R.id.txtamount)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnplus)
    }

    fun updateData(newCardList: List<DebtCardModel>) {
        cardList = newCardList
        notifyDataSetChanged()  // Notify that the data set has changed
    }
}
