package com.example.coincraft

data class DebtCardModel(
    val profileImage: Int = R.drawable.avatar,
    var name: String = "",
    var date: String = "",
    val coinImage: Int = R.drawable.coin,
    var amount: String = "",
    var state: String = "",
    var id: String = ""
) {

    // Converts DebtCardModel to a Map for Firebase
    fun toMap(): Map<String, Any> {
        return mapOf(
            "profileImage" to profileImage,
            "name" to name,
            "date" to date,
            "coinImage" to coinImage,
            "amount" to amount,
            "state" to state,
            "id" to id
        )
    }

    // Static method to create a DebtCardModel from a Map
    companion object {
        fun fromMap(map: Map<String, Any>): DebtCardModel {
            return DebtCardModel(
                profileImage = map["profileImage"] as? Int ?: R.drawable.avatar,
                name = map["name"] as? String ?: "",
                date = map["date"] as? String ?: "",
                coinImage = map["coinImage"] as? Int ?: R.drawable.coin,
                amount = map["amount"] as? String ?: "",
                state = map["state"] as? String ?: "",
                id = map["id"] as? String ?: ""
            )
        }
    }
}