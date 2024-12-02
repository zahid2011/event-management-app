package com.example.event_lottery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EntrantEventWaitingDetails extends AppCompatActivity {
    private TextView eventName, eventDate, eventDescription, eventCapacity, eventPrice, eventGeoLocation;
    private Button leaveEventButton;
    private String eventId;
    private FirebaseFirestore db;
    private static final String TAG = "EntrantEventWaitingDetails";

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_leave_waiting_list);

        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        eventName = findViewById(R.id.event_name);
        eventDate = findViewById(R.id.event_date);
        eventDescription = findViewById(R.id.event_description);
        eventCapacity = findViewById(R.id.event_capacity);
        eventPrice = findViewById(R.id.event_price);
        eventGeoLocation = findViewById(R.id.event_geo_location);
        leaveEventButton = findViewById(R.id.leave_event_button);

        // Retrieve event details from intent
        eventId = getIntent().getStringExtra("eventId");
        String name = getIntent().getStringExtra("eventName");
        String date = getIntent().getStringExtra("eventDate");
        String description = getIntent().getStringExtra("eventDescription");
        String capacity = getIntent().getStringExtra("eventCapacity");
        String price = getIntent().getStringExtra("eventPrice");
        String location = getIntent().getStringExtra("eventLocation");

        // Populate the UI with the event details
        eventName.setText(name != null ? name : "Unknown Event");
        eventDate.setText(date != null ? date : "No date provided");
        eventDescription.setText(description != null ? description : "No description available");
        eventCapacity.setText("Capacity: " + (capacity != null ? capacity : "N/A"));
        eventPrice.setText("Price: $" + (price != null ? price : "N/A"));
        eventGeoLocation.setText("Location: " + (location != null ? location : "N/A"));

        // Set up back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Set up "Leave Waiting List" button
        leaveEventButton.setOnClickListener(v -> showConfirmationDialog());
    }

    /**
     * Shows a confirmation dialog to confirm if the user wants to leave the waiting list.
     */
    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Leave Waiting List")
                .setMessage("Are you sure you want to leave the waiting list for this event?")
                .setPositiveButton("Yes", (dialog, which) -> removeUserFromWaitingList())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Removes the user from the waiting list of the specified event.
     */
    private void removeUserFromWaitingList() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null); // This is the email

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid userId. Cannot proceed.");
            return;
        }

        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Invalid event. Please try again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid eventId. Cannot proceed.");
            return;
        }

        // Log userId and eventId for debugging
        Log.d(TAG, "Attempting to remove user with ID (email): " + userId);
        Log.d(TAG, "Attempting to remove user from event with ID: " + eventId);

        db.collection("events").document(eventId).collection("waitingList")
                .whereEqualTo("userId", userId) // Match based on the userId field
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Log.d(TAG, "Query executed successfully. Number of documents found: " + querySnapshot.size());

                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Log.d(TAG, "Deleting document with ID: " + document.getId());
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Successfully removed user from waiting list. Document ID: " + document.getId());
                                        Toast.makeText(this, "Successfully removed from waiting list.", Toast.LENGTH_SHORT).show();
                                        finish(); // Close the activity after successful removal
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error deleting document: " + document.getId(), e);
                                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Log.d(TAG, "No matching document found for userId: " + userId);
                        Toast.makeText(this, "You are not in this waiting list.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error accessing waiting list for eventId: " + eventId, e);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
