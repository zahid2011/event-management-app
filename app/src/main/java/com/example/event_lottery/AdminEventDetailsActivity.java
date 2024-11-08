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
    private TextView eventNameTextView, eventDateTextView, eventCapacityTextView, eventPriceTextView, eventDescriptionTextView;
    private Button removeEventButton;
    private FirebaseFirestore db;
    private String eventId; // unique identifier for the event document in Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_event_details_page);

        // initializing Firestore
        db = FirebaseFirestore.getInstance();

       
        eventNameTextView = findViewById(R.id.event_name);
        eventDateTextView = findViewById(R.id.event_date);
        eventCapacityTextView = findViewById(R.id.event_capacity);
        eventPriceTextView = findViewById(R.id.event_price);
        removeEventButton = findViewById(R.id.remove_event_button);
        eventDescriptionTextView = findViewById(R.id.event_description);

    
        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");
        String eventName = intent.getStringExtra("eventName");
        String eventDate = intent.getStringExtra("eventDate");
        String eventCapacity = intent.getStringExtra("eventCapacity");
        String eventPrice = intent.getStringExtra("eventPrice");
        String eventDescription = intent.getStringExtra("eventDescription");



        eventNameTextView.setText(eventName);
        eventDateTextView.setText("Date: " + eventDate);
        eventDescriptionTextView.setText("Event Description: " + eventDescription);
        eventCapacityTextView.setText("Capacity: " + eventCapacity);
        eventPriceTextView.setText("Price: $" + eventPrice);


        // back button functionality
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // removing event button with confirmation dialog
        removeEventButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEvent(); // Call method to delete the event
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void deleteEvent() {
        // ensuring eventId is not null or empty before attempting to delete
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
