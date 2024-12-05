package com.example.fpmobdev;

public class Budget {
    public String category;
    public double amount;
    public double remaining;
    public double alertThreshold;

    public Budget() {}

    public Budget(String category, double amount, double remaining, double alertThreshold) {
        this.category = category;
        this.amount = amount;
        this.remaining = remaining;
        this.alertThreshold = alertThreshold;
    }
}