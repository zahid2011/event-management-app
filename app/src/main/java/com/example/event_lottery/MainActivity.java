// MainActivity.java
package com.example.event_lottery;

import static android.content.ContentValues.TAG;
import android.util.Log;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        setContentView(R.layout.activity_dashboard);
        btnCreateNewEvent = findViewById(R.id.btn_create_event);
        if (btnCreateNewEvent == null) {
            Log.e(TAG, "Button not found. Check the ID or layout.");
            return; // Exit if button is null to avoid NullPointerException
        }
        btnOngoingEvents = findViewById(R.id.btn_ongoing_events);
        btnPastEvents = findViewById(R.id.btn_past_events);
        btnManageFacility = findViewById(R.id.btn_manage_facility);
        btnLogout = findViewById(R.id.btn_logout);

        btnCreateNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button clicked!");
                // Navigate to CreateEventActivity
                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(intent);
            }
        });

        btnOngoingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OngoingEventsActivity.class);
                startActivity(intent);
            }
        });

        btnPastEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PastEventsActivity.class);
                startActivity(intent);
            }
        });

        btnManageFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageFacilityActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
