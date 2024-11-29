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
    private Button logoutButton;
    private Button waitingListButton;
    private Button notificationButton;
    private Button editProfileButton;
    private Button qrCodeButton;
    private String userId;
    private ImageButton backButton;

    private ImageView profileIcon;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_dashboard);

        // Initialize buttons
        waitingListButton = findViewById(R.id.waiting_list);
        notificationButton = findViewById(R.id.notification);
        editProfileButton = findViewById(R.id.edit_profile);
        qrCodeButton = findViewById(R.id.qr_code);

        profileIcon = findViewById(R.id.profile_icon);
        backButton = findViewById(R.id.backButton);

        // Check if profileIcon is null
        if (profileIcon == null) {
            Log.e(TAG, "profileIcon is null. Check the ID in your layout file.");
        } else {
            Log.d(TAG, "profileIcon initialized successfully.");
        }

        // Retrieve user ID (email) from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", null);
        String userRole = sharedPreferences.getString("USER_ROLE", null);

        if (userRole == null || !userRole.equals("Entrant")) {
            Toast.makeText(DashboardActivity.this, "Access denied. You are not authorized to access this dashboard.", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }


        if (userId == null || userId.isEmpty()) {
            Toast.makeText(DashboardActivity.this, "No user ID found. Please log in again.", Toast.LENGTH_SHORT).show();
            // Redirect to login activity
            Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Load profile image
        loadProfileImage();

        // Back button
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // For event activity
        waitingListButton.setOnClickListener(v -> {

            // Start the EventListActivity
            Intent waitingListIntent = new Intent(DashboardActivity.this, EntrantUserWaitingListActivity.class);
            startActivity(waitingListIntent);
        });

        // Notification settings
        notificationButton.setOnClickListener(v -> {

            // Start the NotificationSettingsActivity
            Intent intent = new Intent(DashboardActivity.this, NotificationSettingsActivity.class);
            startActivity(intent);
        });

        // Edit Profile
        editProfileButton.setOnClickListener(v -> {

            // Start the EditProfileActivity
            Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // QR Code Scanner
        qrCodeButton.setOnClickListener(v -> {

            Log.d(TAG, "QR Code button clicked.");

            // Start the QRCodeScannerActivity
            Intent intent = new Intent(DashboardActivity.this, QRCodeScannerActivity.class);
            startActivity(intent);
        });
    }
    private void redirectToLogin() {
        Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload profile image when activity resumes
        loadProfileImage();
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
                            profileIcon.setImageResource(R.drawable.ic_image_placeholder); // Use default image
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(DashboardActivity.this, "Failed to load profile image", Toast.LENGTH_SHORT).show());
    }
}
