package com.example.coincraft
import java.io.Serializable

class TransactionModel (
    var id: String = "",
    var amount: Double = 0.00,
    var category: String = "Uncategorized",
    var note: String = "No Notes",
    var date: String = ""
) : Serializable {
    constructor() : this( "",0.00, "Miscellaneous", "No Notes", "")
}

