package com.example.event_lottery;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendNotificationActivity extends AppCompatActivity {

    private static final String TAG = "SendNotification";
    private static final String SERVER_KEY = "AAAAXYZ12345678abcdefghijklmnop"; // Replace with your actual server key
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    private TextView messageTextView;
    private Button notifyButton, cancelButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notifications);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve event ID from Intent
        String eventId = getIntent().getStringExtra("event_id");
        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Event ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI elements
        messageTextView = findViewById(R.id.card_message_area);
        notifyButton = findViewById(R.id.btn_notify);
        cancelButton = findViewById(R.id.btn_cancel);

        // Set default message
        messageTextView.setText("Congratulations! You've won the lottery.");

        // Notify button logic
        notifyButton.setOnClickListener(v -> fetchEmailsAndSendNotifications(eventId));

        // Cancel button logic
        cancelButton.setOnClickListener(v -> finish());
    }

    private void fetchEmailsAndSendNotifications(String eventId) {
        db.collection("events")
                .document(eventId)
                .collection("selectedEntrants")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> emails = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        emails = queryDocumentSnapshots.toObjects(UserEmail.class)
                                .stream()
                                .map(UserEmail::getEmail)
                                .toList();
                    }

                    if (!emails.isEmpty()) {
                        for (String email : emails) {
                            sendNotificationToUser(email, "Congratulations! You've won the lottery.");
                        }
                    } else {
                        Toast.makeText(this, "No selected participants found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching emails: ", e);
                    Toast.makeText(this, "Failed to fetch emails.", Toast.LENGTH_SHORT).show();
                });
    }

    private void sendNotificationToUser(String email, String message) {
        try {
            JSONObject json = new JSONObject();
            json.put("to", email); // Replace with Firebase Messaging token if required

            JSONObject notification = new JSONObject();
            notification.put("title", "Event Lottery");
            notification.put("body", message);

            json.put("notification", notification);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_URL, json,
                    response -> {
                        Toast.makeText(SendNotificationActivity.this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Response: " + response.toString());
                    },
                    error -> {
                        Toast.makeText(SendNotificationActivity.this, "Failed to send notification", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error: " + error.toString());
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "key=" + SERVER_KEY);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object: ", e);
            Toast.makeText(this, "Error sending notification", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper class for mapping Firestore email documents
    public static class UserEmail {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
