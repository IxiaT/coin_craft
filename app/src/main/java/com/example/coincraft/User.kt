package com.example.coincraft

class User (
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var hp: Int = 100,
    var xp: Int = 100
) {
    constructor() : this("", "", "", 100, 100)

    fun retrieveEmail(): String {
        return email
    }

    fun retrieveUsername(): String {
        return username
    }

    fun retrievePassword(): String {
        return password
    }

    fun  retrieveHP(): Int {
        return hp
    }

    fun retrieveXP(): Int {
        return xp
    }
}