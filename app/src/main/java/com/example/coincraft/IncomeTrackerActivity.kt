package com.example.coincraft

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class IncomeTrackerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_tracker)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewPopularProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemList = listOf(
            IncomeTrackerItem("Gold Earned - January", R.drawable.balance, "₱1000"),
            IncomeTrackerItem("Gold Earned - February", R.drawable.balance, "₱1500"),
            IncomeTrackerItem("Gold Earned - March", R.drawable.balance, "₱2000")
        )

        val adapter = IncomeTrackerAdapter(itemList)
        recyclerView.adapter = adapter
    }
}
