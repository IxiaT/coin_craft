package com.example.fpmobdev;

public class Income {
    public String source;
    public double amount;
    public String date;

    public Income() {}

    public Income(String source, double amount, String date) {
        this.source = source;
        this.amount = amount;
    }
}