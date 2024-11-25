package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OngoingEventsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout eventsContainer;
    private TextView totalEvents;
    private EditText searchInput;
    private ImageButton searchButton;
    private int eventCount = 0;
    private List<Events> allEvents = new ArrayList<>(); // Store all events for filtering

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ongoing_events);

        // Initialize Firestore and views
        db = FirebaseFirestore.getInstance();
        eventsContainer = findViewById(R.id.events_container);
        totalEvents = findViewById(R.id.total_events);
        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);

        // Load events from Firestore
        loadEvents();

        // Set up search button click listener
        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                filterEvents(query);
            } else {
                displayAllEvents(); // Show all events if the search query is empty
            }
        });

        // Add a TextWatcher to handle clearing the search bar
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    displayAllEvents(); // Show all events when search input is cleared
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });
    }

    private void loadEvents() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        allEvents.clear();
                        eventsContainer.removeAllViews(); // Clear any existing views
                        eventCount = 0;

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Create an Events object for each document
                            Events event = document.toObject(Events.class);
                            allEvents.add(event); // Add to the list of all events
                        }

                        displayAllEvents(); // Display all events initially
                    } else {
                        Toast.makeText(OngoingEventsActivity.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayAllEvents() {
        eventsContainer.removeAllViews(); // Clear existing views
        eventCount = 0;

        for (Events event : allEvents) {
            addEventView(event);
        }

        // Update the total events count
        totalEvents.setText("Total Events: " + eventCount);
    }

    private void filterEvents(String query) {
        eventsContainer.removeAllViews(); // Clear existing views
        eventCount = 0;

        for (Events event : allEvents) {
            // Check if the event name (Event ID) matches the query exactly
            if (event.getEventName().equalsIgnoreCase(query)) {
                addEventView(event); // Add the matching event to the view
                break; // Exit the loop once the event is found (assuming Event IDs are unique)
            }
        }

        // Update the total events count
        totalEvents.setText("Total Events: " + eventCount);

        // If no events match the query, show a toast
        if (eventCount == 0) {
            Toast.makeText(this, "No event found with ID: " + query, Toast.LENGTH_SHORT).show();
        }
    }

    private void addEventView(Events event) {
        eventCount++; // Increment the count for each event

        // Inflate the event_item.xml layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View eventView = inflater.inflate(R.layout.event_item, eventsContainer, false);

        // Find views in the event_item.xml layout
        TextView eventNameTextView = eventView.findViewById(R.id.event_name);
        TextView eventDescriptionTextView = eventView.findViewById(R.id.event_description);
        Button detailsButton = eventView.findViewById(R.id.details_button);

        // Set data for the event
        eventNameTextView.setText("Event ID: " + event.getEventName());
        eventDescriptionTextView.setText("Description: " + event.getDescription());

        // Set button click listener
        detailsButton.setOnClickListener(v -> openEventDetails(event.getEventName()));

        // Add the event view to the container
        eventsContainer.addView(eventView);
    }

    private void openEventDetails(String eventId) {
        // Intent to open EventDetailsActivity (assuming it exists)
        Intent intent = new Intent(OngoingEventsActivity.this, EventDetailsActivity.class);
        intent.putExtra("event_id", eventId); // Pass event ID
        startActivity(intent);
    }
}
