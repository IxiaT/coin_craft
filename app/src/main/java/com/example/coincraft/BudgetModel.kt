package com.example.coincraft

import java.io.Serializable

class BudgetModel (
    var budgetId: String = "",
    var budgetCategory: String = "",
    var budgetLimit: Double = 0.00
): Serializable {
    constructor() : this( "", "",0.00)
}