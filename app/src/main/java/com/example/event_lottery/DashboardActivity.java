package com.example.event_lottery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";

    private Button waitingListButton;
    private Button notificationSettingsButton;
    private Button editProfileButton;
    private Button qrCodeButton;
    private ImageButton backButton;
    private ImageButton notificationIconButton;
    private ImageView profileIcon;
    private String userId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_dashboard);

        // Initialize UI elements
        waitingListButton = findViewById(R.id.waiting_list);
        notificationSettingsButton = findViewById(R.id.notification);
        editProfileButton = findViewById(R.id.edit_profile);
        qrCodeButton = findViewById(R.id.qr_code);
        backButton = findViewById(R.id.backButton);
        notificationIconButton = findViewById(R.id.notification_button);
        profileIcon = findViewById(R.id.profile_icon);

        // Retrieve user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", null);
        String userRole = sharedPreferences.getString("USER_ROLE", null);

        if (userRole == null || !userRole.equals("Entrant")) {
            Toast.makeText(this, "Access denied. You are not authorized to access this dashboard.", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "No user ID found. Please log in again.", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Load profile image
        loadProfileImage();

        // Set up button click listeners
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        waitingListButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EntrantUserWaitingListActivity.class);
            startActivity(intent);
        });

        notificationSettingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, NotificationSettingsActivity.class);
            startActivity(intent);
        });

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        qrCodeButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, QRCodeScannerActivity.class);
            startActivity(intent);
        });

        notificationIconButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, NotificationListActivity.class);
            startActivity(intent);
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadProfileImage() {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(profileImageUrl)
                                    .circleCrop()
                                    .into(profileIcon);
                        } else {
                            profileIcon.setImageResource(R.drawable.ic_image_placeholder);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(DashboardActivity.this, "Failed to load profile image", Toast.LENGTH_SHORT).show()
                );
    }
}
