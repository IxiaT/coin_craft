package com.example.coincraft

import java.util.Locale.Category

class ExpenseModel (
    var amount: Double = 0.00,
    var category: String = "Uncategorized",
    var expenseNote: String = "No Notes",
    var date: String = ""
)   {
    constructor() : this(0.00, "Miscellaneous", "No Notes", "")
}

