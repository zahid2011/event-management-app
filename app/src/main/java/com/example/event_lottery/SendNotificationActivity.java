package com.example.event_lottery;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class SendNotificationActivity extends AppCompatActivity {

    private TextView messageTextView;
    private Button notifyButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notifications);

        // Initialize UI elements
        messageTextView = findViewById(R.id.card_message_area);  // Reference message area
        notifyButton = findViewById(R.id.btn_notify);
        cancelButton = findViewById(R.id.btn_cancel);

        // Set default message in message area
        messageTextView.setText("Congratulations! You've won the lottery.");

        // Notify button logic
        notifyButton.setOnClickListener(v -> {
            String message = messageTextView.getText().toString();
            sendNotificationToUser(message);  // Send the message in notification
        });

        // Cancel button logic
        cancelButton.setOnClickListener(v -> finish());  // Close the activity
    }

    private void sendNotificationToUser(String message) {
        try {
            JSONObject json = new JSONObject();
            json.put("to", "/topics/winners"); // Adjust this as needed for specific users or topics

            JSONObject notification = new JSONObject();
            notification.put("title", "Event Lottery");
            notification.put("body", message);

            json.put("notification", notification);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", json,
                    response -> Toast.makeText(SendNotificationActivity.this, "Notification sent successfully", Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(SendNotificationActivity.this, "Failed to send notification", Toast.LENGTH_SHORT).show()) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "key=YOUR_SERVER_KEY"); // Replace with your FCM server key
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error sending notification", Toast.LENGTH_SHORT).show();
        }
    }
}
