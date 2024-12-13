package com.example.coincraft

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BudgetingMainFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId: String
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var budgetAdapter: BudgetAdapter

    private lateinit var NeedsRV: RecyclerView
    private lateinit var WantsRV: RecyclerView
    private lateinit var month: String
    private lateinit var year: String

    lateinit var NeedsCategory: Array<String>
    private lateinit var addNeedButton: ImageButton
    private lateinit var addWantButton: ImageButton
    private lateinit var NeedsDataList: ArrayList<BudgetModel>
    private lateinit var WantsDataList: ArrayList<BudgetModel>
    lateinit var WantsCategory: Array<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_budgeting_main, container, false)

//        arguments?.let {
//            month = it.getString("month") as String
//            year = it.getSerializable("year") as String
//        }

        sharedPreferences = requireContext().getSharedPreferences("my_pref", MODE_PRIVATE)
        userId = sharedPreferences.getString("User", null).toString()
        budgetViewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        addNeedButton = view.findViewById(R.id.need_add_btn)
        addNeedButton.setOnClickListener {
            val needDialogFragment = AddNeedCategoryDialogFragment()
            //needDialogFragment.listener = this
            needDialogFragment.show(childFragmentManager, "addNeedCategoryDialog")
        }

        addWantButton = view.findViewById(R.id.want_add_btn)
        addWantButton.setOnClickListener{
            val wantDialogFragment = AddWantCategoryDialogFragment()
            wantDialogFragment.show(childFragmentManager, "addWantCategoryDialog")
        }

        fetchBudgetData(view)

        budgetViewModel.transactionUpdated.observe(requireActivity()) { updated ->
            if (updated) {
                fetchBudgetData(view) // Reload your RecyclerView or data
                budgetViewModel.transactionUpdated.postValue(false) // Reset state
            }
        }

        return view
    }

    /*
    override fun onNeedCategoryAdded(categoryName: String) {
        val newNeed = NeedWantDataClass(categoryName)
        NeedsDataList.add(newNeed)
        needsAdapter.notifyItemInserted(NeedsDataList.size - 1)
    }
     */

    private fun fetchBudgetData(view: View) {
        budgetViewModel.getBudgets(userId) { wants, needs, error ->
            if (error != null) {
                // Handle error
                println("Error: $error")
            } else {
                // Use the lists
                println("Needs: $needs")
                println("Wants: $wants")
                budgetAdapter = BudgetAdapter(needs, childFragmentManager)
                NeedsRV = view.findViewById(R.id.needsrecyclerview)
                NeedsRV.layoutManager = LinearLayoutManager(context)
                NeedsRV.adapter = budgetAdapter

                budgetAdapter = BudgetAdapter(wants, childFragmentManager)
                WantsRV = view.findViewById(R.id.wantsrecyclerview)
                WantsRV.layoutManager = LinearLayoutManager(context)
//                bundle.putString("month", currentMonth.toString())
//                bundle.putString("year", currentYear.toString())
//                bundle.putSerializable("wants", ArrayList(wants))
//                bundle.putSerializable("needs", ArrayList(needs))
//                budgetFragment.arguments = bundle
            }
        }
    }

}