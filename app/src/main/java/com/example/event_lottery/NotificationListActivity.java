package com.example.event_lottery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class NotificationListActivity extends AppCompatActivity {

    private static final String TAG = "NotificationListActivity";
    private ListView listView;
    private NotificationAdapter notificationAdapter;
    private List<NotificationItem> notificationList;
    private FirebaseFirestore db;
    private String userId; // User email
    private TextView noNotificationsTextView;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        // Initialize views
        listView = findViewById(R.id.notification_list_view);
        noNotificationsTextView = findViewById(R.id.no_notifications_text_view);
        backButton = findViewById(R.id.back_button);

        // Check if backButton is null
        if (backButton == null) {
            Log.e(TAG, "backButton is null. Check the ID in your layout file.");
        } else {
            Log.d(TAG, "backButton initialized successfully.");
        }

        // Set up back button listener
        backButton.setOnClickListener(v -> finish());

        // Initialize other components
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this, notificationList);
        listView.setAdapter(notificationAdapter);

        // Get userId (email) from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", null);

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "No user ID found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "User ID (email): " + userId);

        db = FirebaseFirestore.getInstance();

        loadNotifications();
    }

    private void loadNotifications() {
        db.collection("users").document(userId).collection("notifications")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    notificationList.clear();

                    if (value != null && !value.isEmpty()) {
                        for (QueryDocumentSnapshot doc : value) {
                            String notificationId = doc.getId();
                            String email = doc.getString("email");
                            String message = doc.getString("message");
                            Long statusLong = doc.getLong("status");
                            int status = statusLong != null ? statusLong.intValue() : 0;
                            String eventName = doc.getString("eventName");

                            notificationList.add(new NotificationItem(notificationId, email, message, status, eventName));
                        }
                        notificationAdapter.notifyDataSetChanged();
                        noNotificationsTextView.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(TAG, "No notifications found.");
                        noNotificationsTextView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                });
    }
}
