package com.example.coincraft

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DebtTrackerActivity : AppCompatActivity(), NewDebtDialogFragment.OnDebtAddedListener, EditDebtDialogFragment.OnDebtEditedListener {

    private lateinit var toReceiveRecyclerView: RecyclerView
    private lateinit var toPayRecyclerView: RecyclerView
    private lateinit var toReceiveAdapter: DebtCardAdapterL
    private lateinit var toPayAdapter: DebtCardAdapterS
    private val toReceiveList = ArrayList<DebtCardModel>()
    private val toPayList = ArrayList<DebtCardModel>()
    private lateinit var addDebtButton: ImageButton
    private lateinit var database: DatabaseReference
    private lateinit var bottomNav: BottomNavigationView

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

        // Setup RecyclerViews
        setupRecyclerView(toReceiveRecyclerView)
        setupRecyclerView(toPayRecyclerView)

        // Firebase reference initialization
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            database = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Debts")
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        bottomNav.setOnNavigationItemSelectedListener { item ->
            val itemId = item.itemId
            var intent: Intent? = null

            when (itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this@DebtTrackerActivity, Home::class.java)
                    startActivity(intent)
                }
                R.id.navigation_transaction -> {
//                    intent = Intent(this@Home, Transaction::class.java)
//                    startActivity(intent)
                }
                R.id.navigation_budgeting -> {
//                    intent = Intent(this@Home, Budgeting::class.java)
//                    startActivity(intent)
                }
                R.id.navigation_debt -> {


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

        // Setup adapters
        toReceiveAdapter = DebtCardAdapterL(this, toReceiveList) { selectedCard, position ->
            openEditDebtDialog(selectedCard.name, selectedCard.amount, selectedCard.date, selectedCard.state, position, "toReceive")
        }

        toPayAdapter = DebtCardAdapterS(this, toPayList) { selectedCard, position ->
            openEditDebtDialog(selectedCard.name, selectedCard.amount, selectedCard.date, selectedCard.state, position, "toPay")
        }

        // Set the adapters
        toReceiveRecyclerView.adapter = toReceiveAdapter
        toPayRecyclerView.adapter = toPayAdapter

        // Load data from Firebase
        loadDataFromFirebase()

        // Handle "Add Debt" button click
        addDebtButton.setOnClickListener {
            val dialog = NewDebtDialogFragment()
            dialog.show(supportFragmentManager, "NewDebtDialog")
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadDataFromFirebase() {
        // Load 'toReceive' debts
        database.child("toReceive").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toReceiveList.clear() // Clear the list before adding items
                for (debtSnapshot in snapshot.children) {
                    val debt = debtSnapshot.getValue(DebtCardModel::class.java)
                    debt?.let {
                        it.id = debtSnapshot.key.toString()
                        toReceiveList.add(it)
                    }
                }
                toReceiveAdapter.notifyDataSetChanged() // Notify the adapter once
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DebtTrackerActivity, "Failed to load debts.", Toast.LENGTH_SHORT).show()
            }
        })

        // Load 'toPay' debts
        database.child("toPay").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toPayList.clear() // Clear the list before adding items
                for (debtSnapshot in snapshot.children) {
                    val debt = debtSnapshot.getValue(DebtCardModel::class.java)
                    debt?.let {
                        it.id = debtSnapshot.key.toString()
                        toPayList.add(it)
                    }
                }
                toPayAdapter.notifyDataSetChanged() // Notify the adapter once
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DebtTrackerActivity, "Failed to load debts.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDebtAdded(amount: String, name: String, date: String, state: String) {
        val category = if (state == "to receive") "toReceive" else "toPay"
        val debtReference = database.child(category).push() // Generates a unique ID
        val debtId = debtReference.key ?: return // Ensure the unique key is valid

        val newDebt = DebtCardModel(
            id = debtId, // Use the unique debtId as the ID
            profileImage = DEFAULT_PROFILE_IMAGE,
            name = name,
            date = date,
            coinImage = DEFAULT_COIN_IMAGE,
            amount = amount,
            state = state
        )

        debtReference.setValue(newDebt).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Debt added successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to add debt.", Toast.LENGTH_SHORT).show()
            }
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
        val category = if (adapterType == "toPay") "toPay" else "toReceive"

        if (position in list.indices) {
            val debt = list[position]

            if (newAmount == "0" || newAmount.toDoubleOrNull() == 0.0) {
                database.child(category).child(debt.id).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        list.removeAt(position)
                        if (adapterType == "toPay") toPayAdapter.notifyItemRemoved(position)
                        else toReceiveAdapter.notifyItemRemoved(position)
                        Toast.makeText(this, "Debt removed successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                debt.amount = newAmount
                if (newDate.isNotEmpty()) {
                    debt.date = newDate
                }
                database.child(category).child(debt.id).setValue(debt).addOnCompleteListener {
                    if (adapterType == "toPay") toPayAdapter.notifyItemChanged(position)
                    else toReceiveAdapter.notifyItemChanged(position)
                    Toast.makeText(this, "Debt updated successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
