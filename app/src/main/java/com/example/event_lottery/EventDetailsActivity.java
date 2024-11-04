package com.example.event_lottery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.event_lottery.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventDetailsActivity extends AppCompatActivity {

    private Button btnUpdateImage;
    private Uri newImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private String eventId = "YOUR_EVENT_ID"; // Replace with actual event ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("event_posters");

        btnUpdateImage = findViewById(R.id.btn_update_image);

        btnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            newImageUri = data.getData();
            uploadNewImageToFirebase();
        }
    }

    private void uploadNewImageToFirebase() {
        if (newImageUri != null) {
            StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpg");

            fileRef.putFile(newImageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String newPosterUrl = uri.toString();
                        updateEventPosterUrl(newPosterUrl);
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(EventDetailsActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEventPosterUrl(String newPosterUrl) {
        db.collection("Events").document(eventId)
                .update("posterUrl", newPosterUrl)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EventDetailsActivity.this, "Event poster updated!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EventDetailsActivity.this, "Failed to update event poster", Toast.LENGTH_SHORT).show();
                });
    }
}
