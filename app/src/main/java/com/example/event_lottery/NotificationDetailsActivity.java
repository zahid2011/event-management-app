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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotificationDetailsActivity extends AppCompatActivity {

    private TextView messageTextView;
    private TextView statusTextView;
    private Button acceptButton;
    private Button rejectButton;
    private Button tryAgainButton;
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

        messageTextView = findViewById(R.id.notification_message);
        statusTextView = findViewById(R.id.notification_status);
        acceptButton = findViewById(R.id.accept_button);
        rejectButton = findViewById(R.id.reject_button);
        tryAgainButton = findViewById(R.id.try_again_button);
        backButton = findViewById(R.id.back_button);

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

        // Depending on status, show appropriate buttons
        if (status == 1) {
            acceptButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);
            tryAgainButton.setVisibility(View.GONE);
        } else if (status == 0) {
            acceptButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
            tryAgainButton.setVisibility(View.VISIBLE);
        }

        acceptButton.setOnClickListener(v -> {
            // Handle accept action
            db.collection("events").document(eventName)
                    .collection("selectedEntrants").document(userId)
                    .set(new StatusObject(1), SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        // Delete the notification
                        deleteNotification();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NotificationDetailsActivity.this, "Error accepting: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        rejectButton.setOnClickListener(v -> {
            db.collection("events").document(eventName)
                    .collection("selectedEntrants").document(userId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Add to cancelledEntrants collection
                        Map<String, Object> cancelledData = new HashMap<>();
                        cancelledData.put("userId", userId);
                        cancelledData.put("status", 0);
                        cancelledData.put("timestamp", new Date());

                        db.collection("events").document(eventName)
                                .collection("cancelledEntrants")
                                .document(userId)
                                .set(cancelledData)
                                .addOnSuccessListener(cancelledAcknowledge -> {
                                    // Remove user from waitingList if present
                                    db.collection("events").document(eventName)
                                            .collection("waitingList").document(userId)
                                            .delete()
                                            .addOnSuccessListener(waitingListRemoval -> {
                                                // Delete the notification
                                                deleteNotification();

                                                Toast.makeText(NotificationDetailsActivity.this,
                                                        "You have been removed and added to cancelled entrants.",
                                                        Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(NotificationDetailsActivity.this,
                                                        "Error removing from waiting list: " + e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(NotificationDetailsActivity.this,
                                            "Error adding to cancelled entrants: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NotificationDetailsActivity.this,
                                "Error rejecting invitation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        tryAgainButton.setOnClickListener(v -> {
            // Handle try again action
            db.collection("events").document(eventName)
                    .collection("waitingList").document(userId)
                    .set(new StatusObject(1), SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        // Delete the notification
                        deleteNotification();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NotificationDetailsActivity.this, "Error trying again: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        backButton.setOnClickListener(v -> finish());
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

    // Create a simple class to hold the status
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
