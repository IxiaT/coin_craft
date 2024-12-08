package com.example.coincraft

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DebtTrackerActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapterL
    private lateinit var cardList: ArrayList<CardModelL>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_tracker)

        recyclerView = findViewById(R.id.rview_topdebts)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        cardList = ArrayList()
        cardList.add(
            CardModelL(
                profileImage = R.drawable.avatar,
                name = "Gandalf The Great",
                date = "November 30, 2034",
                coinImage = R.drawable.coin,
                amount = "500.00"
            )
        )
        cardList.add(
            CardModelL(
                profileImage = R.drawable.avatar, // You can replace this with another drawable resource
                name = "Frodo Baggins",
                date = "December 8, 2034",
                coinImage = R.drawable.coin,
                amount = "1500.00"
            )
        )
        // Add more items as needed

        adapter = CardAdapterL(this, cardList)
        recyclerView.adapter = adapter
    }
}