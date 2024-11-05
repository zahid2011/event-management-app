// MainActivity.java
package com.example.event_lottery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    private Button loginButton, signUpButton, btnCreateNewEvent, btnOngoingEvents, btnPastEvents, btnManageFacility;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.main_signup_page); // Ensure this is the correct layout file

        // Handle deep link if the activity was launched from a QR code scan
        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            if (data != null && "event".equals(data.getHost())) {
                String eventId = data.getLastPathSegment(); // Extracts <event_id> from "myapp://event/<event_id>"

                // Launch EventDetailsActivity with the eventId
                Intent detailsIntent = new Intent(this, EventDetailsActivity.class);
                detailsIntent.putExtra("event_id", eventId);
                startActivity(detailsIntent);
            }
        }

        // Initialize buttons
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        btnCreateNewEvent = findViewById(R.id.btn_create_event);
        btnOngoingEvents = findViewById(R.id.btn_ongoing_events);
        btnPastEvents = findViewById(R.id.btn_past_events);
        btnManageFacility = findViewById(R.id.btn_manage_facility);

        // Set up button listeners with null checks
        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            });
        } else {
            Toast.makeText(this, "Login button not found", Toast.LENGTH_SHORT).show();
        }

        if (signUpButton != null) {
            signUpButton.setOnClickListener(v -> {
                Intent signUpIntent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(signUpIntent);
            });
        } else {
            Toast.makeText(this, "Sign Up button not found", Toast.LENGTH_SHORT).show();
        }

        // Event management buttons
        if (btnCreateNewEvent != null) {
            btnCreateNewEvent.setOnClickListener(v -> {
                Toast.makeText(this, "create event", Toast.LENGTH_SHORT).show();
                Intent createEventIntent = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(createEventIntent);
            });
        }

        if (btnOngoingEvents != null) {
            btnOngoingEvents.setOnClickListener(v -> {
                Intent ongoingEventsIntent = new Intent(MainActivity.this, OngoingEventsActivity.class);
                startActivity(ongoingEventsIntent);
            });
        }

        if (btnPastEvents != null) {
            btnPastEvents.setOnClickListener(v -> {
                Intent pastEventsIntent = new Intent(MainActivity.this, PastEventsActivity.class);
                startActivity(pastEventsIntent);
            });
        }

        if (btnManageFacility != null) {
            btnManageFacility.setOnClickListener(v -> {
                Intent manageFacilityIntent = new Intent(MainActivity.this, ManageFacilityActivity.class);
                startActivity(manageFacilityIntent);
            });
        }
    }
}
