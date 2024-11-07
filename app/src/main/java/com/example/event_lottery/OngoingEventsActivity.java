package com.example.event_lottery;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import androidx.core.content.ContextCompat;




import java.util.Date;


public class OngoingEventsActivity extends AppCompatActivity {


    private FirebaseFirestore db;
    private LinearLayout eventsContainer;
    private TextView totalEvents;
    private EditText searchBar;
    private int eventCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ongoing_events);


        // Initialize Firestore and views
        db = FirebaseFirestore.getInstance();
        eventsContainer = findViewById(R.id.events_container);
        totalEvents = findViewById(R.id.total_events);
        searchBar = findViewById(R.id.search_bar);


        // Load events from Firestore
        loadEvents();
    }


    private void loadEvents() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventsContainer.removeAllViews(); // Clear any existing views
                        eventCount = 0;


                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Create a view for each event
                            Events event = document.toObject(Events.class);
                            addEventView(event);
                        }


                        // Update the total events count
                        totalEvents.setText("Total Events: " + eventCount);
                    } else {
                        Toast.makeText(OngoingEventsActivity.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void addEventView(Events event) {
        eventCount++; // Increment the count for each event


        // Create a new LinearLayout to represent each event
        LinearLayout eventLayout = new LinearLayout(this);
        eventLayout.setOrientation(LinearLayout.HORIZONTAL);
        eventLayout.setPadding(8, 8, 8, 8);
        eventLayout.setBackgroundColor(getResources().getColor(R.color.event_background));


        // Event ID TextView
        TextView eventIdTextView = new TextView(this);
        eventIdTextView.setText("Event ID: " + event.getEventName());
        eventIdTextView.setTypeface(null, android.graphics.Typeface.BOLD);


        // Description TextView
        TextView eventDescTextView = new TextView(this);
        eventDescTextView.setText("Description: " + event.getDescription());


        // "Details" Button
        Button detailsButton = new Button(this);
        detailsButton.setText("Details");
        detailsButton.setOnClickListener(v -> openEventDetails(event.getEventName()));


        // Add TextViews and Button to the event layout
        eventLayout.addView(eventIdTextView);
        eventLayout.addView(eventDescTextView);
        eventLayout.addView(detailsButton);


        // Add the event layout to the container
        eventsContainer.addView(eventLayout);
    }


    private void openEventDetails(String eventId) {
        // Intent to open EventDetailsActivity (assuming it exists)
        Toast.makeText(OngoingEventsActivity.this, eventId, Toast.LENGTH_SHORT).show();// Pass event ID
        Intent intent = new Intent(OngoingEventsActivity.this, EventDetailsActivity.class);
        intent.putExtra("eventName", eventId);
        startActivity(intent);
    }
}
