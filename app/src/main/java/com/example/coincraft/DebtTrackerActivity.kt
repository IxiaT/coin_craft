package com.example.coincraft

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DebtTrackerActivity : AppCompatActivity(), NewDebtDialogFragment.OnDebtAddedListener {

    private lateinit var topDebtsRecyclerView: RecyclerView
    private lateinit var yourDebtsRecyclerView: RecyclerView
    private lateinit var topDebtsAdapter: DebtCardAdapterL
    private lateinit var yourDebtsAdapter: DebtCardAdapterS
    private lateinit var topDebtsList: ArrayList<DebtCardModelL>
    private lateinit var yourDebtsList: ArrayList<DebtCardModelS>
    private lateinit var addDebtButton: ImageButton

    companion object {
        val DEFAULT_PROFILE_IMAGE = R.drawable.avatar
        val DEFAULT_COIN_IMAGE = R.drawable.coin
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_tracker)

        // Initialize views
        addDebtButton = findViewById(R.id.newdebtbtn)
        topDebtsRecyclerView = findViewById(R.id.rview_topdebts)
        yourDebtsRecyclerView = findViewById(R.id.rview_yourdebts)

        // Setup RecyclerViews
        setupRecyclerView(topDebtsRecyclerView)
        setupRecyclerView(yourDebtsRecyclerView)

        // Handle "Add Debt" button click
        addDebtButton.setOnClickListener {
            val dialog = NewDebtDialogFragment()
            dialog.show(supportFragmentManager, "NewDebtDialog")
        }

        // Populate initial data
        topDebtsList = generateTopDebts()
        yourDebtsList = generateYourDebts()

        // Bind data to adapters
        topDebtsAdapter = DebtCardAdapterL(this, topDebtsList)
        yourDebtsAdapter = DebtCardAdapterS(this, yourDebtsList)

        topDebtsRecyclerView.adapter = topDebtsAdapter
        yourDebtsRecyclerView.adapter = yourDebtsAdapter
    }

    // Set up RecyclerView layout to always be horizontal
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    // Generate sample data for "Top Debts"
    private fun generateTopDebts(): ArrayList<DebtCardModelL> {
        return arrayListOf(
            DebtCardModelL(
                profileImage = DEFAULT_PROFILE_IMAGE,
                name = "Gandalf The Great",
                date = "November 30, 2034",
                coinImage = DEFAULT_COIN_IMAGE,
                amount = "500.00",
                state = "to receive"
            ),
            DebtCardModelL(
                profileImage = DEFAULT_PROFILE_IMAGE,
                name = "Frodo Baggins",
                date = "December 8, 2034",
                coinImage = DEFAULT_COIN_IMAGE,
                amount = "1500.00",
                state = "to pay"
            )
        )
    }

    // Generate sample data for "Your Debts"
    private fun generateYourDebts(): ArrayList<DebtCardModelS> {
        val debtEntries = arrayListOf<DebtCardModelS>()
        for (i in 1..6) {
            debtEntries.add(
                DebtCardModelS(
                    profileImage = DEFAULT_PROFILE_IMAGE,
                    name = "Legolas",
                    date = "12/24/24",
                    coinImage = DEFAULT_COIN_IMAGE,
                    amount = "50.00",
                    state = "to pay"
                )
            )
        }
        return debtEntries
    }

    // Add new debt data dynamically
    override fun onDebtAdded(amount: String, name: String, date: String, state: String) {
        yourDebtsList.add(
            DebtCardModelS(
                profileImage = DEFAULT_PROFILE_IMAGE,
                name = name,
                date = date,
                coinImage = DEFAULT_COIN_IMAGE,
                amount = amount,
                state = state
            )
        )
        yourDebtsAdapter.notifyDataSetChanged()
    }


}
