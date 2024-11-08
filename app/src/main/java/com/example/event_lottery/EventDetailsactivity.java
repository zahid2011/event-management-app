package com.example.event_lottery;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EventDetailsactivity extends AppCompatActivity {

    private static final String TAG = "EventDetailsActivity";
    private TextView eventNameView, descriptionView, capacityView, dateTimeView, priceView, geolocationView, maxWaitingListView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Initialize views
        eventNameView = findViewById(R.id.event_name);
        descriptionView = findViewById(R.id.event_description);
        capacityView = findViewById(R.id.event_capacity);
        dateTimeView = findViewById(R.id.event_date);
        priceView = findViewById(R.id.event_price);
        geolocationView = findViewById(R.id.event_geolocation);
        maxWaitingListView = findViewById(R.id.event_max_waiting_list);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve event name from intent
        String eventName = getIntent().getStringExtra("event_name");
        if (eventName != null && !eventName.isEmpty()) {
            fetchEventDetails(eventName);
        } else {
            Log.e(TAG, "Event name is missing in the intent");
            Toast.makeText(this, "Event name is missing", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchEventDetails(String eventName) {
        Log.d(TAG, "Fetching details for event: " + eventName);

        // Query Firestore to fetch event details based on eventName
        db.collection("events")
                .whereEqualTo("eventName", eventName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Log.d(TAG, "Event found: " + document.getId());

                                // Retrieve and display each field according to the Firebase structure
                                String description = document.getString("description");
                                String eventDateTime = document.getString("eventDateTime");
                                String price = document.getString("price");
                                String capacity = document.getString("capacity");
                                Boolean geolocationEnabled = document.getBoolean("geolocationEnabled");
                                Long maxWaitingList = document.getLong("maxWaitingList");

                                // Update UI with event details
                                eventNameView.setText("Event Name: " + eventName);
                                descriptionView.setText("Description: " + (description != null ? description : "N/A"));
                                capacityView.setText("Capacity: " + (capacity != null ? capacity : "N/A"));
                                dateTimeView.setText("Date: " + (eventDateTime != null ? eventDateTime : "N/A"));
                                priceView.setText("Price: $" + (price != null ? price : "N/A"));
                                geolocationView.setText("Geolocation Enabled: " + (geolocationEnabled != null ? geolocationEnabled : "N/A"));
                                maxWaitingListView.setText("Max Waiting List: " + (maxWaitingList != null ? maxWaitingList : "N/A"));
                            }
                        } else {
                            Log.d(TAG, "No event found with the name " + eventName);
                            Toast.makeText(this, "No event found with the name " + eventName, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Log.e(TAG, "Failed to fetch event details", task.getException());
                        Toast.makeText(this, "Failed to fetch event details", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
