package com.example.coincraft

class FinancialModel (
    var goalName: String = "",
    var goalType: String = "",
    var amount: Double = 0.00,
    val currentProgress: Double = 0.00,
    var date: String = ""
){
    constructor() : this("", "", 0.00, 0.00, "")
}