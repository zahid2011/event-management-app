package com.example.event_lottery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView profileButton, notificationButton, joinEventButton, createEventButton,
            createFacilityButton, entrantDashboardButton, adminDashboardButton,
            organizerDashboardButton;

    private Button logoutButton;
    private TextView welcomeMessage; // For displaying the user's first name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the dashboard layout as the main content view
        setContentView(R.layout.sample_dashboard);

        // Initialize UI elements
        initializeUI();

        // Retrieve user info from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userRole = sharedPreferences.getString("USER_ROLE", null);
        String userFirstName = sharedPreferences.getString("USER_FIRST_NAME", "User"); // Default to "User"

        // Display the user's first name in the welcome message
        welcomeMessage.setText("Welcome back, " + userFirstName + "!");

        // Configure dashboard buttons based on the role
        configureDashboardAccess(userRole);

        // Set up common listeners (e.g., logout, profile)
        setupCommonListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if the user is logged in
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);
        String userFirstName = sharedPreferences.getString("USER_FIRST_NAME", null); // Retrieve the user's first name

        if (userId == null || userId.isEmpty()) {
            // User is not logged in
            welcomeMessage.setText("To log in, please click the Profile button.");
        } else {
            // User is logged in
            welcomeMessage.setText("Welcome back, " + userFirstName + "!");
        }
    }

    private void initializeUI() {
        profileButton = findViewById(R.id.profile_icon);
        notificationButton = findViewById(R.id.notification_icon);
        joinEventButton = findViewById(R.id.join_event_icon);
        createEventButton = findViewById(R.id.create_event_icon);
        createFacilityButton = findViewById(R.id.create_facility_icon);
        entrantDashboardButton = findViewById(R.id.entrant_dashboard_icon);
        adminDashboardButton = findViewById(R.id.admin_dashboard_icon);
        organizerDashboardButton = findViewById(R.id.organizer_dashboard_icon);
        logoutButton = findViewById(R.id.logout_button);
        welcomeMessage = findViewById(R.id.welcome_message);


        welcomeMessage.setText("To log in, please click the Profile button.");
    }

    private void configureDashboardAccess(String userRole) {
        // Hide all dashboard buttons by default
        entrantDashboardButton.setVisibility(View.GONE);
        organizerDashboardButton.setVisibility(View.GONE);
        adminDashboardButton.setVisibility(View.GONE);

        if (userRole == null) {
            Toast.makeText(this, "User role not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Standardize role string (remove leading/trailing spaces and ensure proper capitalization)
        userRole = userRole.trim();

        // Enable and configure the button for the user's role
        switch (userRole) {
            case "Entrant": // Check for "Entrant"
                entrantDashboardButton.setVisibility(View.VISIBLE);
                entrantDashboardButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                });
                break;

            case "Organizer": // Check for "Organizer"
                organizerDashboardButton.setVisibility(View.VISIBLE);
                organizerDashboardButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, OrganizerDashboardActivity.class);
                    startActivity(intent);
                });
                break;

            case "Admin": // Check for "Admin"
                adminDashboardButton.setVisibility(View.VISIBLE);
                adminDashboardButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                });
                break;

            default: // Unknown role
                Toast.makeText(this, "Unknown role: " + userRole, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setupCommonListeners() {
        if (profileButton != null) {
            profileButton.setOnClickListener(v -> {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String userId = sharedPreferences.getString("USER_ID", null);

                Intent intent;
                if (userId != null && !userId.isEmpty()) {
                    intent = new Intent(MainActivity.this, EditProfileActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, LoginMainActivity.class);
                }
                startActivity(intent);
            });
        }

        if (logoutButton != null) {
            logoutButton.setOnClickListener(v -> {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clears all the saved session data
                editor.apply();

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                Toast.makeText(this, "Logged out successfully. Please log in to access your profile.", Toast.LENGTH_LONG).show();
                finish();
            });
        }
    }
}
