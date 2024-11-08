package com.example.event_lottery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText emailEditText, usernameEditText, firstNameEditText, lastNameEditText, passwordEditText, phoneNumberEditText;
    private Button saveChangesButton, cancelButton;
    private ImageButton editImageButton, backButton;
    private ImageView profileImageView;
    private FirebaseFirestore db;
    private String userId; // Use email as the user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        // Retrieve the user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", null);

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "No user ID found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        saveChangesButton = findViewById(R.id.save_changes);
        cancelButton = findViewById(R.id.cancel_button);
        editImageButton = findViewById(R.id.edit_image);
        backButton = findViewById(R.id.backButton);
        profileImageView = findViewById(R.id.profileImage);

        // Fetch user data from Firestore
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Populate UI elements with user data
                            emailEditText.setText(document.getString("email"));
                            usernameEditText.setText(document.getString("username"));
                            firstNameEditText.setText(document.getString("firstName"));
                            lastNameEditText.setText(document.getString("lastName"));
                            phoneNumberEditText.setText(document.getString("phoneNumber"));
                            // TODO: Load profile image if stored
                        } else {
                            Toast.makeText(EditProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Handle "Save Changes" button click
        saveChangesButton.setOnClickListener(v -> updateProfile());

        // Handle "Cancel" button click
        cancelButton.setOnClickListener(v -> finish());

        // Handle "Edit Image" button click
        editImageButton.setOnClickListener(v -> {
            Toast.makeText(EditProfileActivity.this, "Edit profile image feature coming soon", Toast.LENGTH_SHORT).show();
        });

        // Handle "Back" button click
        backButton.setOnClickListener(v -> finish());
    }

    private void updateProfile() {
        String email = emailEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Map to hold the updated data
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", email);
        updates.put("username", username);
        updates.put("firstName", firstName);
        updates.put("lastName", lastName);
        updates.put("phoneNumber", phoneNumber);

        if (!TextUtils.isEmpty(password)) {
            updates.put("password", password);
        }

        // Update the document in Firestore
        db.collection("users").document(userId).update(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
