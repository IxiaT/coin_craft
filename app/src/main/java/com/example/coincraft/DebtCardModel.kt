package com.example.coincraft

import java.util.UUID

data class DebtCardModel(
    var id: String = "",
    var profileImage: Int = 0,
    var name: String = "",
    var date: String = "",
    var coinImage: Int = 0,
    var amount: String = "",
    var state: String = ""
) {
    // No-argument constructor for Firebase deserialization
    constructor() : this("", 0, "", "", 0, "", "")
}