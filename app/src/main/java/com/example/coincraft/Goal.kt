package com.example.coincraft

class Goal(
    var goalName: String = "",
    var targetAmount: Double = 0.0,
    var savedAmount: Double = 0.0,
    var targetDate: String = ""
) {
    // Default constructor for Firebase
    constructor() : this("", 0.0, 0.0, "")
}