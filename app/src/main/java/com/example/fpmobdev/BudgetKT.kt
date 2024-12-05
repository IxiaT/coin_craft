package com.example.fpmobdev

data class BudgetKT(
    var category: String = "",
    var amount: Double = 0.0,
    var remaining: Double = 0.0,
    var alertThreshold: Double = 0.0
)
