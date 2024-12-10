package com.example.coincraft

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DebtTrackerActivity : AppCompatActivity(), NewDebtDialogFragment.OnDebtAddedListener, EditDebtDialogFragment.OnDebtEditedListener {

    private lateinit var toReceiveRecyclerView: RecyclerView
    private lateinit var toPayRecyclerView: RecyclerView
    private lateinit var toReceiveAdapter: DebtCardAdapterL
    private lateinit var toPayAdapter: DebtCardAdapterS
    private lateinit var toReceiveList: ArrayList<DebtCardModel>
    private lateinit var toPayList: ArrayList<DebtCardModel>
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
        toReceiveRecyclerView = findViewById(R.id.rview_topdebts)
        toPayRecyclerView = findViewById(R.id.rview_yourdebts)

        // Setup RecyclerViews
        setupRecyclerView(toReceiveRecyclerView)
        setupRecyclerView(toPayRecyclerView)

        // Handle "Add Debt" button click
        addDebtButton.setOnClickListener {
            val dialog = NewDebtDialogFragment()
            dialog.show(supportFragmentManager, "NewDebtDialog")
        }

        // Populate initial data
        toReceiveList = generateToReceiveDebts()
        toPayList = generateToPayDebts()

        // Bind data to adapters with onItemClicked implementation
        toReceiveAdapter = DebtCardAdapterL(this, toReceiveList) { selectedCard, position ->
            openEditDebtDialog(selectedCard.name, selectedCard.amount, selectedCard.date, selectedCard.state, position, "toReceive")
        }

        toPayAdapter = DebtCardAdapterS(this, toPayList) { selectedCard, position ->
            openEditDebtDialog(selectedCard.name, selectedCard.amount, selectedCard.date, selectedCard.state, position, "toPay")
        }

        toReceiveRecyclerView.adapter = toReceiveAdapter
        toPayRecyclerView.adapter = toPayAdapter
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun generateToReceiveDebts(): ArrayList<DebtCardModel> {
        return arrayListOf(
            DebtCardModel(DEFAULT_PROFILE_IMAGE, "Gandalf The Great", "November 30, 2034", DEFAULT_COIN_IMAGE, "500.00", "to receive")
        )
    }

    private fun generateToPayDebts(): ArrayList<DebtCardModel> {
        return arrayListOf(
            DebtCardModel(DEFAULT_PROFILE_IMAGE, "Frodo Baggins", "December 8, 2034", DEFAULT_COIN_IMAGE, "1500.00", "to pay")
        )
    }

    override fun onDebtAdded(amount: String, name: String, date: String, state: String) {
        val newDebt = DebtCardModel(DEFAULT_PROFILE_IMAGE, name, date, DEFAULT_COIN_IMAGE, amount, state)
        if (state == "to receive") {
            toReceiveList.add(newDebt)
            toReceiveAdapter.notifyDataSetChanged()
        } else {
            toPayList.add(newDebt)
            toPayAdapter.notifyDataSetChanged()
        }

    }

    private fun openEditDebtDialog(debtOwner: String, currentAmount: String, debtDate: String, debtStatus: String, position: Int, adapterType: String) {
        val dialogFragment = EditDebtDialogFragment()
        val bundle = Bundle().apply {
            putString("debtOwner", debtOwner)
            putString("currentAmount", currentAmount)
            putString("debtStatus", debtStatus)
            putString("debtDate", debtDate)
            putInt("debtPosition", position)
            putString("adapterType", adapterType)
        }
        dialogFragment.arguments = bundle
        dialogFragment.show(supportFragmentManager, "editDebtDialog")
    }

    override fun onDebtEdited(newAmount: String, newDate: String, position: Int, adapterType: String) {
        val list = if (adapterType == "toPay") toPayList else toReceiveList
        val adapter = if (adapterType == "toPay") toPayAdapter else toReceiveAdapter

        if (position in list.indices) {
            val debt = list[position]
            if (newAmount == "0" || newAmount.toDoubleOrNull() == 0.0) {
                list.removeAt(position)
                adapter.notifyItemRemoved(position)
                Toast.makeText(this, "Debt removed successfully!", Toast.LENGTH_SHORT).show()
            } else {
                debt.amount = newAmount
                if (newDate.isNotBlank()) {
                    debt.date = newDate
                }
                adapter.notifyItemChanged(position)
            }
        }
    }



}

