package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class NotificationDetailsActivity extends AppCompatActivity {

    private TextView messageTextView;
    private TextView statusTextView;
    private Button acceptButton;
    private Button rejectButton;
    private Button tryAgainButton;
    private Button cancelButton;  // Added Cancel button
    private ImageButton backButton;

    private String email;
    private String message;
    private int status;
    private String eventName;
    private String notificationId;

    private FirebaseFirestore db;
    private String userId; // User email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        // Initialize views
        messageTextView = findViewById(R.id.notification_message);
        statusTextView = findViewById(R.id.notification_status);
        acceptButton = findViewById(R.id.accept_button);
        rejectButton = findViewById(R.id.reject_button);
        tryAgainButton = findViewById(R.id.try_again_button);
        cancelButton = findViewById(R.id.cancel_button);  // Initialize Cancel button
        backButton = findViewById(R.id.back_button);

        // Set up back button listener
        backButton.setOnClickListener(v -> finish());

        db = FirebaseFirestore.getInstance();

        // Get userId (email) from SharedPreferences
        userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("USER_ID", null);

        // Get data from intent
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        message = intent.getStringExtra("message");
        status = intent.getIntExtra("status", 0);
        eventName = intent.getStringExtra("eventName");
        notificationId = intent.getStringExtra("notificationId");

        messageTextView.setText(message);
        statusTextView.setText("Status: " + status);

        // Show appropriate buttons based on status
        if (status == 1) {
            acceptButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);
            tryAgainButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
        } else if (status == 0) {
            acceptButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
            tryAgainButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);  // Show Cancel button
        }

        acceptButton.setOnClickListener(v -> {
            // Handle accept action
            db.collection("events").document(eventName)
                    .collection("selectedEntrants").document(userId)
                    .set(new StatusObject(1), SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        deleteNotification();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NotificationDetailsActivity.this, "Error accepting: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        rejectButton.setOnClickListener(v -> {
            // Handle reject action
            db.collection("events").document(eventName)
                    .collection("selectedEntrants").document(userId)
                    .set(new StatusObject(0), SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        deleteNotification();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NotificationDetailsActivity.this, "Error rejecting: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        tryAgainButton.setOnClickListener(v -> {
            // Handle try again action
            db.collection("events").document(eventName)
                    .collection("waitingList").document(userId)
                    .set(new StatusObject(1), SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        deleteNotification();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NotificationDetailsActivity.this, "Error trying again: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        cancelButton.setOnClickListener(v -> {
            // Handle cancel action
            db.collection("events").document(eventName)
                    .collection("waitingList").document(userId)
                    .set(new StatusObject(0), SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        deleteNotification();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NotificationDetailsActivity.this, "Error cancelling: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void deleteNotification() {
        db.collection("users").document(userId)
                .collection("notifications").document(notificationId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(NotificationDetailsActivity.this, "Action completed and notification deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(NotificationDetailsActivity.this, "Error deleting notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Helper class to hold the status
    public static class StatusObject {
        public int status;

        public StatusObject() {
            // Default constructor required
        }

        public StatusObject(int status) {
            this.status = status;
        }
    }
}
