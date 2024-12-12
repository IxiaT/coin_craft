package com.example.coincraft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BudgetingMainFragment : Fragment() {

    private lateinit var NeedsRecyclerView: RecyclerView
    private lateinit var NeedsDataList: ArrayList<NeedWantDataClass>
    lateinit var NeedsCategory: Array<String>

    private lateinit var WantsRecyclerView: RecyclerView
    private lateinit var WantsDataList: ArrayList<NeedWantDataClass>
    lateinit var WantsCategory: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_budgeting_main, container, false)

        val addNeedCategoryButton = view.findViewById<ImageButton>(R.id.needaddbtn)
        addNeedCategoryButton.setOnClickListener {
            val needDialogFragment = AddNeedCategoryDialogFragment()
            needDialogFragment.show(childFragmentManager, "addNeedCategoryDialog")
        }

        val addWantCategoryButton = view.findViewById<ImageButton>(R.id.wantaddbtn)
        addWantCategoryButton.setOnClickListener{
            val wantDialogFragment = AddWantCategoryDialogFragment()
            wantDialogFragment.show(childFragmentManager, "addWantCategoryDialog")
        }

        NeedsCategory = arrayOf(
            "Education", "Food", "Groceries", "Laundry", "Medical", "Payment", "Phone", "Rent", "Tax", "Transportation", "Utilities"
        )

        NeedsRecyclerView = view.findViewById(R.id.needsrecyclerview)
        NeedsRecyclerView.layoutManager = LinearLayoutManager(context)
        NeedsRecyclerView.setHasFixedSize(true)

        NeedsDataList = arrayListOf<NeedWantDataClass>()
        getNeedsData()


        WantsCategory = arrayOf(
            "Cable", "Collectibles", "Entertainment", "Games", "Online Shopping", "Loans Payment", "Shopping", "Subscriptions", "Travel", "Uncategorized"
        )

        WantsRecyclerView = view.findViewById(R.id.wantsrecyclerview)
        WantsRecyclerView.layoutManager = LinearLayoutManager(context)
        WantsRecyclerView.setHasFixedSize(true)

        WantsDataList = arrayListOf<NeedWantDataClass>()
        getWantsData()

        return view
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