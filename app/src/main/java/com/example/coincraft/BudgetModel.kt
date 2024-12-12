package com.example.coincraft

class BudgetModel (
    var budgetType: String = "",
    var budgetCategory: String = "",
    var budgetLimit: Double = 0.00
) {
    constructor() : this( "", "",0.00)
}