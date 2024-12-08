package com.example.coincraft

class Budget(
    var category: String = "",
    var amount: Double = 0.0,
    var remaining: Double = 0.0,
    var alertThreshold: Double = 0.0
) {
    constructor() : this("", 0.0, 0.0, 0.0) // Default constructor for Firebase
}
