package com.example.event_lottery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        listView = findViewById(R.id.notification_list_view);

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

                    if (value != null) {
                        notificationList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            String email = doc.getString("email");
                            String message = doc.getString("message");
                            Long statusLong = doc.getLong("status");
                            int status = statusLong != null ? statusLong.intValue() : 0;
                            String eventName = doc.getString("eventName"); // Get eventName

                            notificationList.add(new NotificationItem(email, message, status, eventName));
                        }
                        notificationAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "No notifications found.");
                    }
                });
    }
}
