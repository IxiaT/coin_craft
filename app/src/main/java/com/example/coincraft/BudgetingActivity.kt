package com.example.coincraft

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coincraft.databinding.ActivityBudgetingBinding

class BudgetingActivity : AppCompatActivity() {

    //private lateinit var sharedPreferences: SharedPreferences
    private lateinit var topbackbtn: ImageButton
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityBudgetingBinding
    val budgetFragment = BudgetingMainFragment()


    private lateinit var editTotalBudget: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBudgetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goToFragment(budgetFragment)

        topbackbtn = findViewById(R.id.backbtn)
        topbackbtn.setOnClickListener {
            val homeAct = Intent(this, Home::class.java)
            startActivity(homeAct)
        }

    }

    private fun goToFragment(fragment: Fragment){
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.budgetmainfragment, fragment).commit()
    }


}