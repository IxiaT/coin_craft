package com.example.fpmobdev;

public class Goal {
    public String goalName;
    public double targetAmount;
    public double savedAmount;
    public String targetDate;

    public Goal() {}

    public Goal(String goalName, double targetAmount, double savedAmount, String targetDate) {
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.targetDate = targetDate;
    }
}