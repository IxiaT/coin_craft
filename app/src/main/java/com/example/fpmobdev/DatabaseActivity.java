package com.example.fpmobdev;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");


        HashMap<String, Object> user1 = new HashMap<>();
        user1.put("email", "user1@example.com");
        user1.put("username", "user1");
        user1.put("password", "password1");

        usersRef.child("user1").setValue(user1)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "User added successfully!"))
                .addOnFailureListener(e -> Log.e("Firebase", "Error adding user", e));
    }
}
