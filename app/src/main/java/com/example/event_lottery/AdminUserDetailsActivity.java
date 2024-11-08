package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminUserDetailsActivity extends AppCompatActivity {
    private TextView displayUsername, displayEmail, usernameValue, fullNameValue, emailValue, phoneValue, roleValue;
    private FirebaseFirestore db;
    private String userId; // storing the user ID for deletion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_details_page);

        // initializing Firebase
        db = FirebaseFirestore.getInstance();

        // initializing the UI elements
        displayUsername = findViewById(R.id.display_username);
        displayEmail = findViewById(R.id.display_email);
        usernameValue = findViewById(R.id.username_value);
        fullNameValue = findViewById(R.id.full_name_value);
        emailValue = findViewById(R.id.email_value);
        roleValue = findViewById(R.id.role_value);

        // user data
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        String email = intent.getStringExtra("email");
        String username = intent.getStringExtra("username");
        String fullName = intent.getStringExtra("fullName");
        String role = intent.getStringExtra("role");

        // Display user data, with userId at the top
        displayUsername.setText(userId);
        displayEmail.setText(email);
        usernameValue.setText(username);
        fullNameValue.setText(fullName);
        emailValue.setText(email);
        roleValue.setText(role);

        // Back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        //confirmation dialog
        Button removeProfileButton = findViewById(R.id.remove_profile_button);
        removeProfileButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    // showing the confirmation dialog before deleting the user
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Profile")
                .setMessage("Are you sure you want to delete this profile?")
                .setPositiveButton("Yes", (dialog, which) -> deleteUserProfile())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // to delete the user profile
    private void deleteUserProfile() {
        if (userId != null && !userId.isEmpty()) {
            db.collection("users").document(userId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AdminUserDetailsActivity.this, "Profile deleted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity after deletion
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AdminUserDetailsActivity.this, "Error deleting profile", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User ID is missing, cannot delete profile.", Toast.LENGTH_SHORT).show();
        }
    }
}
