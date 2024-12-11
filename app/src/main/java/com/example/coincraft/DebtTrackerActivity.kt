package com.example.coincraft

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.util.UUID

class DebtTrackerActivity : AppCompatActivity(), NewDebtDialogFragment.OnDebtAddedListener, EditDebtDialogFragment.OnDebtEditedListener {

    private lateinit var toReceiveRecyclerView: RecyclerView
    private lateinit var toPayRecyclerView: RecyclerView
    private lateinit var toReceiveAdapter: DebtCardAdapterL
    private lateinit var toPayAdapter: DebtCardAdapterS
    private lateinit var toReceiveList: ArrayList<DebtCardModel>
    private lateinit var toPayList: ArrayList<DebtCardModel>
    private lateinit var addDebtButton: ImageButton
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var firestore: FirebaseFirestore

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
        bottomNav = findViewById(R.id.botnav)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Setup RecyclerViews
        setupRecyclerView(toReceiveRecyclerView)
        setupRecyclerView(toPayRecyclerView)

        // Handle "Add Debt" button click
        addDebtButton.setOnClickListener {
            val dialog = NewDebtDialogFragment()
            dialog.show(supportFragmentManager, "NewDebtDialog")
        }

        // Fetch debts from Firestore
        fetchDebtsFromFirestore()

        // Bottom navigation setup
        bottomNav.setOnNavigationItemSelectedListener { item ->
            val itemId = item.itemId
            var intent: Intent? = null

            when (itemId) {
                R.id.navigation_home -> {

                    val intent = Intent(this@DebtTrackerActivity, Home::class.java)
                    startActivity(intent)
                }


                R.id.navigation_discover -> {
//                    intent = Intent(this@Home, Transaction::class.java)
//                    startActivity(intent)
                }

                R.id.navigation_likes -> {
//                    intent = Intent(this@Home, Budgeting::class.java)
//                    startActivity(intent)
                }

                R.id.navigation_account -> {
                    if (this@DebtTrackerActivity !is DebtTrackerActivity) {
                        intent = Intent(this@DebtTrackerActivity, DebtTrackerActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            val selectedItem = bottomNav.findViewById<View>(item.itemId)
            selectedItem?.animate()?.apply {
                scaleX(1.2f)
                scaleY(1.2f)
                duration = 100
                withEndAction {
                    selectedItem.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
            }?.start()

            true
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun fetchDebtsFromFirestore() {
        // Fetch debts categorized as "to receive"
        firestore.collection("debts")
            .whereEqualTo("state", "to receive")
            .get()
            .addOnSuccessListener { result ->
                toReceiveList = ArrayList()
                for (document in result) {
                    val debt = document.toObject(DebtCardModel::class.java)
                    toReceiveList.add(debt)
                }
                // Update the adapter after fetching data
                toReceiveAdapter = DebtCardAdapterL(this, toReceiveList) { selectedCard, position ->
                    openEditDebtDialog(selectedCard.name, selectedCard.amount, selectedCard.date, selectedCard.state, position, "toReceive")
                }
                toReceiveRecyclerView.adapter = toReceiveAdapter
            }

        // Fetch debts categorized as "to pay"
        firestore.collection("debts")
            .whereEqualTo("state", "to pay")
            .get()
            .addOnSuccessListener { result ->
                toPayList = ArrayList()
                for (document in result) {
                    val debt = document.toObject(DebtCardModel::class.java)
                    toPayList.add(debt)
                }
                // Update the adapter after fetching data
                toPayAdapter = DebtCardAdapterS(this, toPayList) { selectedCard, position ->
                    openEditDebtDialog(selectedCard.name, selectedCard.amount, selectedCard.date, selectedCard.state, position, "toPay")
                }
                toPayRecyclerView.adapter = toPayAdapter
            }
    }

    override fun onDebtAdded(amount: String, name: String, date: String, state: String) {
        val newDebt = DebtCardModel(
            id = UUID.randomUUID().toString(), // Temporarily generate a unique ID for local use
            profileImage = DEFAULT_PROFILE_IMAGE,
            name = name,
            date = date,
            coinImage = DEFAULT_COIN_IMAGE,
            amount = amount,
            state = state
        )

        // Add the new debt to the local list and update the adapter immediately
        if (state == "to receive") {
            toReceiveList.add(newDebt)
            toReceiveAdapter.notifyItemInserted(toReceiveList.size - 1)
            toReceiveRecyclerView.scrollToPosition(toReceiveList.size - 1)
        } else {
            toPayList.add(newDebt)
            toPayAdapter.notifyItemInserted(toPayList.size - 1)
            toPayRecyclerView.scrollToPosition(toPayList.size - 1)
        }

        // Add new debt to Firestore
        firestore.collection("debts").add(newDebt)
            .addOnSuccessListener { documentReference ->
                // Update the debt with the Firestore document ID after it's saved
                newDebt.id = documentReference.id

                // Optionally, update the debt in Firestore again if you need to store the ID
                firestore.collection("debts").document(newDebt.id).set(newDebt)
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error saving debt ID to Firestore: $exception", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                // If Firestore fails to add the debt, remove it from the local list
                if (state == "to receive") {
                    toReceiveList.removeAt(toReceiveList.size - 1)
                    toReceiveAdapter.notifyItemRemoved(toReceiveList.size)
                } else {
                    toPayList.removeAt(toPayList.size - 1)
                    toPayAdapter.notifyItemRemoved(toPayList.size)
                }
                Toast.makeText(this, "Error adding debt: $exception", Toast.LENGTH_SHORT).show()
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
