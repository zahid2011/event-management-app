package com.example.event_lottery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    private Button logoutButton;
    private Button waitingListButton;
    private Button notificationButton;
    private Button editProfileButton;
    private Button qrCodeButton;
    private String userId;

    private Button viewEditProfileButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_dashboard);

            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            userId = sharedPreferences.getString("USER_ID", null);

            if (userId == null || userId.isEmpty()) {
                Toast.makeText(DashboardActivity.this, "No user ID found. Please log in again.", Toast.LENGTH_SHORT).show();
                // Redirect to login activity
                Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                return;
            }

        // Initialize buttons
        logoutButton = findViewById(R.id.logout);
        waitingListButton = findViewById(R.id.waiting_list);
        notificationButton = findViewById(R.id.notification);
        editProfileButton = findViewById(R.id.edit_profile);
        qrCodeButton = findViewById(R.id.qr_code);

        // Set logout button functionality
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to LoginActivity
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Placeholder click listeners for other buttons
        waitingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent waitingListIntent = new Intent(DashboardActivity.this, EventListActivity.class);
                startActivity(waitingListIntent);
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add functionality for notifications settings button
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add functionality for QR code button
            }
        });
    }
}
