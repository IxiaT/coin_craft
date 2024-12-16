package com.example.coincraft

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BudgetingMainFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId: String
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var budgetAdapter: BudgetAdapter

    private lateinit var NeedsRV: RecyclerView
    private lateinit var WantsRV: RecyclerView
    private var month = 11
    private var year = 0
    private lateinit var needsLimit: TextView
    private lateinit var wantsLimit: TextView
    private lateinit var totalBudgetTxt: TextView
    private lateinit var totalExpenseTxt: TextView
    private lateinit var budgetRemaining: TextView

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
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        arguments?.let {
            month = it.getInt("month")
            year = it.getInt("year")
        }

        addNeedButton = view.findViewById(R.id.need_add_btn)
        addWantButton = view.findViewById(R.id.want_add_btn)
        needsLimit = view.findViewById(R.id.need_limit)
        wantsLimit = view.findViewById(R.id.wants_limit)
        totalBudgetTxt = view.findViewById(R.id.totalbudgettxt)
        totalExpenseTxt = view.findViewById(R.id.totalexpensestxt)
        budgetRemaining = view.findViewById(R.id.budgetremainingtxt)

        addNeedButton.setOnClickListener {
            val needDialogFragment = AddNeedCategoryDialogFragment()
            //needDialogFragment.listener = this
            needDialogFragment.show(childFragmentManager, "addNeedCategoryDialog")
        }

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
        var totalNeedsBudget = 0.0
        var totalWantsBudget = 0.0
        var totalBudget = 0.0

        var needsFetched = false
        var wantsFetched = false

        budgetViewModel.getBudgetNeeds(userId) { needs, error ->
            if (error != null) {
                // Handle error
                println("Error: $error")
            } else {
                // Use the lists
                println("Needs: $needs")

                budgetAdapter = BudgetAdapter(needs, childFragmentManager)
                NeedsRV = view.findViewById(R.id.needs_rv)
                NeedsRV.layoutManager = LinearLayoutManager(context)
                NeedsRV.adapter = budgetAdapter

                totalNeedsBudget = needs.sumOf { it.budgetLimit }
                needsLimit.text = totalNeedsBudget.toString()

                needsFetched = true
                if (needsFetched && wantsFetched) {
                    totalBudget = totalNeedsBudget + totalWantsBudget
                    totalBudgetTxt.text = totalBudget.toString()
                }
            }
        }

        budgetViewModel.getBudgetWants(userId) { wants, error ->
            if (error != null) {
                println("Error: $error")
            } else {
                println("Wants: $wants")
                budgetAdapter = BudgetAdapter(wants, childFragmentManager)
                WantsRV = view.findViewById(R.id.wantsrecyclerview)
                WantsRV.adapter = budgetAdapter
                WantsRV.layoutManager = LinearLayoutManager(context)

                totalWantsBudget = wants.sumOf { it.budgetLimit }
                wantsLimit.text = totalWantsBudget.toString()

                wantsFetched = true
                if (needsFetched && wantsFetched) {
                    totalBudget = totalNeedsBudget + totalWantsBudget
                    totalBudgetTxt.text = totalBudget.toString()

                    expenseViewModel.getExpensesByMonth(userId, month) { expense, error ->
                        if (error != null) {
                            println("Error: $error")
                        } else {
                            val totalExpense = expense.sumOf { it.amount }
                            totalExpenseTxt.text = totalExpense.toString()

                            val budgetRemain = totalBudget - totalExpense
                            budgetRemaining.text = budgetRemain.toString()
                        }
                    }
                }
            }

        }
    }
}