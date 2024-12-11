package com.example.coincraft

import java.text.SimpleDateFormat
import java.util.*

data class FinancialModel(
    var key: String? = null,
    val name: String = "",
    val icon: Int = 0,
    var saved: Double = 0.0,
    val target: Double = 0.0,
    var date: String = "" // Store date in yyyy-MM-dd format for consistency
) {
    var remaining: Double = 0.0
        get() = target - saved

    var percentage: Int = 0
        get() = if (target != 0.0) ((saved / target) * 100).toInt() else 0

    fun getFormattedDateForDisplay(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val parsedDate: Date = inputFormat.parse(date) ?: Date() // Default to current date if parsing fails
        return outputFormat.format(parsedDate)
    }

    fun getFormattedDateForDialog(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val parsedDate: Date = inputFormat.parse(date) ?: Date() // Default to current date if parsing fails
        return outputFormat.format(parsedDate)
    }
}