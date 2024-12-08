package com.example.coincraft

class Income(
    var source: String = "",
    var amount: Double = 0.0,
    var date: String = ""
) {
    // Default constructor for Firebase
    constructor() : this("", 0.0, "")
}