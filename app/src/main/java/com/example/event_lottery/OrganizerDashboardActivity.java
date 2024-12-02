package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

/**
 * Represents the dashboard for organizers, providing navigation to various organizer-specific functionalities.
 * Includes options for creating new events, viewing ongoing and past events, and managing facilities.
 */

public class OrganizerDashboardActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     * Initializes the organizer dashboard UI components and sets up navigation functionality.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // Organizer-specific layout

        // Back Button functionality
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // Navigate back to MainActivity
            Intent intent = new Intent(OrganizerDashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // Close OrganizerDashboardActivity
        });

        // "Create New Events" card
        /*
          Navigates to the {@link CreateEventActivity} when the "Create New Events" card is clicked.
         */

        // "Create New Events" card
        CardView createEventButton = findViewById(R.id.btn_create_event); // Updated ID
        createEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, CreateEventActivity.class);
            startActivity(intent);
        });

        // "Ongoing Events" card
        /*
         * Navigates to the {@link OngoingEventsActivity} when the "Ongoing Events" card is clicked.
         */

        // "Ongoing Events" card
        CardView ongoingEventsButton = findViewById(R.id.btn_ongoing_events); // Updated ID
        ongoingEventsButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, OngoingEventsActivity.class);
            startActivity(intent);
        });

        // "Past Events" card
        CardView pastEventsButton = findViewById(R.id.btn_past_events); // Updated ID
        pastEventsButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, PastEventsActivity.class);
            startActivity(intent);
        });

        // "Manage Facility" card
        /*
         * Navigates to the {@link ManageFacilityActivity} when the "Manage Facility" card is clicked.
         */

        // "Manage Facility" card
        CardView manageFacilityButton = findViewById(R.id.btn_manage_facility); // Updated ID
        manageFacilityButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, ManageFacilityActivity.class);
            startActivity(intent);
        });
    }
}
