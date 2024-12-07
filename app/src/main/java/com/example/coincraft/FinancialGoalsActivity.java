package com.example.coincraft;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class FinancialGoalsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_goals);

        CircularProgressIndicator progressIndicator = findViewById(R.id.financialGoalProgress);
        TextView progressText = findViewById(R.id.progressPercentage);

        // Example financial data
        int currentSavings = 42500; // User's current savings
        int goalAmount = 50000;    // Target financial goal

        // Calculate progress percentage
        int progress = (int) ((currentSavings / (float) goalAmount) * 100);

        // Update progress bar and text
        progressIndicator.setProgressCompat(progress, true);
        progressText.setText(progress + "%");
    }
}