package com.example.event_lottery;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;




public class EventDetailsActivity extends AppCompatActivity {
    private static final int IMAGE_UPLOAD_REQUEST_CODE = 1;

    TextView tvEventName;
    private TextView tvEventDate;
    private TextView tvEventDescription;
    private TextView tvEventCapacity;
    private TextView tvQrCodeLabel;
    private TextView tvMaxWaitingList;
    private ImageView ivBackArrow, imgEventImage, qrCodeImageView, editEventImg;
    private FirebaseFirestore db;
    private Button btnViewWaitingList, btnRunLottery, btnParticipantManagement;
    private String eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        //imgEventImage = findViewById(R.id.img_event_image);
        imgEventImage = findViewById(R.id.img_event_details_image);

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
        qrCodeImageView = findViewById(R.id.img_qr_code);
        btnViewWaitingList = findViewById(R.id.btn_view_waiting_list);
        editEventImg=findViewById(R.id.btn_edit_event_details_image);



        btnParticipantManagement = findViewById(R.id.btn_participant_management);

// Add OnClickListener for Participant Management button
        btnParticipantManagement.setOnClickListener(v -> {
            Log.d("EventDetailsActivity", "Navigating to ParticipantManagementActivity with Event ID: " + eventId);

            // Navigate to ParticipantManagementActivity
            Intent intent = new Intent(EventDetailsActivity.this, ParticipantManagementActivity.class);
            intent.putExtra("eventId", eventId); // Pass the event ID
            startActivity(intent);
        });


        editEventImg.setOnClickListener(v -> {
            Intent intent = new Intent(EventDetailsActivity.this, EventImageUploadActivity.class);
            startActivityForResult(intent, IMAGE_UPLOAD_REQUEST_CODE); // Open image upload activity
        });


        // Fetch event details from Firestore
        fetchEventDetails();

        // Set click listener for max waiting list TextView
        tvMaxWaitingList.setOnClickListener(v -> showMaxWaitingListDialog());

        // Set back arrow click listener to finish the activity
        ivBackArrow.setOnClickListener(v -> finish());

        btnRunLottery = findViewById(R.id.btn_run_lottery);


        btnRunLottery.setOnClickListener(v -> {
            Intent intent = new Intent(EventDetailsActivity.this, RunLotteryActivity.class);
            intent.putExtra("event_id", eventId); // Pass the event ID to the next activity
            startActivity(intent);
        });

        btnViewWaitingList.setOnClickListener(v -> {
            Log.d("EventDetailsActivity", "Navigating to WaitingListActivity with Event ID: " + eventId);
            Intent intent = new Intent(EventDetailsActivity.this, WaitingListActivity.class);
            intent.putExtra("event_id", eventId); // Pass the event ID to the next activity
            startActivity(intent);
        });
    }

    void fetchEventDetails() {
        DocumentReference docRef = db.collection("events").document(eventId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Retrieve and display event data
                    String eventName = document.getString("eventName");
                    Timestamp eventTimestamp = document.getTimestamp("eventDateTime");  // Retrieve as Timestamp
                    String description = document.getString("description");
                    String capacity = document.getString("capacity");
                    String qrContent = document.getString("qrContent");
                    String imageUrl = document.getString("imageUrl");

                    // Set data in views
                    tvEventName.setText(eventName != null ? eventName : "N/A");
                    tvEventDescription.setText("Event Description: " + (description != null ? description : "N/A"));
                    tvEventCapacity.setText("Capacity: " + (capacity != null ? capacity + " seats available" : "N/A"));
                    tvQrCodeLabel.setText("QR Code For The Event");



                    // Generate QR code image if qrhash exists
                    if (qrContent != null && !qrContent.isEmpty()) {
                        generateQRCodeImage(qrContent); // Generate QR code from content
                    } else {
                        qrCodeImageView.setImageDrawable(null); // Clear QR code image if no content
                    }

                    // Format date if available
                    if (eventTimestamp != null) {
                        Date eventDate = eventTimestamp.toDate();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        tvEventDate.setText("Date: " + dateFormat.format(eventDate));
                    } else {
                        tvEventDate.setText("Date: N/A");
                    }

                    // Only set maxWaitingList if it exists in the document
                    if (document.contains("maxWaitingList")) {
                        Long maxWaitingList = document.getLong("maxWaitingList");
                        tvMaxWaitingList.setText("Max Waiting List Entrants: " + maxWaitingList);
                    } else {
                        tvMaxWaitingList.setText("Max Waiting List Entrants: [Tap to Set]");
                    }

                    // Fetch and load the image from the URL
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        loadImageFromUrl(imageUrl);
                    } else {
                        imgEventImage.setImageResource(R.drawable.ic_image_placeholder);
                        Toast.makeText(this, "No image available for this event", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Event details not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Log.e("EventDetailsActivity", "Error fetching document", task.getException());
                Toast.makeText(this, "Error loading event details", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }




    private void generateQRCodeImage(String qrhash) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrhash, BarcodeFormat.QR_CODE, 200, 200);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show();
        }
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
                int newMaxWaitingListLimit = Integer.parseInt(inputText);
                validateAndUpdateMaxWaitingListLimit(newMaxWaitingListLimit); // Validate and update the limit
            } else {
                Toast.makeText(EventDetailsActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    void validateAndUpdateMaxWaitingListLimit(int newMaxWaitingListLimit) {
        // Reference to the waitingList subcollection for the current event
        CollectionReference waitingListRef = db.collection("events").document(eventId).collection("waitingList");

        // Fetch all documents in the waitingList subcollection
        waitingListRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int currentWaitingListSize = task.getResult().size(); // Get the current waiting list size

                // Check if the new max limit is greater than the current waiting list size
                if (newMaxWaitingListLimit > currentWaitingListSize) {
                    // Proceed to update the max waiting list limit
                    updateMaxWaitingListLimit(newMaxWaitingListLimit);
                } else {
                    // Show an error message if the new max limit is not valid
                    Toast.makeText(EventDetailsActivity.this,
                            "Max waiting list limit must be greater than the current waiting list size: " +
                                    currentWaitingListSize,
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e("EventDetailsActivity", "Error fetching waiting list size", task.getException());
                Toast.makeText(this, "Failed to fetch waiting list size.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e("EventDetailsActivity", "Error fetching waiting list size", e);
            Toast.makeText(this, "Failed to validate waiting list limit.", Toast.LENGTH_SHORT).show();
        });
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


    // Inside EventDetailsActivity.java

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_UPLOAD_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                try {
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    saveImageToFirestore(selectedImage);
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    void saveImageToFirestore(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("event_images/" + System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storageRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            // Save the image URL in Firestore
                            saveImageUrlToFirestore(uri.toString());
                            loadImageFromUrl(uri.toString());
                            Toast.makeText(EventDetailsActivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                        }))
                .addOnFailureListener(e -> {
                    Toast.makeText(EventDetailsActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

    private void loadImageFromUrl(String url) {
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_image_placeholder) // Add a default placeholder
                .error(R.drawable.ic_error) // Add an error placeholder
                .into(imgEventImage);
    }

    private void saveImageUrlToFirestore(String imageUrl) {
        DocumentReference docRef = db.collection("events").document(eventId);
        docRef.update("imageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Image updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update image URL", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

}
