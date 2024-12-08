package com.example.coincraft

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val hpBar = findViewById<ProgressBar>(R.id.hp_bar)
        val xpBar = findViewById<ProgressBar>(R.id.xp_bar)
        val earnedBar = findViewById<ProgressBar>(R.id.earned_bar)
        val spentBar = findViewById<ProgressBar>(R.id.spent_bar)
        val balance = findViewById<TextView>(R.id.balance_amount)
        val earnedAmount = findViewById<TextView>(R.id.earned_amount)
        val spentAmount = findViewById<TextView>(R.id.spent_amount)
        val plusBtn = findViewById<FloatingActionButton>(R.id.plus_button)
        val bottomNav = findViewById<BottomNavigationView>(R.id.botnav)



        hpBar.setProgress(10, true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}