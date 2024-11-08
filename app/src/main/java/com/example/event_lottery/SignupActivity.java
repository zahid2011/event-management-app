package com.example.event_lottery;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build; // Import for version checks
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull; // Correct imports
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignupActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;

    private EditText userIdEditText, emailEditText, usernameEditText, firstNameEditText, lastNameEditText, passwordEditText;
    private Button signupButton;
    private ImageButton backButton, cameraIcon;
    private Spinner roleSpinner;
    private String selectedRole;
    private FirebaseFirestore db;
    private ImageView profileImageView;
    private Uri imageUri;
    private StorageReference storageReference;

    // For permission handling
    private String requiredPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page); // Ensure this matches your layout file name

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        // Initialize UI elements
        userIdEditText = findViewById(R.id.userIdEditText);
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        backButton = findViewById(R.id.backButton);
        roleSpinner = findViewById(R.id.roleSpinner);
        profileImageView = findViewById(R.id.profileImage);
        cameraIcon = findViewById(R.id.cameraIcon);

        // Setup role spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.role_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Determine required permission based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requiredPermission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            requiredPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        // Image selection handlers
        cameraIcon.setOnClickListener(v -> checkPermissionAndOpenFileChooser());
        profileImageView.setOnClickListener(v -> checkPermissionAndOpenFileChooser());

        // Back button handler
        backButton.setOnClickListener(v -> finish());

        // Signup button handler
        signupButton.setOnClickListener(v -> {
            String userId = userIdEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String username = usernameEditText.getText().toString().trim();
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            selectedRole = roleSpinner.getSelectedItem().toString();

            if (userId.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Save user ID in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("USER_ID", userId);
                editor.apply();

                if (imageUri != null) {
                    uploadImageAndSaveUser(userId, email, username, firstName, lastName, password, selectedRole);
                } else {
                    saveUserInfo(userId, email, username, firstName, lastName, password, selectedRole, null);
                }
            }
        });
    }

    private void checkPermissionAndOpenFileChooser() {
        if (ContextCompat.checkSelfPermission(this, requiredPermission)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{requiredPermission}, STORAGE_PERMISSION_CODE);
        } else {
            // Permission already granted
            openFileChooser();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Restrict to images only
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Always call the superclass method first
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                openFileChooser();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied to access your external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the image was picked
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Display the image in the ImageView
            profileImageView.setImageURI(imageUri);
        }
    }

    private void uploadImageAndSaveUser(String userId, String email, String username, String firstName, String lastName, String password, String role) {
        // Use userId as the filename to ensure uniqueness
        StorageReference fileReference = storageReference.child(userId + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String profileImageUrl = uri.toString();
                        saveUserInfo(userId, email, username, firstName, lastName, password, role, profileImageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignupActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Proceed without the image
                    saveUserInfo(userId, email, username, firstName, lastName, password, role, null);
                });
    }

    private void saveUserInfo(String userId, String email, String username, String firstName, String lastName, String password, String role, @Nullable String profileImageUrl) {
        // Create a User object
        User userInfo = new User(userId, email, username, firstName, lastName, password, role, profileImageUrl);

        // Store the user information in Firestore
        db.collection("users").document(userId).set(userInfo)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(SignupActivity.this, "Failed to save user info: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
