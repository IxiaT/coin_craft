package com.example.coincraft

class User(
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var hp: Double = 0.0,
    var xp: Double = 0.0
) {
    // Default constructor for Firebase
    constructor() : this("", "", "",)

    fun retrieveEmail(): String {
        return email
    }

    fun retrieveUsername(): String {
        return username
    }

    fun retrievePassword(): String {
        return password
    }
}
