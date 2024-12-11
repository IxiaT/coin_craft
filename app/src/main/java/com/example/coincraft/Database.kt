package com.example.coincraft

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Database {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance("https://fp-mobdev-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserAccounts")

    init {
        Log.d(TAG,"Database reference initialized: $databaseReference")
    }

    fun getUserRef(userId: String): DatabaseReference{
        return databaseReference.child(userId)
    }

}