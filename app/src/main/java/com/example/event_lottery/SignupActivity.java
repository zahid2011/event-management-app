package com.example.event_lottery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {
    private EditText emailEditText, usernameEditText, firstNameEditText, lastNameEditText, passwordEditText, phoneNumberEditText;
    private Button signupButton;
    private ImageButton backButton, cameraIcon;
    private Spinner roleSpinner;
    private String selectedRole;
    private FirebaseFirestore db;
    private ImageView profileImageView;
    private Uri imageUri;
    private StorageReference storageRef;
    private ProgressDialog progressDialog;

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image picker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        signupButton = findViewById(R.id.save_changes);
        backButton = findViewById(R.id.backButton);
        roleSpinner = findViewById(R.id.roleSpinner);
        profileImageView = findViewById(R.id.profileImage);
        cameraIcon = findViewById(R.id.cameraIcon);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing up...");
        progressDialog.setCancelable(false);

        // Setting up the spinner with role options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.role_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedRole = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRole = null;
            }
        });

        backButton.setOnClickListener(v -> finish());

        cameraIcon.setOnClickListener(v -> openFileChooser());

        signupButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();

            if (!areFieldsFilled(email, password, username, firstName, lastName, selectedRole)) {
                Toast.makeText(SignupActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else if (!isEmailValid(email)) {
                Toast.makeText(SignupActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            } else if (!isPasswordValid(password)) {
                Toast.makeText(SignupActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show(); // Show progress dialog

                // Generate a unique ID for the user
                String userId = UUID.randomUUID().toString();

                if (imageUri != null) {
                    // If image is selected, upload it first
                    uploadImageAndSaveUserInfo(userId, email, username, firstName, lastName, password, selectedRole, phoneNumber);
                } else {
                    // No image selected, save user info directly
                    saveUserInfo(userId, email, username, firstName, lastName, password, selectedRole, phoneNumber, null);
                }
            }
        });
    }

    // Validation Methods
    public boolean areFieldsFilled(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmailValid(String email) {
        // Simple regex for email validation
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isPasswordValid(String password) {
        // Password should be at least 6 characters
        return password != null && password.length() >= 6;
    }

    private void saveUserInfo(String id, String email, String username, String firstName, String lastName, String password, String role, String phoneNumber, String profileImageUrl) {
        // Create a User object with id as the first parameter
        User userInfo = new User(id, email, username, firstName, lastName, password, role);

        if (profileImageUrl != null) {
            userInfo.setProfileImageUrl(profileImageUrl);
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            userInfo.setPhoneNumber(phoneNumber);
        }

        // Store the user information in Firestore, using the email as the document ID
        db.collection("users").document(email).set(userInfo)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss(); // Hide progress dialog
                    Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss(); // Hide progress dialog
                    Toast.makeText(SignupActivity.this, "Failed to save user info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // For Image selection
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();

            try {
                // Display the image in the ImageView with rounded corners
                Glide.with(this)
                        .load(imageUri)
                        .apply(RequestOptions.circleCropTransform()) // Make the image rounded
                        .into(profileImageView);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageAndSaveUserInfo(String id, String email, String username, String firstName, String lastName, String password, String role, String phoneNumber) {
        // Create a reference to 'profile_images/email.jpg'
        StorageReference fileReference = storageRef.child("profile_images/" + email + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();

                        // Save user info with profileImageUrl
                        saveUserInfo(id, email, username, firstName, lastName, password, role, phoneNumber, downloadUrl);

                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss(); // Hide progress dialog
                        Toast.makeText(SignupActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                    });

                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss(); // Hide progress dialog
                    Toast.makeText(SignupActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }
}
