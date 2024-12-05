package com.example.fpmobdev;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Added for logging
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText email;
    private EditText username;
    private EditText password;
    private EditText retryPassword;
    private ImageView RegisterButton;

    private static final String TAG = "RegisterActivity"; // Added for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EdgeToEdge.enable(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://fp-mobdev-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserAccounts");
        Log.d(TAG, "Database reference initialized: " + databaseReference.toString());

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        retryPassword = findViewById(R.id.retryPassword);
        username = findViewById(R.id.userName);
        RegisterButton = findViewById(R.id.registerButton);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals(retryPassword.getText().toString())) {
                    if (isValidPassword(password.getText().toString().trim())) {
                        registerNewUser();
                    } else {
                        Toast.makeText(getApplicationContext(), "Password is Invalid", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void registerNewUser() {
        String user, passP, mail;

        mail = email.getText().toString();
        passP = password.getText().toString();
        user = username.getText().toString();

        if (TextUtils.isEmpty(mail)) {
            Toast.makeText(getApplicationContext(), "Please enter email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(user)) {
            Toast.makeText(getApplicationContext(), "Please enter Name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(passP)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(mail, passP).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User created successfully with email: " + mail);
                    saveUserToDatabase(mail, user, passP);
                } else {
                    Log.e(TAG, "Register failed: ", task.getException());
                    Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserToDatabase(String email, String username, String password) {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            Log.d(TAG, "Attempting to save user with UID: " + userId);

            User user = new User(email, username, password);

            DatabaseReference userRef = databaseReference.child(userId);

            userRef.child("User").setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User saved successfully to database");

                    // Initialize default tables
                    initializeDefaultTables(userRef);

                    Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                    Intent register = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(register);
                    finish(); // Close current activity
                } else {
                    Log.e(TAG, "Failed to save user details to database", task.getException());
                    Toast.makeText(RegisterActivity.this, "Failed to save user details", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "User authentication failed; UID is null");
            Toast.makeText(RegisterActivity.this, "User authentication failed", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to initialize default tables
    private void initializeDefaultTables(DatabaseReference userRef) {
        // Default values for tables
        DatabaseReference expensesRef = userRef.child("Expenses");
        expensesRef.child("default").setValue(new Expense("Miscellaneous", 0.0, "2024-01-01"));

        DatabaseReference incomeRef = userRef.child("Income");
        incomeRef.child("default").setValue(new Income("Initial Income", 0.0, "2024-01-01"));

        DatabaseReference budgetsRef = userRef.child("Budgets");
        budgetsRef.child("default").setValue(new Budget("General", 0.0, 0.0, 0.0));

        DatabaseReference goalsRef = userRef.child("Goals");
        goalsRef.child("default").setValue(new Goal("New Goal", 0.0, 0.0, "2024-12-31"));

        DatabaseReference debtsRef = userRef.child("Debts");
        debtsRef.child("default").setValue(new Debt("No one", 0.0, "2024-01-01", "Receivable"));

        Log.d(TAG, "Default tables initialized");
    }

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
