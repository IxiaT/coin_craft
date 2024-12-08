package com.example.coincraft

data class Goal(
    val name: String,
    val icon: Int,
    var saved: Double,
    val target: Double
) {
    var percentage: Int = 0
        get() = ((saved / target) * 100).toInt()

    var remaining: Double = 0.0
        get() = target - saved
}
