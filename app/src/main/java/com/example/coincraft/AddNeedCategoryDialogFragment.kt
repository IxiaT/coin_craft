package com.example.coincraft

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner

class AddNeedCategoryDialogFragment: DialogFragment() {

    private lateinit var closeBtn: ImageButton
    private lateinit var addBtn: ImageButton
    private lateinit var categorySpinner: Spinner
    private lateinit var amountLimit: EditText

    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId: String

//    interface OnNeedCategoryAddedListener {
//        fun onNeedCategoryAdded(categoryName: String)
//    }
//
//    private var listener: OnNeedCategoryAddedListener? = null
//
//    fun setOnNeedCategoryAddedListener(listener: OnNeedCategoryAddedListener) {
//        this.listener = listener
//    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_need_category_dialog, container, false)

        sharedPreferences = requireContext().getSharedPreferences("my_pref", MODE_PRIVATE)
        userId = sharedPreferences.getString("User", null).toString()
        budgetViewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        closeBtn = view.findViewById(R.id.closebtn)
        addBtn = view.findViewById(R.id.add_button)
        categorySpinner = view.findViewById(R.id.category_spinner)
        amountLimit = view.findViewById(R.id.txtamount)

        loadSpinnerArray(R.array.expense_categories)

        closeBtn.setOnClickListener {
            dismiss()
        }

        addBtn.setOnClickListener {
            if(amountLimit.text.isEmpty()){
                amountLimit.error = "Input amount"
            } else {
                saveBudget()
            }
        }

        return view
    }

    private fun saveBudget() {
        val amountText = amountLimit.text.toString()
        val category = categorySpinner.selectedItem.toString()
        val type = "Needs"

        if (amountText.isBlank() || category.isBlank()) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val budget = BudgetModel(
            budgetCategory = category,
            budgetLimit = amount
        )

        budgetViewModel.addBudgetNeeds(userId, budget) { success, error ->
            if (success) {
                Toast.makeText(requireContext(), "Budget added successfully", Toast.LENGTH_SHORT)
                    .show()
                budgetViewModel.transactionUpdated.postValue(true)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Failed to add budget: $error", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun loadSpinnerArray(arrayResId: Int) {
        // Get the appropriate array (income or expense)
        val categories = resources.getStringArray(arrayResId)

        // Set up the spinner adapter with the new array
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_dropdown_item,
            categories
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

}