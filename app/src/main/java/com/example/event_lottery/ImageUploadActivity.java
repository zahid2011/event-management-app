//package com.example.event_lottery;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ImageUploadActivity extends AppCompatActivity {
//
//    private static final String TAG = "ImageUploadActivity";
//    private static final int PICK_IMAGE_REQUEST = 1;
//
//    private ImageView ivProfileImagePreview;
//    private Button btnUploadProfileImage;
//    private Button btnRemoveProfileImage;
//    private ImageButton backButton;
//
//    private Uri imageUri;
//
//    private FirebaseFirestore db;
//    private StorageReference storageRef;
//    private String userId; // Use email as the user ID
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.image_upload_screen);
//
//        // Retrieve the user ID (email) from SharedPreferences
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        userId = sharedPreferences.getString("USER_ID", null);
//
//        if (userId == null || userId.isEmpty()) {
//            Toast.makeText(ImageUploadActivity.this, "No user ID found", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // Initialize Firestore and Storage
//        db = FirebaseFirestore.getInstance();
//        storageRef = FirebaseStorage.getInstance().getReference();
//
//        // Initialize UI elements
//        ivProfileImagePreview = findViewById(R.id.ivProfileImagePreview);
//        btnUploadProfileImage = findViewById(R.id.btnUploadProfileImage);
//        btnRemoveProfileImage = findViewById(R.id.btnRemoveProfileImage);
//        backButton = findViewById(R.id.backButton2);
//
//        // Load existing profile image if available
//        db.collection("users").document(userId).get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
//                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
//                            Glide.with(this).load(profileImageUrl).into(ivProfileImagePreview);
//                        } else {
//                            ivProfileImagePreview.setImageResource(android.R.drawable.ic_menu_gallery);
//                        }
//                    }
//                })
//                .addOnFailureListener(e -> Log.e(TAG, "Error loading profile image", e));
//
//        // Handle "Upload Picture" button click
//        btnUploadProfileImage.setOnClickListener(v -> openFileChooser());
//
//        // Handle "Remove Picture" button click
//        btnRemoveProfileImage.setOnClickListener(v -> removeProfileImage());
//
//        // Handle "Back" button click
//        backButton.setOnClickListener(v -> finish());
//    }
//
//    private void openFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // For Image selection
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
//                && data != null && data.getData() != null) {
//
//            imageUri = data.getData();
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                ivProfileImagePreview.setImageBitmap(bitmap);
//
//                // Upload image to Firebase Storage
//                uploadImageToFirebaseStorage();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void uploadImageToFirebaseStorage() {
//        if (imageUri != null) {
//            // Create a reference to 'profile_images/userId.jpg'
//            StorageReference fileReference = storageRef.child("profile_images/" + userId + ".jpg");
//
//            // Upload the image
//            fileReference.putFile(imageUri)
//                    .addOnSuccessListener(taskSnapshot -> {
//                        // Get the download URL
//                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
//                            String downloadUrl = uri.toString();
//
//                            // Update the user's document in Firestore
//                            Map<String, Object> updates = new HashMap<>();
//                            updates.put("profileImageUrl", downloadUrl);
//
//                            db.collection("users").document(userId).update(updates)
//                                    .addOnSuccessListener(aVoid -> {
//                                        Toast.makeText(ImageUploadActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        Log.e(TAG, "Error updating Firestore", e);
//                                        Toast.makeText(ImageUploadActivity.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
//                                    });
//
//                        }).addOnFailureListener(e -> {
//                            Log.e(TAG, "Failed to get download URL", e);
//                            Toast.makeText(ImageUploadActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
//                        });
//
//                    })
//                    .addOnFailureListener(e -> {
//                        Log.e(TAG, "Failed to upload image to Storage", e);
//                        Toast.makeText(ImageUploadActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
//                    });
//        } else {
//            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void removeProfileImage() {
//        // Remove the image URL from Firestore
//        Map<String, Object> updates = new HashMap<>();
//        updates.put("profileImageUrl", FieldValue.delete());
//
//        db.collection("users").document(userId).update(updates)
//                .addOnSuccessListener(aVoid -> {
//                    // Delete the image from Firebase Storage
//                    StorageReference fileReference = storageRef.child("profile_images/" + userId + ".jpg");
//                    fileReference.delete()
//                            .addOnSuccessListener(aVoid1 -> {
//                                ivProfileImagePreview.setImageResource(android.R.drawable.ic_menu_gallery);
//                                Toast.makeText(ImageUploadActivity.this, "Profile image removed", Toast.LENGTH_SHORT).show();
//                            })
//                            .addOnFailureListener(e -> {
//                                Log.e(TAG, "Failed to delete image from Storage", e);
//                                Toast.makeText(ImageUploadActivity.this, "Failed to delete image from storage", Toast.LENGTH_SHORT).show();
//                            });
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "Failed to remove profile image from Firestore", e);
//                    Toast.makeText(ImageUploadActivity.this, "Failed to remove profile image", Toast.LENGTH_SHORT).show();
//                });
//    }
//}

