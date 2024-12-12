package com.example.coincraft

data class FinancialModel(
    var id: String? = null,
    val name: String = "",
    var saved: Double = 0.0,
    val target: Double = 0.0,
    var date: String = "", // Store date in yyyy-MM-dd format for consistency
    var state: Boolean = false, // Tracks whether the goal is completed
    val type: String = "" // Stores the type of goal
) {
    var remaining: Double = 0.0
        get() = target - saved

    var percentage: Int = 0
        get() = if (target != 0.0) ((saved / target) * 100).toInt() else 0

    fun checkCompletion() {
        if (percentage >= 100 && !state) {
            state = true
        }
    }
}
