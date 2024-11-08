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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EntrantEventWaitingDetails extends AppCompatActivity {
    private TextView eventName, eventDate, eventDescription, eventCapacity, eventPrice, eventGeoLocation;
    private Button leaveEventButton;
    private String eventId;
    private FirebaseFirestore db;
    private static final String TAG = "EntrantEventWaitingDetails";

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
        eventName.setText(getIntent().getStringExtra("eventName"));
        eventDate.setText(getIntent().getStringExtra("eventDate"));
        eventDescription.setText(getIntent().getStringExtra("eventDescription"));
        eventCapacity.setText("Capacity: " + getIntent().getStringExtra("eventCapacity"));
        eventPrice.setText("Price: $" + getIntent().getStringExtra("eventPrice"));
        eventGeoLocation.setText("Location: " + getIntent().getStringExtra("eventLocation"));

        // Set up back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // "Leave Waiting List" button with confirmation dialog
        leaveEventButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Removal")
                    .setMessage("Are you sure you want to leave the waiting list for this event?")
                    .setPositiveButton("Yes", (dialog, which) -> removeUserFromWaitingList())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void removeUserFromWaitingList() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Locate and delete the document in the waiting list for this event
        db.collection("events").document(eventId).collection("waitingList")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Successfully removed user from waiting list");
                                        Toast.makeText(this, "Successfully removed from waiting list", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error deleting document", e);
                                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Log.d(TAG, "No matching record found in waiting list for deletion");
                        Toast.makeText(this, "No matching record found in waiting list", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error accessing waiting list", e);
                    Toast.makeText(this, "Error accessing waiting list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
