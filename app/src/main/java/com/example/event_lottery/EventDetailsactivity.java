package com.example.event_lottery;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.event_lottery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EventDetailsActivity extends AppCompatActivity {


    private TextView tvEventName, tvEventDate, tvEventDescription, tvEventCapacity, tvMaxWaitingList, tvQrCodeLabel;
    private ImageView imgQrCode, imgEventImage, ivBackArrow;
    private Switch switchGeolocation;
    private Button btnViewWaitingList, btnRunLottery, btnViewEntrantLocations;


    private DatabaseReference eventsDatabaseRef;
    private String eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);


        // Get the event ID passed from the previous activity
        eventId = getIntent().getStringExtra("eventName");


        // Initialize Firebase reference
        eventsDatabaseRef = FirebaseDatabase.getInstance().getReference("events");


        // Initialize views
        tvEventName = findViewById(R.id.tv_event_name);
        tvEventDate = findViewById(R.id.tv_event_date);
        tvEventDescription = findViewById(R.id.tv_event_description);
        tvEventCapacity = findViewById(R.id.tv_event_capacity);
        tvMaxWaitingList = findViewById(R.id.tv_max_waiting_list);
        imgQrCode = findViewById(R.id.img_qr_code);
        imgEventImage = findViewById(R.id.img_event_image);
        tvQrCodeLabel = findViewById(R.id.tv_qr_code_label);
        switchGeolocation = findViewById(R.id.switch_geolocation);
        btnViewWaitingList = findViewById(R.id.btn_view_waiting_list);
        btnRunLottery = findViewById(R.id.btn_run_lottery);
        btnViewEntrantLocations = findViewById(R.id.btn_view_entrant_locations);
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
                    String price = dataSnapshot.child("price").getValue(String.class);


                    // Set data in views
                    tvEventName.setText(eventName);
                    tvEventDate.setText("Date: " + eventDateTime);
                    tvEventDescription.setText("Event Description: " + description);
                    tvEventCapacity.setText("Capacity: " + capacity + " seats available");
                    tvQrCodeLabel.setText("QR Code for Event");


                    // Optional: Load QR code or other images if you have URLs
                    // Use a library like Glide or Picasso to load images from URLs
                    // Glide.with(EventDetails.this).load(qrhash).into(imgQrCode);


                    // Display any other details as required
                    // Example: tvPrice.setText("Price: $" + price);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }
}
