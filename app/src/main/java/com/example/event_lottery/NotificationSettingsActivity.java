package com.example.event_lottery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationSettingsActivity extends AppCompatActivity {

    private SwitchCompat switchWinLottery;
    private SwitchCompat switchLoseLottery;
    private SwitchCompat switchAdminOrganizer;
    private String userId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        // Initialize back button and set click listener
        ImageButton backButton = findViewById(R.id.backButton2);
        backButton.setOnClickListener(v -> finish()); // Closes the activity to go back

        // Initialize switches

        switchAdminOrganizer = findViewById(R.id.switch_admin_organizer);

        // Retrieve user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", null);

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "No user ID found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Load switch states from Firebase
        loadSwitchStates();

        // Set listeners to handle switch state changes


        switchAdminOrganizer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle "Admin/Organizer" switch toggle
            saveSwitchStateToFirebase(isChecked);
        });
    }

    // Method to save switch state in Firebase
    private void saveSwitchStateToFirebase(boolean doNotReceiveNotifications) {
        db.collection("users").document(userId)
                .update("doNotReceiveAdminNotifications", doNotReceiveNotifications)
                .addOnSuccessListener(aVoid -> {
                    // Successfully updated
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update notification settings", Toast.LENGTH_SHORT).show();
                });
    }

    // Method to load switch states from Firebase
    private void loadSwitchStates() {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Boolean doNotReceiveAdminNotifications = documentSnapshot.getBoolean("doNotReceiveAdminNotifications");
                        if (doNotReceiveAdminNotifications != null) {
                            switchAdminOrganizer.setChecked(doNotReceiveAdminNotifications);
                        } else {
                            switchAdminOrganizer.setChecked(false); // Default is to receive notifications
                        }

                        // Optionally load other switches if needed
                        // For now, we don't need to load win/lose lottery switches
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load notification settings", Toast.LENGTH_SHORT).show();
                });
    }
}
