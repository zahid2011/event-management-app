package com.example.event_lottery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private ImageView profileButton, notificationButton, joinEventButton, createEventButton,
            createFacilityButton, entrantDashboardButton, adminDashboardButton,
            organizerDashboardButton;

    private androidx.cardview.widget.CardView entrantDashboardCard, organizerDashboardCard, adminDashboardCard;

    private Button logoutButton;
    private TextView welcomeMessage;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the dashboard layout as the main content view
        setContentView(R.layout.sample_dashboard);

        // Initialize UI elements
        initializeUI();
        db = FirebaseFirestore.getInstance();

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

        // Retrieve SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String savedDeviceId = sharedPreferences.getString("DEVICE_ID", null);

        // Get current device's identifier
        String currentDeviceId = getDeviceIdentifier();

        if (savedDeviceId != null && savedDeviceId.equals(currentDeviceId)) {
            // Device matches, fetch saved user info
            String userRole = sharedPreferences.getString("USER_ROLE", null);
            String userFirstName = sharedPreferences.getString("USER_FIRST_NAME", "User");

            welcomeMessage.setText("Welcome back, " + userFirstName + "!");
            configureDashboardAccess(userRole); // Configure buttons based on role
        } else {
            // Device mismatch or no saved device, check Firestore for deviceId
            db.collection("users").whereEqualTo("deviceId", currentDeviceId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            // Device found, retrieve user data
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String email = document.getString("email");
                            String role = document.getString("role");
                            String firstName = document.getString("firstName");

                            // Save user info in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("USER_ID", email);
                            editor.putString("USER_ROLE", role);
                            editor.putString("USER_FIRST_NAME", firstName != null ? firstName : "User");
                            editor.putString("DEVICE_ID", currentDeviceId); // Save current device ID
                            editor.apply();

                            // Update UI
                            welcomeMessage.setText("Welcome back, " + firstName + "!");
                            configureDashboardAccess(role);
                        } else {
                            // Device not recognized
                            Toast.makeText(MainActivity.this, "Device not recognized. Please log in.", Toast.LENGTH_SHORT).show();
                            welcomeMessage.setText("To log in, please click the Profile button.");
                            hideDashboardButtons();
                        }
                    });
        }
    }
    private String getDeviceIdentifier() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    // Hide dashboard buttons if login fails
    private void hideDashboardButtons() {
        // Hide the entire card views
        entrantDashboardCard.setVisibility(View.GONE);
        organizerDashboardCard.setVisibility(View.GONE);
        adminDashboardCard.setVisibility(View.GONE);
    }

    private void initializeUI() {
        profileButton = findViewById(R.id.profile_icon);
        notificationButton = findViewById(R.id.notification_icon);
        joinEventButton = findViewById(R.id.join_event_icon);
        createFacilityButton = findViewById(R.id.create_facility_icon);
        entrantDashboardButton = findViewById(R.id.entrant_dashboard_icon);
        adminDashboardButton = findViewById(R.id.admin_dashboard_icon);
        organizerDashboardButton = findViewById(R.id.organizer_dashboard_icon);
        logoutButton = findViewById(R.id.logout_button);
        welcomeMessage = findViewById(R.id.welcome_message);

        // Initialize CardViews
        entrantDashboardCard = findViewById(R.id.card_entrant_dashboard);
        organizerDashboardCard = findViewById(R.id.card_organizer_dashboard);
        adminDashboardCard = findViewById(R.id.card_admin_dashboard);

        welcomeMessage.setText("To log in, please click the Profile button.");

        createFacilityButton.setOnClickListener(v -> {
            // Navigate to the ManageFacilityActivity screen
            Intent intent = new Intent(MainActivity.this, ManageFacilityActivity.class);
            startActivity(intent);
        });
    }


    private void configureDashboardAccess(String userRole) {
        hideDashboardButtons();

        if (userRole == null) {
            android.util.Log.e("MainActivity", "USER_ROLE is null or not found in SharedPreferences");
            return;
        }

        android.util.Log.d("MainActivity", "Retrieved USER_ROLE: " + userRole);

        // Standardize role string
        userRole = userRole.trim().toLowerCase();

        // Show the appropriate card based on the role
        switch (userRole) {
            case "entrant":
                entrantDashboardCard.setVisibility(View.VISIBLE);
                entrantDashboardButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                });
                break;

            case "organiser":
                organizerDashboardCard.setVisibility(View.VISIBLE);
                organizerDashboardButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, OrganizerDashboardActivity.class);
                    startActivity(intent);
                });
                break;

            case "admin":
                adminDashboardCard.setVisibility(View.VISIBLE);
                adminDashboardButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                });
                break;

            default:
                Toast.makeText(this, "Unknown role: " + userRole, Toast.LENGTH_SHORT).show();
                android.util.Log.e("MainActivity", "Unknown role: " + userRole);
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
                String userId = sharedPreferences.getString("USER_ID", null);

                if (userId != null && db != null) {
                    db.collection("users").document(userId).update("deviceId", null)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(MainActivity.this, "Device ID cleared in Firestore.", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(MainActivity.this, "Failed to clear device ID.", Toast.LENGTH_SHORT).show();
                            });
                } else if (db == null) {
                    Toast.makeText(MainActivity.this, "Firestore is not initialized.", Toast.LENGTH_SHORT).show();
                }

                // Clear SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_LONG).show();
                finish();
            });
        }
    }
}
