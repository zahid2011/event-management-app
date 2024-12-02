package com.example.event_lottery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";

    private CardView waitingListCard;
    private CardView notificationSettingsCard;
    private CardView editProfileCard;
    private CardView scanQRCodeCard;
    private ImageButton backButton;
    private ImageView profileIcon;
    private String userId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_dashboard);

        // Initialize UI elements
        waitingListCard = findViewById(R.id.waiting_list_button);
        notificationSettingsCard = findViewById(R.id.notification_settings_button);
        editProfileCard = findViewById(R.id.view_edit_profile_button);
        scanQRCodeCard = findViewById(R.id.scan_qr_code_button);
        backButton = findViewById(R.id.backButton);
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

        waitingListCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EntrantUserWaitingListActivity.class);
            startActivity(intent);
        });

        notificationSettingsCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, NotificationSettingsActivity.class);
            startActivity(intent);
        });

        editProfileCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        scanQRCodeCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, QRCodeScannerActivity.class);
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
