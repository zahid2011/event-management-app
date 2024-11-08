package com.example.event_lottery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDetailsactivity extends AppCompatActivity {

    private static final String TAG = "EventDetailsActivity";
    private TextView eventNameView, descriptionView, capacityView, dateTimeView, priceView, geolocationView, maxWaitingListView;
    private Button registerButton;
    private FirebaseFirestore db;
    private String eventId;
    private String eventName;
    private String userId;

    // SharedPreferences
    private SharedPreferences sharedPreferences;

    // Variable to store whether geolocation is enabled for the event
    private boolean geolocationEnabled = false;

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
        registerButton = findViewById(R.id.register_button);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Retrieve event name from intent
        eventName = getIntent().getStringExtra("event_name");
        if (eventName != null && !eventName.isEmpty()) {
            fetchEventDetails(eventName);
        } else {
            Log.e(TAG, "Event name is missing in the intent");
            Toast.makeText(this, "Event name is missing", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set up the register button click listener
        registerButton.setOnClickListener(view -> registerForEvent());
    }

    private void fetchEventDetails(String eventName) {
        Log.d(TAG, "Fetching details for event: " + eventName);

        // Query Firestore to fetch event details based on eventName
        db.collection("events")
                .whereEqualTo("eventName", eventName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            // Retrieve the first matching document
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            eventId = document.getId(); // Store the event ID for later use
                            Log.d(TAG, "Event found: " + eventId);

                            // Retrieve and display each field according to the Firebase structure
                            String description = document.getString("description");
                            String eventDateTime = document.getString("eventDateTime");
                            String price = document.getString("price");
                            String capacity = document.getString("capacity");
                            Boolean geoEnabled = document.getBoolean("geolocationEnabled");
                            geolocationEnabled = (geoEnabled != null) ? geoEnabled : false;
                            Long maxWaitingList = document.getLong("maxWaitingList");

                            // Update UI with event details
                            eventNameView.setText("Event Name: " + eventName);
                            descriptionView.setText("Description: " + (description != null ? description : "N/A"));
                            capacityView.setText("Capacity: " + (capacity != null ? capacity : "N/A"));
                            dateTimeView.setText("Date: " + (eventDateTime != null ? eventDateTime : "N/A"));
                            priceView.setText("Price: $" + (price != null ? price : "N/A"));
                            geolocationView.setText("Geolocation Enabled: " + geolocationEnabled);
                            maxWaitingListView.setText("Max Waiting List: " + (maxWaitingList != null ? maxWaitingList : "N/A"));
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

    private void registerForEvent() {
        if (eventId == null) {
            Toast.makeText(this, "Event ID is missing. Cannot register.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Attempting to register for event: " + eventId);

        // Retrieve the user ID from SharedPreferences
        userId = sharedPreferences.getString("USER_ID", null);

        if (userId == null) {
            Toast.makeText(this, "User ID not found. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "User ID not found in SharedPreferences.");
            return;
        }

        Log.d(TAG, "User ID retrieved from SharedPreferences: " + userId);

        // Reference to the event's waiting list in Firestore
        DocumentReference eventRef = db.collection("events").document(eventId);

        // Check if the user is already registered
        eventRef.collection("waitingList").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Toast.makeText(EventDetailsactivity.this, "You are already registered for this event.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "User already registered.");
                            // Optionally, disable the register button
                            finish();

                        } else {
                            // User is not registered yet
                            if (geolocationEnabled) {
                                // Show warning popup with only a Dismiss button
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("Geolocation Required")
                                        .setMessage("This waiting list requires geolocation. Press Confirm to register otherwise touch anywhere outside to exit")
                                        .setNeutralButton("Confirm", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User dismissed the dialog
                                                dialog.dismiss();
                                                // Proceed to register the user
                                                addUserToWaitingList(eventRef);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                // No geolocation required, proceed directly
                                addUserToWaitingList(eventRef);
                            }
                        }
                    } else {
                        Log.e(TAG, "Failed to check registration status", task.getException());
                        Toast.makeText(EventDetailsactivity.this, "Failed to check registration status.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addUserToWaitingList(DocumentReference eventRef) {
        eventRef.collection("waitingList").document(userId)
                .set(new WaitingListEntry(userId))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User successfully registered to the waiting list.");
                    Toast.makeText(EventDetailsactivity.this, "Successfully registered.", Toast.LENGTH_SHORT).show();
                    // Optionally, disable the register button to prevent multiple registrations
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error registering to the waiting list", e);
                    Toast.makeText(EventDetailsactivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                });
    }

    // Define a model class for waiting list entries
    public static class WaitingListEntry {
        private String userId;

        public WaitingListEntry() {
            // Default constructor required
            this.userId = "";
        }

        public WaitingListEntry(String userId) {
            this.userId = userId;
        }

        public String getUserId() {
            return userId;
        }
    }
}
