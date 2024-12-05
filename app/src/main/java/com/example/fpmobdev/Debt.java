package com.example.fpmobdev;

public class Debt {
    public String name;
    public double amount;
    public String date;
    public String type;

    public Debt() {}

    public Debt(String name, double amount, String date, String type) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }
}