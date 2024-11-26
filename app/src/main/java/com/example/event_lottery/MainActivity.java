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
                // Check if the user is already logged in
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                if (isLoggedIn) {
                    // Navigate to EditProfileActivity if logged in
                    Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                } else {
                    // Navigate to the LoginMainActivity if not logged in
                    Intent intent = new Intent(MainActivity.this, LoginMainActivity.class);
                    startActivity(intent);
                }
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clears all the saved session data
                editor.apply();

                // Redirect to login page after logout
                Intent intent = new Intent(MainActivity.this, LoginMainActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }
}
