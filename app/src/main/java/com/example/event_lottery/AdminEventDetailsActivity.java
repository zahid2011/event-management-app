package com.example.event_lottery;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminEventDetailsActivity extends AppCompatActivity {
    private TextView eventNameTextView, eventDateTextView, eventCapacityTextView, eventPriceTextView, eventDescriptionTextView, geoLocationTextView;
    private Button removeEventButton;
    private FirebaseFirestore db;
    private String eventId; // unique identifier for the event document in Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_event_details_page);

        // initializing Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize TextViews
        eventNameTextView = findViewById(R.id.event_name);
        eventDateTextView = findViewById(R.id.event_date);
        eventCapacityTextView = findViewById(R.id.event_capacity);
        eventPriceTextView = findViewById(R.id.event_price);
        eventDescriptionTextView = findViewById(R.id.event_description);
        geoLocationTextView = findViewById(R.id.event_geo_location); // Add this to your XML layout
        removeEventButton = findViewById(R.id.remove_event_button);

        // Get data from the Intent
        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");
        String eventName = intent.getStringExtra("eventName");
        String eventDateString = intent.getStringExtra("eventDate");
        String eventCapacity = intent.getStringExtra("eventCapacity");
        String eventPrice = intent.getStringExtra("eventPrice");
        String eventDescription = intent.getStringExtra("eventDescription");
        boolean geoLocation = intent.getBooleanExtra("geoLocation", false);

        // Populate TextViews
        eventNameTextView.setText(eventName != null ? eventName : "No Name");
        eventDateTextView.setText(eventDateString != null ? "Date: " + eventDateString : "Date: N/A");
        eventCapacityTextView.setText(eventCapacity != null ? "Capacity: " + eventCapacity : "Capacity: N/A");
        eventPriceTextView.setText(eventPrice != null ? "Price: $" + eventPrice : "Price: N/A");
        eventDescriptionTextView.setText(eventDescription != null ? "Description: " + eventDescription : "Description: N/A");
        geoLocationTextView.setText("Geo-location: " + (geoLocation ? "Enabled" : "Disabled"));

        // Back button functionality
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Removing event button with confirmation dialog
        removeEventButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Yes", (dialog, which) -> deleteEvent())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void deleteEvent() {
        // Ensure eventId is not null or empty before attempting to delete
        if (eventId != null && !eventId.isEmpty()) {
            db.collection("events").document(eventId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AdminEventDetailsActivity.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(AdminEventDetailsActivity.this, "Error deleting event", Toast.LENGTH_SHORT).show()
                    );
        } else {
            Toast.makeText(this, "Event ID is missing, cannot delete event.", Toast.LENGTH_SHORT).show();
        }
    }
}
