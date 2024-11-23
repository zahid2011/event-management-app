package com.example.event_lottery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View; // Make sure to import this
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Entrant_qr_code_activity extends AppCompatActivity {

    private static final String TAG = "EntrantQRCodeActivity";
    private TextView eventNameView, descriptionView, capacityView, dateTimeView, priceView, geolocationView, maxWaitingListView;
    private Button registerButton;
    private TextView alreadyRegisteredMessage;
    private ImageView ivBackArrow;
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
        setContentView(R.layout.entrant_qr_code_register_activity); // Ensure this matches your actual layout file

        // Initialize views
        eventNameView = findViewById(R.id.event_name);
        descriptionView = findViewById(R.id.event_description);
        capacityView = findViewById(R.id.event_capacity);
        dateTimeView = findViewById(R.id.event_date);
        priceView = findViewById(R.id.event_price);
        geolocationView = findViewById(R.id.event_geolocation);
        maxWaitingListView = findViewById(R.id.event_max_waiting_list);
        registerButton = findViewById(R.id.register_button);
        alreadyRegisteredMessage = findViewById(R.id.already_registered_message);
        ivBackArrow = findViewById(R.id.iv_back_arrow);

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

        // Set up the back arrow click listener
        ivBackArrow.setOnClickListener(v -> finish());
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
                            Timestamp eventTimestamp = document.getTimestamp("eventDateTime");
                            String price = document.getString("price");
                            String capacity = document.getString("capacity");
                            Boolean geoEnabled = document.getBoolean("geolocationEnabled");
                            geolocationEnabled = (geoEnabled != null) ? geoEnabled : false;
                            Long maxWaitingList = document.getLong("maxWaitingList");

                            // Format date if available
                            String eventDateTimeStr = "N/A";
                            if (eventTimestamp != null) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                eventDateTimeStr = dateFormat.format(eventTimestamp.toDate());
                            }

                            // Update UI with event details
                            eventNameView.setText("Event Name: " + eventName);
                            descriptionView.setText("Description: " + (description != null ? description : "N/A"));
                            capacityView.setText("Capacity: " + (capacity != null ? capacity : "N/A"));
                            dateTimeView.setText("Date: " + eventDateTimeStr);
                            priceView.setText("Price: $" + (price != null ? price : "N/A"));
                            geolocationView.setText("Geolocation Enabled: " + geolocationEnabled);
                            maxWaitingListView.setText("Max Waiting List: " + (maxWaitingList != null ? maxWaitingList : "N/A"));

                            // Check if the user is already registered
                            checkUserRegistrationStatus();

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

    private void checkUserRegistrationStatus() {
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

        eventRef.collection("waitingList").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // User is already registered
                            Log.d(TAG, "User already registered.");
                            registerButton.setVisibility(View.GONE);
                            alreadyRegisteredMessage.setVisibility(View.VISIBLE);
                        } else {
                            // User is not registered yet
                            registerButton.setVisibility(View.VISIBLE);
                            alreadyRegisteredMessage.setVisibility(View.GONE);
                        }
                    } else {
                        Log.e(TAG, "Failed to check registration status", task.getException());
                        Toast.makeText(Entrant_qr_code_activity.this, "Failed to check registration status.", Toast.LENGTH_SHORT).show();
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

        // Check if the user is already registered (redundant but ensures data consistency)
        eventRef.collection("waitingList").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Toast.makeText(Entrant_qr_code_activity.this, "You are already registered for this event.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "User already registered.");
                            registerButton.setVisibility(View.GONE);
                            alreadyRegisteredMessage.setVisibility(View.VISIBLE);
                        } else {
                            // User is not registered yet
                            if (geolocationEnabled) {
                                // Show warning popup with a Dismiss button
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("Geolocation Required")
                                        .setMessage("This waiting list requires geolocation. Press Confirm to register or Dismiss to cancel.")
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // Proceed to register the user
                                                addUserToWaitingList(eventRef);
                                            }
                                        })
                                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User cancelled the dialog
                                                dialog.dismiss();
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
                        Toast.makeText(Entrant_qr_code_activity.this, "Failed to check registration status.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addUserToWaitingList(DocumentReference eventRef) {
        eventRef.collection("waitingList").document(userId)
                .set(new WaitingListEntry(userId))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User successfully registered to the waiting list.");
                    Toast.makeText(Entrant_qr_code_activity.this, "Successfully registered.", Toast.LENGTH_SHORT).show();
                    // Disable the register button and show the message
                    registerButton.setVisibility(View.GONE);
                    alreadyRegisteredMessage.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error registering to the waiting list", e);
                    Toast.makeText(Entrant_qr_code_activity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                });
    }

    // Define a model class for waiting list entries
    public static class WaitingListEntry {
        private String userId;

        public WaitingListEntry() {
            // Default constructor required
        }

        public WaitingListEntry(String userId) {
            this.userId = userId;
        }

        public String getUserId() {
            return userId;
        }
    }
}
