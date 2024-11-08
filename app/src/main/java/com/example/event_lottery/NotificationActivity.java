package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private EditText etMessage; // Variable name consistent with XML
    private Button btnNotify, btnCancel;
    private FirebaseFirestore db;
    private ArrayList<String> selectedUsers; // List of selected user emails
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        etMessage = findViewById(R.id.et_message); // Match this ID with the XML
        btnNotify = findViewById(R.id.btn_notify);
        btnCancel = findViewById(R.id.btn_cancel);

        // Get the selected users and event ID from the intent
        Intent intent = getIntent();
        selectedUsers = intent.getStringArrayListExtra("selected_users");
        eventId = intent.getStringExtra("event_id");

        if (selectedUsers == null || selectedUsers.isEmpty()) {
            Toast.makeText(this, "No users selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Handle Notify button click
        btnNotify.setOnClickListener(v -> sendNotifications());

        // Handle Cancel button click
        btnCancel.setOnClickListener(v -> finish());
    }

    private void sendNotifications() {
        String customMessage = etMessage.getText().toString().trim();

        if (customMessage.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        // Iterate through the selected users and update Firestore
        for (String email : selectedUsers) {
            CollectionReference notificationsRef = db.collection("users")
                    .document(email)
                    .collection("notifications");

            DocumentReference newNotificationRef = notificationsRef.document();
            newNotificationRef.set(new NotificationModel(customMessage))
                    .addOnSuccessListener(aVoid -> Log.d("NotificationActivity", "Message sent to: " + email))
                    .addOnFailureListener(e -> Log.e("NotificationActivity", "Error sending message to: " + email, e));
        }

        Toast.makeText(this, "Notifications sent", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity
    }

    // Model class for the notification
    public static class NotificationModel {
        private String messages;

        public NotificationModel(String messages) {
            this.messages = messages;
        }

        public String getMessages() {
            return messages;
        }

        public void setMessages(String messages) {
            this.messages = messages;
        }
    }
}