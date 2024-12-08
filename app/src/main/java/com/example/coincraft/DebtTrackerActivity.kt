package com.example.coincraft

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DebtTrackerActivity : AppCompatActivity(), NewDebtDialogFragment.OnDebtAddedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViews: RecyclerView
    private lateinit var adapter: DebtCardAdapterL
    private lateinit var adapters: DebtCardAdapterS
    private lateinit var cardList: ArrayList<DebtCardModelL>
    private lateinit var cardLists: ArrayList<DebtCardModelS>
    private lateinit var addDebtButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_tracker)


        addDebtButton = findViewById(R.id.newdebtbtn)
        addDebtButton.setOnClickListener {
            val dialog = NewDebtDialogFragment()
            dialog.show(supportFragmentManager, "NewDebtDialog")
        }


        recyclerView = findViewById(R.id.rview_topdebts)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        cardList = ArrayList()
        cardList.add(
            DebtCardModelL(
                profileImage = R.drawable.avatar,
                name = "Gandalf The Great",
                date = "November 30, 2034",
                coinImage = R.drawable.coin,
                amount = "500.00"
            )
        )
        cardList.add(
            DebtCardModelL(
                profileImage = R.drawable.avatar, // You can replace this with another drawable resource
                name = "Frodo Baggins",
                date = "December 8, 2034",
                coinImage = R.drawable.coin,
                amount = "1500.00"
            )
        )
        // Add more items as needed

        adapter = DebtCardAdapterL(this, cardList)
        recyclerView.adapter = adapter


        recyclerViews = findViewById(R.id.rview_yourdebts)
        recyclerViews.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        cardLists = ArrayList()
        cardLists.add (
            DebtCardModelS(
                profileImage = R.drawable.avatar,
                name = "Legolas",
                date = "12/24/24",
                coinImage = R.drawable.coin,
                amount = "50.00"
            )
        )
        cardLists.add (
            DebtCardModelS(
                profileImage = R.drawable.avatar,
                name = "Legolas",
                date = "12/24/24",
                coinImage = R.drawable.coin,
                amount = "50.00"
            )
        )
        cardLists.add (
            DebtCardModelS(
                profileImage = R.drawable.avatar,
                name = "Legolas",
                date = "12/24/24",
                coinImage = R.drawable.coin,
                amount = "50.00"
            )
        )
        cardLists.add (
            DebtCardModelS(
                profileImage = R.drawable.avatar,
                name = "Legolas",
                date = "12/24/24",
                coinImage = R.drawable.coin,
                amount = "50.00"
            )
        )
        cardLists.add (
            DebtCardModelS(
                profileImage = R.drawable.avatar,
                name = "Legolas",
                date = "12/24/24",
                coinImage = R.drawable.coin,
                amount = "50.00"
            )
        )
        cardLists.add (
            DebtCardModelS(
                profileImage = R.drawable.avatar,
                name = "Legolas",
                date = "12/24/24",
                coinImage = R.drawable.coin,
                amount = "50.00"
            )
        )

        adapters = DebtCardAdapterS(this, cardLists)
        recyclerViews.adapter = adapters
    }

    override fun onDebtAdded(amount: String, name: String, date: String) {
        // Handle the data sent from the dialog
        cardLists.add(
            DebtCardModelS(
                profileImage = R.drawable.avatar, // Adjust based on your implementation
                name = name,
                date = date,
                coinImage = R.drawable.coin,
                amount = amount
            )
        )
        adapters.notifyDataSetChanged()
    }
}