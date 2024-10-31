// MainActivity.java
package com.example.event_lottery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.event_lottery.R;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateNewEvent, btnOngoingEvents, btnPastEvents, btnManageFacility, btnLogout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnCreateNewEvent = findViewById(R.id.btn_create_event);
        btnOngoingEvents = findViewById(R.id.btn_ongoing_events);
        btnPastEvents = findViewById(R.id.btn_past_events);
        btnManageFacility = findViewById(R.id.btn_manage_facility);
        btnLogout = findViewById(R.id.btn_logout);

        // Set up click listeners
        btnCreateNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CreateEventActivity
                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(intent);
            }
        });

        btnOngoingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to OngoingEventsActivity
                Intent intent = new Intent(MainActivity.this, OngoingEventsActivity.class);
                startActivity(intent);
            }
        });

        btnPastEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to PastEventsActivity
                Intent intent = new Intent(MainActivity.this, PastEventsActivity.class);
                startActivity(intent);
            }
        });

        btnManageFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ManageFacilityActivity
                Intent intent = new Intent(MainActivity.this, ManageFacilityActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout logic here (e.g., clearing user session)
                finish();
            }
        });
    }
}
