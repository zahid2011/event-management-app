package com.example.event_lottery;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.event_lottery.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class EventDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView eventTitle, eventDate, eventDescription, eventCapacity;
    private ImageView eventImage;
    private Switch geolocationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        eventTitle = findViewById(R.id.tv_event_title);
        eventDate = findViewById(R.id.tv_event_date);
        eventDescription = findViewById(R.id.tv_event_description);
        eventCapacity = findViewById(R.id.tv_event_capacity);
        eventImage = findViewById(R.id.img_event_image);
        geolocationSwitch = findViewById(R.id.switch_geolocation);

        // Get event ID from the Intent
        String eventId = getIntent().getStringExtra("event_id");
        if (eventId != null) {
            loadEventDetails(eventId);
        } else {
            Toast.makeText(this, "Event ID is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadEventDetails(String eventId) {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> populateEventDetails(documentSnapshot))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void populateEventDetails(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
            // Populate the layout with Firestore data, with null checks
            eventTitle.setText(documentSnapshot.getString("eventName"));

            // Format date if it's available
            if (documentSnapshot.getDate("dateTime") != null) {
                eventDate.setText("Date: " + documentSnapshot.getDate("dateTime").toString());
            } else {
                eventDate.setText("Date: Not available");
            }

            // Populate capacity and description with null checks
            eventCapacity.setText("Capacity: " + (documentSnapshot.getString("capacity") != null ?
                    documentSnapshot.getString("capacity") : "Not specified"));
            eventDescription.setText("Description: " + (documentSnapshot.getString("description") != null ?
                    documentSnapshot.getString("description") : "No description available"));

            // Optional: Load image or other details if available
            // If your event image is a URL, you could use a library like Picasso or Glide to load it into eventImage
        } else {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
        }
    }
}
