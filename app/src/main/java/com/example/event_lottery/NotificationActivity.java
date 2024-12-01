
package com.example.event_lottery;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private EditText etMessage;
    private Button btnNotify, btnCancel;
    private FirebaseFirestore db;
    private String eventId;
    private ArrayList<String> selectedEmails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        etMessage = findViewById(R.id.et_message);
        btnNotify = findViewById(R.id.btn_notify);
        btnCancel = findViewById(R.id.btn_cancel);

        // Get eventId from the intent
        eventId = getIntent().getStringExtra("event_id");
        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Event ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch selected entrants
        fetchSelectedEntrants();

        // Notify button logic
        btnNotify.setOnClickListener(v -> {
            String customMessage = etMessage.getText().toString().trim();
            if (customMessage.isEmpty()) {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
                return;
            }
            sendNotifications(customMessage);
        });

        // Cancel button logic
        btnCancel.setOnClickListener(v -> finish());
    }

    private void fetchSelectedEntrants() {
        selectedEmails = new ArrayList<>();
        CollectionReference selectedEntrantsRef = db.collection("events").document(eventId).collection("selectedEntrants");

        selectedEntrantsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DocumentSnapshot document : task.getResult()) {
                    String email = document.getString("email");
                    if (email != null) {
                        selectedEmails.add(email);
                    }
                }

                if (selectedEmails.isEmpty()) {
                    Toast.makeText(this, "No selected entrants found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Error fetching selected entrants", Toast.LENGTH_SHORT).show();
                Log.e("NotificationActivity", "Error: ", task.getException());
                finish();
            }
        });
    }

    private void sendNotifications(String customMessage) {
        if (selectedEmails.isEmpty()) {
            Toast.makeText(this, "No selected entrants to notify", Toast.LENGTH_SHORT).show();
            return;
        }

        for (String email : selectedEmails) {
            // Send notification to Firestore under each user's "notifications" subcollection
            CollectionReference notificationsRef = db.collection("notifications");
            notificationsRef.add(new NotificationModel(email, customMessage))
                    .addOnSuccessListener(documentReference -> {
                        Log.d("NotificationActivity", "Notification sent to: " + email);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("NotificationActivity", "Failed to send notification to: " + email, e);
                    });
        }

        Toast.makeText(this, "Notifications sent", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Model class for the notification
    public static class NotificationModel {
        private String email;
        private String message;

        public NotificationModel(String email, String message) {
            this.email = email;
            this.message = message;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
