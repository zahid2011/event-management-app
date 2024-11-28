package com.example.event_lottery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizerDashboardActivity extends AppCompatActivity {

    private Button buttonEvent;
    private Button button_Event;
    private Button button_Facility;
    private Button logoutButton;  // Add this line
    private String userId;
    private String userRole;
    private SharedPreferences sharedPreferences;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // Organizer-specific layout

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", null);
        userRole = sharedPreferences.getString("USER_ROLE", null);

        // Initialize buttons
        buttonEvent = findViewById(R.id.btn_create_event);
        button_Event = findViewById(R.id.btn_ongoing_events);
        button_Facility = findViewById(R.id.btn_manage_facility);

        backButton = findViewById(R.id.backButton);  // Back button initialization

        // Back Button Listener
        backButton.setOnClickListener(v -> {
            // Navigate back to the main activity
            Intent intent = new Intent(OrganizerDashboardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });


        // Existing listeners for other buttons
        buttonEvent.setOnClickListener(v -> {
            Intent createEventIntent = new Intent(OrganizerDashboardActivity.this, CreateEventActivity.class);
            startActivity(createEventIntent);
            Toast.makeText(OrganizerDashboardActivity.this, "Navigating to Create Event", Toast.LENGTH_SHORT).show();
        });

        button_Event.setOnClickListener(v -> {
            Intent ongoingEventIntent = new Intent(OrganizerDashboardActivity.this, OngoingEventsActivity.class);
            startActivity(ongoingEventIntent);
            Toast.makeText(OrganizerDashboardActivity.this, "Navigating to Ongoing Events", Toast.LENGTH_SHORT).show();
        });

        button_Facility.setOnClickListener(v -> {
            Intent manageFacilityIntent = new Intent(OrganizerDashboardActivity.this, ManageFacilityActivity.class);
            startActivity(manageFacilityIntent);
            Toast.makeText(OrganizerDashboardActivity.this, "Navigating to Facility Profile", Toast.LENGTH_SHORT).show();
        });
    }
}