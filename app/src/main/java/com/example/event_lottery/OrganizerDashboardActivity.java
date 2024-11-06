package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizerDashboardActivity extends AppCompatActivity {

    private Button buttonEvent;
    private Button button_Event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);  // Organizer-specific layout

        // Initialize the button
        buttonEvent = findViewById(R.id.btn_create_event);
        button_Event = findViewById(R.id.btn_ongoing_events);
        // Set an OnClickListener to start CreateEventActivity
        buttonEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createEventIntent = new Intent(OrganizerDashboardActivity.this, CreateEventActivity.class);
                startActivity(createEventIntent);  // Start the CreateEventActivity
                Toast.makeText(OrganizerDashboardActivity.this, "Navigating to Create Event", Toast.LENGTH_SHORT).show();
            }
        });



        // Set an OnClickListener to start OngoingEventActivity
        button_Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ongoingEventIntent = new Intent(OrganizerDashboardActivity.this, OngoingEventsActivity.class);
                startActivity(ongoingEventIntent);  // Start the CreateEventActivity
                Toast.makeText(OrganizerDashboardActivity.this, "Navigating to Ongoing Events", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
