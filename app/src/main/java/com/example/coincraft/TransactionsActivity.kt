package com.example.coincraft

import TransactionAdapter
import TransactionItem
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coincraft.R

class TransactionsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var itemList: List<TransactionItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        //    these are just static values for display purposes ^^

        itemList = listOf(
            TransactionItem("Gold Earned", "₱1000", R.drawable.balance),
            TransactionItem("Gold Spent", "₱500", R.drawable.balance),
            TransactionItem("Gold Balance", "₱500", R.drawable.balance)
        )

        recyclerView = findViewById(R.id.recyclerViewTransactions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        transactionAdapter = TransactionAdapter(itemList)
        recyclerView.adapter = transactionAdapter
    }
}