//package com.example.event_lottery;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ImageUploadActivity extends AppCompatActivity {
//
//    private static final String TAG = "ImageUploadActivity";
//    private static final int PICK_IMAGE_REQUEST = 1;
//
//    private ImageView ivProfileImagePreview;
//    private Button btnUploadProfileImage;
//    private Button btnRemoveProfileImage;
//    private ImageButton backButton;
//
//    private Uri imageUri;
//
//    private FirebaseFirestore db;
//    private StorageReference storageRef;
//    private String userId; // Use email as the user ID
//
//    private ProgressDialog progressDialog; // Progress dialog for loading prompt
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.image_upload_screen);
//
//        // Retrieve the user ID (email) from SharedPreferences
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        userId = sharedPreferences.getString("USER_ID", null);
//
//        if (userId == null || userId.isEmpty()) {
//            Toast.makeText(ImageUploadActivity.this, "No user ID found", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // Initialize Firestore and Storage
//        db = FirebaseFirestore.getInstance();
//        storageRef = FirebaseStorage.getInstance().getReference();
//
//        // Initialize UI elements
//        ivProfileImagePreview = findViewById(R.id.ivProfileImagePreview);
//        btnUploadProfileImage = findViewById(R.id.btnUploadProfileImage);
//        btnRemoveProfileImage = findViewById(R.id.btnRemoveProfileImage);
//        backButton = findViewById(R.id.backButton2);
//
//        // Initialize ProgressDialog
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Uploading image...");
//        progressDialog.setCancelable(false);
//
//        // Load existing profile image if available
//        loadProfileImage();
//
//        // Handle "Upload Picture" button click
//        btnUploadProfileImage.setOnClickListener(v -> openFileChooser());
//
//        // Handle "Remove Picture" button click
//        btnRemoveProfileImage.setOnClickListener(v -> removeProfileImage());
//
//        // Handle "Back" button click
//        backButton.setOnClickListener(v -> finish());
//    }
//
//    private void loadProfileImage() {
//        db.collection("users").document(userId).get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
//                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
//                            Glide.with(this).load(profileImageUrl).into(ivProfileImagePreview);
//                        } else {
//                            ivProfileImagePreview.setImageResource(android.R.drawable.ic_menu_gallery);
//                        }
//                    }
//                })
//                .addOnFailureListener(e -> Log.e(TAG, "Error loading profile image", e));
//    }
//
//    private void openFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // For Image selection
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
//                && data != null && data.getData() != null) {
//
//            imageUri = data.getData();
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                ivProfileImagePreview.setImageBitmap(bitmap);
//
//                // Upload image to Firebase Storage
//                uploadImageToFirebaseStorage();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void uploadImageToFirebaseStorage() {
//        if (imageUri != null) {
//            progressDialog.show(); // Show loading prompt
//
//            // Create a reference to 'profile_images/userId.jpg'
//            StorageReference fileReference = storageRef.child("profile_images/" + userId + ".jpg");
//
//            // Upload the image
//            fileReference.putFile(imageUri)
//                    .addOnSuccessListener(taskSnapshot -> {
//                        // Get the download URL
//                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
//                            String downloadUrl = uri.toString();
//
//                            // Update the user's document in Firestore
//                            Map<String, Object> updates = new HashMap<>();
//                            updates.put("profileImageUrl", downloadUrl);
//
//                            db.collection("users").document(userId).update(updates)
//                                    .addOnSuccessListener(aVoid -> {
//                                        progressDialog.dismiss(); // Hide loading prompt
//                                        Toast.makeText(ImageUploadActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();
//
//                                        // Set result to indicate the profile image was updated
//                                        setResult(RESULT_OK);
//
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        progressDialog.dismiss(); // Hide loading prompt
//                                        Log.e(TAG, "Error updating Firestore", e);
//                                        Toast.makeText(ImageUploadActivity.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
//                                    });
//
//                        }).addOnFailureListener(e -> {
//                            progressDialog.dismiss(); // Hide loading prompt
//                            Log.e(TAG, "Failed to get download URL", e);
//                            Toast.makeText(ImageUploadActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
//                        });
//
//                    })
//                    .addOnFailureListener(e -> {
//                        progressDialog.dismiss(); // Hide loading prompt
//                        Log.e(TAG, "Failed to upload image to Storage", e);
//                        Toast.makeText(ImageUploadActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
//                    });
//        } else {
//            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void removeProfileImage() {
//        progressDialog.setMessage("Removing image...");
//        progressDialog.show(); // Show loading prompt
//
//        // Remove the image URL from Firestore
//        Map<String, Object> updates = new HashMap<>();
//        updates.put("profileImageUrl", FieldValue.delete());
//
//        db.collection("users").document(userId).update(updates)
//                .addOnSuccessListener(aVoid -> {
//                    // Delete the image from Firebase Storage
//                    StorageReference fileReference = storageRef.child("profile_images/" + userId + ".jpg");
//                    fileReference.delete()
//                            .addOnSuccessListener(aVoid1 -> {
//                                progressDialog.dismiss(); // Hide loading prompt
//                                ivProfileImagePreview.setImageResource(android.R.drawable.ic_menu_gallery);
//                                Toast.makeText(ImageUploadActivity.this, "Profile image removed", Toast.LENGTH_SHORT).show();
//
//                                // Set result to indicate the profile image was removed
//                                setResult(RESULT_OK);
//
//                            })
//                            .addOnFailureListener(e -> {
//                                progressDialog.dismiss(); // Hide loading prompt
//                                Log.e(TAG, "Failed to delete image from Storage", e);
//                                Toast.makeText(ImageUploadActivity.this, "Failed to delete image from storage", Toast.LENGTH_SHORT).show();
//                            });
//                })
//                .addOnFailureListener(e -> {
//                    progressDialog.dismiss(); // Hide loading prompt
//                    Log.e(TAG, "Failed to remove profile image from Firestore", e);
//                    Toast.makeText(ImageUploadActivity.this, "Failed to remove profile image", Toast.LENGTH_SHORT).show();
//                });
//    }
//}




