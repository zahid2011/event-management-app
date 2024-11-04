package com.example.event_lottery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CreateEventActivity extends AppCompatActivity {

    private Button btnCreateEvent, btnGenerateQr, btnCancel, btnAddImage;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("event_posters");

        btnCreateEvent = findViewById(R.id.btn_create_event);
        btnGenerateQr = findViewById(R.id.btn_generate_qr);
        btnCancel = findViewById(R.id.btn_cancel);
        btnAddImage = findViewById(R.id.btn_add_image);

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadImageToFirebase();
                } else {
                    Toast.makeText(CreateEventActivity.this, "Please select an image for the event poster", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set onClickListener for the Generate QR Code button
        btnGenerateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action for Generate QR Code button
                Toast.makeText(CreateEventActivity.this, "QR Code Generated!", Toast.LENGTH_SHORT).show();
                //QR code generation functionality here if needed
            }
        });

        // onClickListener for the Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // This will close the current activity and return
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Toast.makeText(this, "Image selected successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            // Create a unique name for each image based on the time of upload
            StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpg");

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String posterUrl = uri.toString(); // Get the download URL
                        saveEventToFirestore(posterUrl); // Pass the URL to saveEventToFirestore
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(CreateEventActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEventToFirestore(String posterUrl) {
        // Replace with actual values from your UI elements, e.g., EditTexts for event name, date, etc.
        String eventName = "Sample Event"; // Example placeholder, get the actual value from EditText
        String date = "Sample Date";       // Example placeholder, get the actual value from DatePicker or EditText
        int capacity = 100;                // Example placeholder, get the actual value from EditText or NumberPicker
        double price = 50.0;               // Example placeholder, get the actual value from EditText

        // Create an Event object
        Event event = new Event(eventName, date, capacity, price, posterUrl);

        db.collection("Events").add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(CreateEventActivity.this, "Event Created!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(CreateEventActivity.this, "Failed to create event", Toast.LENGTH_SHORT).show());
    }
}
