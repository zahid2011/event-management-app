package com.example.event_lottery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class QRCodeDetailsActivity extends AppCompatActivity {
    private TextView eventNameTextView, eventDateTextView, eventCapacityTextView, eventPriceTextView, eventDescriptionTextView, geoLocationTextView;
    private ImageView qrCodeImageView;
    private Button deleteQRCodeButton;
    private FirebaseFirestore db;
    private String eventId; // unique identifier for the event document in Firestore

    /**
     * Called when the activity is created. Initializes the views and loads event data from Firestore.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the data it most recently supplied. Otherwise, it is null.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_details_page);

        db = FirebaseFirestore.getInstance();

        // Views
        eventNameTextView = findViewById(R.id.event_name);
        eventDateTextView = findViewById(R.id.event_date);
        eventCapacityTextView = findViewById(R.id.event_capacity);
        eventPriceTextView = findViewById(R.id.event_price);
        eventDescriptionTextView = findViewById(R.id.event_description);
        geoLocationTextView = findViewById(R.id.event_geo_location);
        qrCodeImageView = findViewById(R.id.qr_code_image);
        deleteQRCodeButton = findViewById(R.id.remove_qr_button);

        // Get eventId from intent
        eventId = getIntent().getStringExtra("eventId");

        if (eventId != null && !eventId.isEmpty()) {
            loadEventData(eventId);
        } else {
            Toast.makeText(this, "Error: Missing Event ID.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Back button functionality
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Delete QR code functionality
        deleteQRCodeButton.setOnClickListener(v -> deleteQRCode());
    }

    /**
     * Loads the event data from Firestore using the provided event ID and updates the UI.
     *
     * @param eventId The unique identifier of the event in Firestore.
     */

    private void loadEventData(String eventId) {
        db.collection("events").document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        displayEventData(documentSnapshot);
                    } else {
                        Toast.makeText(QRCodeDetailsActivity.this, "Event not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(QRCodeDetailsActivity.this, "Error loading event data", Toast.LENGTH_SHORT).show()
                );
    }

    /**
     * Displays the details of the event on the screen, including generating and showing the QR code.
     *
     * @param documentSnapshot The Firestore document snapshot containing the event details.
     */

    private void displayEventData(DocumentSnapshot documentSnapshot) {
        // Retrieve and display event data
        String eventName = documentSnapshot.getString("eventName");
        Timestamp eventDateTime = documentSnapshot.getTimestamp("eventDateTime");
        String eventCapacity = documentSnapshot.getString("capacity");
        String eventPrice = documentSnapshot.getString("price");
        String eventDescription = documentSnapshot.getString("description");
        boolean geoLocationEnabled = documentSnapshot.getBoolean("geoLocationEnabled") != null
                ? documentSnapshot.getBoolean("geoLocationEnabled")
                : false;
        String qrContent = documentSnapshot.getString("qrContent");

        // Format the date and time
        String formattedDateTime = "N/A";
        if (eventDateTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy 'at' hh:mm a", Locale.getDefault());
            formattedDateTime = sdf.format(eventDateTime.toDate());
        }

        // Update UI elements
        eventNameTextView.setText(eventName != null ? eventName : "No Name");
        eventDateTextView.setText("Date & Time: " + formattedDateTime);
        eventCapacityTextView.setText(eventCapacity != null ? "Capacity: " + eventCapacity : "Capacity: N/A");
        eventPriceTextView.setText(eventPrice != null ? "Price: $" + eventPrice : "Price: N/A");
        eventDescriptionTextView.setText(eventDescription != null ? "Description: " + eventDescription : "Description: N/A");
        geoLocationTextView.setText("Geo-location: " + (geoLocationEnabled ? "Enabled" : "Disabled"));

        // Generate QR Code and display it
        if (qrContent != null && !qrContent.isEmpty()) {
            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap qrCodeBitmap = barcodeEncoder.encodeBitmap(qrContent, BarcodeFormat.QR_CODE, 150, 150);
                qrCodeImageView.setImageBitmap(qrCodeBitmap);
            } catch (WriterException e) {
                Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Deletes the QR code associated with the event by setting its fields to null in Firestore.
     */

    private void deleteQRCode() {
        if (eventId != null && !eventId.isEmpty()) {
            db.collection("events").document(eventId)
                    .update("qrContent", null, "qrhash", null)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(QRCodeDetailsActivity.this, "QR Code deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(QRCodeDetailsActivity.this, "Error deleting QR Code", Toast.LENGTH_SHORT).show()
                    );
        } else {
            Toast.makeText(this, "Event ID is missing, cannot delete QR Code.", Toast.LENGTH_SHORT).show();
        }
    }
}
