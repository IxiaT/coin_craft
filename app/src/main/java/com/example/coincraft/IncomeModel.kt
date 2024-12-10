package com.example.coincraft

class IncomeModel (
    var amount: Double = 0.00,
    var category: String = "Uncategorized",
    var incomeNote: String = "No Notes",
    var date: String = ""
){
    constructor() : this(0.00, "Uncategorized", "No Notes", "")
}