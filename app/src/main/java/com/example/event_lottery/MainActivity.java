package com.example.event_lottery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView profileButton, notificationButton, joinEventButton, createEventButton,
            createFacilityButton, entrantDashboardButton, adminDashboardButton,
            organizerDashboardButton;

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the dashboard layout as the main content view
        setContentView(R.layout.sample_dashboard);

        // Initialize buttons
        profileButton = findViewById(R.id.profile_icon);
        notificationButton = findViewById(R.id.notification_icon);
        joinEventButton = findViewById(R.id.join_event_icon);
        createEventButton = findViewById(R.id.create_event_icon);
        createFacilityButton = findViewById(R.id.create_facility_icon);
        entrantDashboardButton = findViewById(R.id.entrant_dashboard_icon);
        adminDashboardButton = findViewById(R.id.admin_dashboard_icon);
        organizerDashboardButton = findViewById(R.id.organizer_dashboard_icon);
        logoutButton = findViewById(R.id.logout_button); // Assuming you have added this button to your layout

        // Set click listeners for each button
        if (profileButton != null) {
            profileButton.setOnClickListener(v -> {
                // Check if the user is logged in using SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE); // Use consistent SharedPreferences name
                String userId = sharedPreferences.getString("USER_ID", null); // Retrieve the stored user ID

                Intent intent;
                if (userId != null && !userId.isEmpty()) {
                    // If the user is logged in, navigate to EditProfileActivity
                    intent = new Intent(MainActivity.this, EditProfileActivity.class);
                } else {
                    // If the user is not logged in, navigate to LoginMainActivity
                    intent = new Intent(MainActivity.this, LoginMainActivity.class);
                }
                // Start the appropriate activity
                startActivity(intent);
            });
        }

        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                // Example of a "Coming Soon" feature
                Toast.makeText(MainActivity.this, "Notification feature is coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        if (joinEventButton != null) {
            joinEventButton.setOnClickListener(v -> {
                // Example of a "Coming Soon" feature
                Toast.makeText(MainActivity.this, "Join Event feature is coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        if (createEventButton != null) {
            createEventButton.setOnClickListener(v -> {
                // Example of a "Coming Soon" feature
                Toast.makeText(MainActivity.this, "Create Event feature is coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        if (createFacilityButton != null) {
            createFacilityButton.setOnClickListener(v -> {
                // Example of a "Coming Soon" feature
                Toast.makeText(MainActivity.this, "Create Facility feature is coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        if (entrantDashboardButton != null) {
            entrantDashboardButton.setOnClickListener(v -> {
                // Example of a "Coming Soon" feature
                Toast.makeText(MainActivity.this, "Entrant Dashboard feature is coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        if (adminDashboardButton != null) {
            adminDashboardButton.setOnClickListener(v -> {
                // Example of a "Coming Soon" feature
                Toast.makeText(MainActivity.this, "Admin Dashboard feature is coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        if (organizerDashboardButton != null) {
            organizerDashboardButton.setOnClickListener(v -> {
                // Example of a "Coming Soon" feature
                Toast.makeText(MainActivity.this, "Organizer Dashboard feature is coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        // Set click listener for logout button
        if (logoutButton != null) {
            logoutButton.setOnClickListener(v -> {
                // Clear user session
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE); // Use consistent SharedPreferences name
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clears all the saved session data
                editor.apply();

                // Redirect to MainActivity after logout
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                // Show a toast message after redirect
                Toast.makeText(MainActivity.this, "You have been logged out. To log in, please click the profile button.", Toast.LENGTH_LONG).show();

                finish();
            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Check if the user is logged in
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE); // Use consistent SharedPreferences name
        String userId = sharedPreferences.getString("USER_ID", null);

        if (userId == null || userId.isEmpty()) {
            // User is not logged in
            Toast.makeText(this, "You are not logged in. Please log in to access your profile.", Toast.LENGTH_SHORT).show();
        }
    }
}
