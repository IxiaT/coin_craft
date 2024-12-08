package com.example.coincraft

data class Goal(
    val name: String,
    val icon: Int,
    val saved: Double,
    val target: Double
) {
    val percentage: Int
        get() = ((saved / target) * 100).toInt()

    val remaining: Double
        get() = target - saved
}
