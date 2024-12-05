package com.example.fpmobdev;

public class Expense {
    public String category;
    public double amount;
    public String date;

    public Expense() {} // Default constructor for Firebase

    public Expense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
}
