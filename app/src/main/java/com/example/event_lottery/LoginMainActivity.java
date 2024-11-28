package com.example.event_lottery;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.FirebaseApp;


public class LoginMainActivity extends AppCompatActivity {
    private Button loginButton, signUpButton, btnCreateNewEvent, btnOngoingEvents, btnPastEvents, btnManageFacility;
    private ImageButton backButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        setContentView(R.layout.main_signup_page);
        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            if (data != null && "event".equals(data.getHost())) {
                String eventId = data.getLastPathSegment();
                // launch EventDetailsActivity with the eventId
                Intent detailsIntent = new Intent(this, EventDetailsActivity.class);
                Intent qr_code_intent = new Intent(this, Entrant_qr_code_activity.class);
                qr_code_intent.putExtra("event_id", eventId);
                detailsIntent.putExtra("event_id", eventId);
                startActivity(qr_code_intent);
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
        backButton = findViewById(R.id.backButton);
        // Set up back button functionality
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                // Navigate back
                finish(); // Closes the current activity and returns to the previous one
            });
        } else {
            Toast.makeText(this, "Back button not found", Toast.LENGTH_SHORT).show();
        }
        // Set up button listeners with null checks
        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                Intent loginIntent = new Intent(LoginMainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            });
        } else {
            Toast.makeText(this, "Login button not found", Toast.LENGTH_SHORT).show();
        }
        if (signUpButton != null) {
            signUpButton.setOnClickListener(v -> {
                Intent signUpIntent = new Intent(LoginMainActivity.this, SignupActivity.class);
                startActivity(signUpIntent);
            });
        } else {
            Toast.makeText(this, "Sign Up button not found", Toast.LENGTH_SHORT).show();
        }
        // Event management buttons
        if (btnCreateNewEvent != null) {
            btnCreateNewEvent.setOnClickListener(v -> {
                Toast.makeText(this, "create event", Toast.LENGTH_SHORT).show();
                Intent createEventIntent = new Intent(LoginMainActivity.this, CreateEventActivity.class);
                startActivity(createEventIntent);
            });
        }
        if (btnOngoingEvents != null) {
            btnOngoingEvents.setOnClickListener(v -> {
                Intent ongoingEventsIntent = new Intent(LoginMainActivity.this, OngoingEventsActivity.class);
                startActivity(ongoingEventsIntent);
            });
        }
        if (btnPastEvents != null) {
            btnPastEvents.setOnClickListener(v -> {
                Intent pastEventsIntent = new Intent(LoginMainActivity.this, PastEventsActivity.class);
                startActivity(pastEventsIntent);
            });
        }
        if (btnManageFacility != null) {
            btnManageFacility.setOnClickListener(v -> {
                Intent manageFacilityIntent = new Intent(LoginMainActivity.this, ManageFacilityActivity.class);
                startActivity(manageFacilityIntent);
            });
        }
    }
}
