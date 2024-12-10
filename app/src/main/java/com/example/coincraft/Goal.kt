package com.example.coincraft

import java.text.SimpleDateFormat
import java.util.*

data class Goal(
    val name: String,
    val icon: Int,
    var saved: Double,
    val target: Double,
    var date: String // Store date in yyyy-MM-dd format for consistency
) {
    var remaining: Double = 0.0
        get() = target - saved

    var percentage: Int = 0
        get() = ((saved / target) * 100).toInt()

    // Method to get the date in the format "MMMM dd, yyyy" for RecyclerView display
    fun getFormattedDateForDisplay(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val parsedDate: Date = inputFormat.parse(date) ?: Date() // Default to current date if parsing fails
        return outputFormat.format(parsedDate)
    }

    // Method to get the date in the format "MM/dd/yyyy" for Dialog
    fun getFormattedDateForDialog(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val parsedDate: Date = inputFormat.parse(date) ?: Date() // Default to current date if parsing fails
        return outputFormat.format(parsedDate)
    }

}
