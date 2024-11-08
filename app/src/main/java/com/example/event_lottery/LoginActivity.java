package com.example.event_lottery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Spinner roleSpinner;
    private Button signInButton;
    private ImageButton backButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        backButton = findViewById(R.id.backButton2);
        roleSpinner = findViewById(R.id.roleSpinner);

        // Set up the back button functionality
        backButton.setOnClickListener(v -> finish()); // Closes the current activity and returns to the previous one

        // Set up the Spinner with the role options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.role_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String selectedRole = roleSpinner.getSelectedItem().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Directly query Firestore to validate the user's credentials
            validateUserCredentials(email, password, selectedRole);
        });
    }

    private void validateUserCredentials(String email, String password, String selectedRole) {
        db.collection("users").document(email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String storedPassword = document.getString("password");
                            String registeredRole = document.getString("role");
                            String userId = document.getString("id");  // Retrieve the `id` field

                            if (storedPassword != null && storedPassword.equals(password)) {
                                if (registeredRole != null && registeredRole.equals(selectedRole)) {
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                    // Save the unique user `id` in SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("USER_ID", userId);  // Store `id` instead of email
                                    editor.apply();

                                    // Redirect based on role
                                    Intent dashboardIntent;
                                    if (selectedRole.equals("Entrant")) {
                                        dashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                                    } else if (selectedRole.equals("Organiser")) {
                                        dashboardIntent = new Intent(LoginActivity.this, OrganizerDashboardActivity.class);
                                    } else if (selectedRole.equals("Admin")) {
                                        dashboardIntent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Invalid role selected.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    startActivity(dashboardIntent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid credentials or role mismatch.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid credentials or role mismatch.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to access database: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}