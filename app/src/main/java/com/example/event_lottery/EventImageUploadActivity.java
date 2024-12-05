package com.example.event_lottery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity for uploading and managing profile images for events.
 * <p>
 * Features include:
 * - Uploading a new image
 * - Previewing the uploaded image
 * - Removing an existing image
 * - Storing the image in Firebase Storage and updating Firestore
 * </p>
 */

public class EventImageUploadActivity extends AppCompatActivity {

    private static final String TAG = "EventImageUploadActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivProfileImagePreview;
    private Button btnUploadProfileImage;
    private Button btnRemoveProfileImage;
    private ImageButton backButton; // Matches backButton2 in XML

    private Uri imageUri;

    private FirebaseFirestore db;
    private StorageReference storageRef;
    private String userId;

    private ProgressDialog progressDialog;


    /**
     * Initializes the activity.
     * <p>
     * Sets up UI elements, Firebase references, and loads the current profile image.
     * </p>
     *
     * @param savedInstanceState The saved state of the activity
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_image_upload);

        // Retrieve the user ID (email) from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", null);

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(EventImageUploadActivity.this, "No user ID found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firestore and Storage
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // Initialize UI elements (IDs match XML)
        ivProfileImagePreview = findViewById(R.id.ivProfileImagePreview);
        btnUploadProfileImage = findViewById(R.id.btnUploadProfileImage);
        btnRemoveProfileImage = findViewById(R.id.btnRemoveProfileImage);
        backButton = findViewById(R.id.backButton2);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");
        progressDialog.setCancelable(false);

        // Load existing profile image if available
        loadProfileImage();

        // Handle "Upload Picture" button click
        btnUploadProfileImage.setOnClickListener(v -> openFileChooser());

        // Handle "Remove Picture" button click
        btnRemoveProfileImage.setOnClickListener(v -> removeProfileImage());

        // Handle "Back" button click
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Loads the current profile image from Firestore and displays it.
     */

    private void loadProfileImage() {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(profileImageUrl)
                                    .circleCrop() // Apply circle crop transformation
                                    .into(ivProfileImagePreview);
                        } else {
                            ivProfileImagePreview.setImageResource(R.drawable.ic_image_placeholder);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error loading profile image", e));
    }

    /**
     * Opens a file chooser for the user to select an image.
     */

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /**
     * Handles the result from the image picker activity.
     *
     * @param requestCode The request code used to start the activity
     * @param resultCode  The result code returned by the activity
     * @param data        The intent data containing the selected image
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();

            try {
                Glide.with(this)
                        .load(imageUri)
                        .circleCrop()
                        .into(ivProfileImagePreview);

                uploadImageToFirebaseStorage();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }


        }
    }

    /**
     * Uploads the selected image to Firebase Storage and updates Firestore with its URL.
     */

    private void uploadImageToFirebaseStorage() {
        if (imageUri != null) {
            progressDialog.show();

            StorageReference fileReference = storageRef.child("profile_images/" + userId + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();

                            Map<String, Object> updates = new HashMap<>();
                            updates.put("profileImageUrl", downloadUrl);

                            db.collection("users").document(userId).update(updates)
                                    .addOnSuccessListener(aVoid -> {
                                        progressDialog.dismiss();

                                        // Send the URL back to EventDetailsActivity
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("imageUrl", downloadUrl); // Pass the URL back
                                        setResult(RESULT_OK, resultIntent);

                                        Toast.makeText(EventImageUploadActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Log.e(TAG, "Error updating Firestore", e);
                                        Toast.makeText(EventImageUploadActivity.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
                                    });

                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Log.e(TAG, "Failed to get download URL", e);
                            Toast.makeText(EventImageUploadActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                        });

                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Log.e(TAG, "Failed to upload image to Storage", e);
                        Toast.makeText(EventImageUploadActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Removes the profile image from Firebase Storage and Firestore.
     */

    private void removeProfileImage() {
        progressDialog.setMessage("Removing image...");
        progressDialog.show();

        Map<String, Object> updates = new HashMap<>();
        updates.put("profileImageUrl", FieldValue.delete());

        db.collection("users").document(userId).update(updates)
                .addOnSuccessListener(aVoid -> {
                    StorageReference fileReference = storageRef.child("profile_images/" + userId + ".jpg");
                    fileReference.delete()
                            .addOnSuccessListener(aVoid1 -> {
                                progressDialog.dismiss();
                                ivProfileImagePreview.setImageResource(R.drawable.ic_image_placeholder);
                                Toast.makeText(EventImageUploadActivity.this, "Profile image removed", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();

                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Log.e(TAG, "Failed to delete image from Storage", e);
                                Toast.makeText(EventImageUploadActivity.this, "Failed to delete image from storage", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.e(TAG, "Failed to remove profile image from Firestore", e);
                    Toast.makeText(EventImageUploadActivity.this, "Failed to remove profile image", Toast.LENGTH_SHORT).show();
                });
    }
}
