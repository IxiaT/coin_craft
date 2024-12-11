package com.example.coincraft

class DebtModel (
    var amount: Double = 0.00,
    var transactionType: String = "",
    var name: String = "",
    var date: String = "",
    var partialPaid: Double = 0.00,
    var status: String = ""
){
    constructor(name: String, amount: String) : this(0.00, "", "No Notes", "", 0.00, "")
}