package com.example.event_lottery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUploadActivity extends AppCompatActivity {

    private static final String TAG = "ImageUploadActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivProfileImagePreview;
    private Button btnUploadProfileImage;
    private Button btnRemoveProfileImage;
    private ImageButton backButton;

    private Uri imageUri;

    private FirebaseFirestore db;
    private StorageReference storageRef;
    private String userId; // Use email as the user ID

    private ProgressDialog progressDialog; // Progress dialog for loading prompt

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload_screen);

        // Retrieve the user ID (email) from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", null);

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(ImageUploadActivity.this, "No user ID found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firestore and Storage
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // Initialize UI elements
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
                            ivProfileImagePreview.setImageResource(R.drawable.ic_image_placeholder); // Use default image
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error loading profile image", e));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // For Image selection
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();

            try {
                // Show the selected image in the ImageView with circle crop
                Glide.with(this)
                        .load(imageUri)
                        .circleCrop()
                        .into(ivProfileImagePreview);

                // Upload image to Firebase Storage
                uploadImageToFirebaseStorage();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        if (imageUri != null) {
            progressDialog.show(); // Show loading prompt

            // Create a reference to 'profile_images/userId.jpg'
            StorageReference fileReference = storageRef.child("profile_images/" + userId + ".jpg");

            // Upload the image
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();

                            // Update the user's document in Firestore
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("profileImageUrl", downloadUrl);

                            db.collection("users").document(userId).update(updates)
                                    .addOnSuccessListener(aVoid -> {
                                        progressDialog.dismiss(); // Hide loading prompt
                                        Toast.makeText(ImageUploadActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();

                                        // Set result to indicate the profile image was updated
                                        setResult(RESULT_OK);
                                        finish(); // Close the activity

                                    })
                                    .addOnFailureListener(e -> {
                                        progressDialog.dismiss(); // Hide loading prompt
                                        Log.e(TAG, "Error updating Firestore", e);
                                        Toast.makeText(ImageUploadActivity.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
                                    });

                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss(); // Hide loading prompt
                            Log.e(TAG, "Failed to get download URL", e);
                            Toast.makeText(ImageUploadActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                        });

                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss(); // Hide loading prompt
                        Log.e(TAG, "Failed to upload image to Storage", e);
                        Toast.makeText(ImageUploadActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeProfileImage() {
        progressDialog.setMessage("Removing image...");
        progressDialog.show(); // Show loading prompt

        // Remove the image URL from Firestore
        Map<String, Object> updates = new HashMap<>();
        updates.put("profileImageUrl", FieldValue.delete());

        db.collection("users").document(userId).update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Delete the image from Firebase Storage
                    StorageReference fileReference = storageRef.child("profile_images/" + userId + ".jpg");
                    fileReference.delete()
                            .addOnSuccessListener(aVoid1 -> {
                                progressDialog.dismiss(); // Hide loading prompt
                                ivProfileImagePreview.setImageResource(R.drawable.ic_image_placeholder); // Use default image
                                Toast.makeText(ImageUploadActivity.this, "Profile image removed", Toast.LENGTH_SHORT).show();

                                // Set result to indicate the profile image was removed
                                setResult(RESULT_OK);
                                finish(); // Close the activity

                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss(); // Hide loading prompt
                                Log.e(TAG, "Failed to delete image from Storage", e);
                                Toast.makeText(ImageUploadActivity.this, "Failed to delete image from storage", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss(); // Hide loading prompt
                    Log.e(TAG, "Failed to remove profile image from Firestore", e);
                    Toast.makeText(ImageUploadActivity.this, "Failed to remove profile image", Toast.LENGTH_SHORT).show();
                });
    }
}


