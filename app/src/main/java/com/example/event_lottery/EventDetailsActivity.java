package com.example.event_lottery;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventDescription, tvEventCapacity, tvQrCodeLabel, tvMaxWaitingList;
    private ImageView ivBackArrow;
    private FirebaseFirestore db;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Get the event ID passed from the previous activity
        eventId = getIntent().getStringExtra("event_id");
        Log.d("EventDetailsActivity", "Received Event ID: " + eventId);

        if (eventId == null) {
            Log.e("EventDetailsActivity", "Event ID is null");
            Toast.makeText(this, "Event ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        tvEventName = findViewById(R.id.tv_event_name);
        tvEventDate = findViewById(R.id.tv_event_date);
        tvEventDescription = findViewById(R.id.tv_event_description);
        tvEventCapacity = findViewById(R.id.tv_event_capacity);
        tvQrCodeLabel = findViewById(R.id.tv_qr_code_label);
        tvMaxWaitingList = findViewById(R.id.tv_max_waiting_list); // Initialize tvMaxWaitingList
        ivBackArrow = findViewById(R.id.iv_back_arrow);

        // Fetch event details from Firestore
        fetchEventDetails();

        // Set click listener for max waiting list TextView
        tvMaxWaitingList.setOnClickListener(v -> showMaxWaitingListDialog());

        // Set back arrow click listener to finish the activity
        ivBackArrow.setOnClickListener(v -> finish());
    }

    private void fetchEventDetails() {
        DocumentReference docRef = db.collection("events").document(eventId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Retrieve and display event data
                    String eventName = document.getString("eventName");
                    String eventDateTime = document.getString("eventDateTime");
                    String description = document.getString("description");
                    String capacity = document.getString("capacity");
                    String qrhash = document.getString("qrhash");
                    Long maxWaitingList = document.getLong("maxWaitingList");

                    // Set data in views
                    tvEventName.setText(eventName != null ? eventName : "N/A");
                    tvEventDate.setText("Date: " + (eventDateTime != null ? eventDateTime : "N/A"));
                    tvEventDescription.setText("Event Description: " + (description != null ? description : "N/A"));
                    tvEventCapacity.setText("Capacity: " + (capacity != null ? capacity + " seats available" : "N/A"));
                    tvQrCodeLabel.setText("QR Code: " + (qrhash != null ? qrhash : "N/A"));
                    tvMaxWaitingList.setText("Max Waiting List Entrants: " + (maxWaitingList != null ? maxWaitingList : "[Tap to Set]"));
                } else {
                    Log.e("EventDetailsActivity", "No such document or access denied");
                    Toast.makeText(EventDetailsActivity.this, "Event details not available", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Log.e("EventDetailsActivity", "Error fetching document", task.getException());
                Toast.makeText(EventDetailsActivity.this, "Error loading event details", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void showMaxWaitingListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Max Waiting List Entrants");

        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Enter max waiting list limit");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String inputText = input.getText().toString();
            if (!inputText.isEmpty()) {
                int maxWaitingListLimit = Integer.parseInt(inputText);
                updateMaxWaitingListLimit(maxWaitingListLimit);
            } else {
                Toast.makeText(EventDetailsActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateMaxWaitingListLimit(int maxWaitingListLimit) {
        DocumentReference docRef = db.collection("events").document(eventId);
        docRef.update("maxWaitingList", maxWaitingListLimit)
                .addOnSuccessListener(aVoid -> {
                    // Immediately update the TextView with the new limit
                    tvMaxWaitingList.setText("Max Waiting List Entrants: " + maxWaitingListLimit);
                    Toast.makeText(EventDetailsActivity.this, "Max waiting list limit updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("EventDetailsActivity", "Error updating max waiting list", e);
                    Toast.makeText(EventDetailsActivity.this, "Failed to update max waiting list", Toast.LENGTH_SHORT).show();
                });
    }
}
