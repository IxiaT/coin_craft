package com.example.coincraft

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BudgetingActivity : AppCompatActivity() {

    private lateinit var editTotalBudget: ImageButton

    private lateinit var NeedsRecyclerView: RecyclerView
    private lateinit var NeedsDataList: ArrayList<NeedWantDataClass>
    lateinit var NeedsCategory: Array<String>

    private lateinit var WantsRecyclerView: RecyclerView
    private lateinit var WantsDataList: ArrayList<NeedWantDataClass>
    lateinit var WantsCategory: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budgeting)

        editTotalBudget = findViewById(R.id.bseditbtn)


        NeedsCategory = arrayOf(
            "Education", "Food", "Groceries", "Laundry", "Medical", "Payment", "Phone", "Rent", "Tax", "Transportation", "Utilities"
        )

        NeedsRecyclerView = findViewById(R.id.needsrecyclerview)
        NeedsRecyclerView.layoutManager = LinearLayoutManager(this)
        NeedsRecyclerView.setHasFixedSize(true)

        NeedsDataList = arrayListOf<NeedWantDataClass>()
        getNeedsData()


        WantsCategory = arrayOf(
            "Cable", "Collectibles", "Entertainment", "Games", "Online Shopping", "Loans Payment", "Shopping", "Subscriptions", "Travel", "Uncategorized"
        )

        WantsRecyclerView = findViewById(R.id.wantsrecyclerview)
        WantsRecyclerView.layoutManager = LinearLayoutManager(this)
        WantsRecyclerView.setHasFixedSize(true)

        WantsDataList = arrayListOf<NeedWantDataClass>()
        getWantsData()

    }

    private fun getNeedsData(){
        for (i in NeedsCategory.indices){
            val needs = NeedWantDataClass(NeedsCategory[i])
            NeedsDataList.add(needs)
        }

        NeedsRecyclerView.adapter = NeedsAdapter(NeedsDataList)
    }

    private fun getWantsData(){
        for(i in WantsCategory.indices){
            val wants = NeedWantDataClass(WantsCategory[i])
            WantsDataList.add(wants)
        }
        WantsRecyclerView.adapter = WantsAdapter(WantsDataList)
    }
}