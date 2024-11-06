package com.example.event_lottery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventDescription, tvEventCapacity, tvQrCodeLabel;
    private ImageView ivBackArrow;
    private DatabaseReference eventsDatabaseRef;
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

        // Initialize Firebase reference
        eventsDatabaseRef = FirebaseDatabase.getInstance().getReference("events");

        // Initialize views
        tvEventName = findViewById(R.id.tv_event_name);
        tvEventDate = findViewById(R.id.tv_event_date);
        tvEventDescription = findViewById(R.id.tv_event_description);
        tvEventCapacity = findViewById(R.id.tv_event_capacity);
        tvQrCodeLabel = findViewById(R.id.tv_qr_code_label);
        ivBackArrow = findViewById(R.id.iv_back_arrow);

        // Fetch event details from Firebase
        fetchEventDetails();

        // Set back arrow click listener to finish the activity
        ivBackArrow.setOnClickListener(v -> finish());
    }

    private void fetchEventDetails() {
        eventsDatabaseRef.child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve and display event data
                    String eventName = dataSnapshot.child("eventName").getValue(String.class);
                    String eventDateTime = dataSnapshot.child("eventDateTime").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String capacity = dataSnapshot.child("capacity").getValue(String.class);
                    String qrhash = dataSnapshot.child("qrhash").getValue(String.class);

                    // Set data in views
                    tvEventName.setText(eventName != null ? eventName : "N/A");
                    tvEventDate.setText("Date: " + (eventDateTime != null ? eventDateTime : "N/A"));
                    tvEventDescription.setText("Event Description: " + (description != null ? description : "N/A"));
                    tvEventCapacity.setText("Capacity: " + (capacity != null ? capacity + " seats available" : "N/A"));
                    tvQrCodeLabel.setText("QR Code: " + (qrhash != null ? qrhash : "N/A"));
                } else {
                    Log.e("EventDetailsActivity", "DataSnapshot does not exist for event ID: " + eventId);
                    Toast.makeText(EventDetailsActivity.this, "Event details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("EventDetailsActivity", "Database error: " + databaseError.getMessage());
                Toast.makeText(EventDetailsActivity.this, "Error loading event details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
