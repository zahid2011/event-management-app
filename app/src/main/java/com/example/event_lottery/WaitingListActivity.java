package com.example.event_lottery;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WaitingListActivity extends AppCompatActivity {

    private ListView lvWaitingList;
    private List<String> waitingListUsers;
    private FirebaseFirestore db;
    private String eventId;
    private Set<Integer> selectedUsers;
    private TextView tvTotalParticipants;
    private Button btnSelectAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);

        lvWaitingList = findViewById(R.id.lv_waiting_list);
        tvTotalParticipants = findViewById(R.id.tv_total_participants);
        btnSelectAll = findViewById(R.id.btn_select_all);
        Button btnSendNotifications = findViewById(R.id.btn_send_notifications);
        ImageButton backButton = findViewById(R.id.backButton);

        waitingListUsers = new ArrayList<>();
        selectedUsers = new HashSet<>();
        db = FirebaseFirestore.getInstance();

        eventId = getIntent().getStringExtra("event_id");
        if (eventId == null) {
            Toast.makeText(this, R.string.event_id_missing, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchWaitingList();

        // "Back" button functionality
        backButton.setOnClickListener(v -> onBackPressed());

        // "Select All" button logic
        btnSelectAll.setOnClickListener(v -> {
            selectedUsers.clear();
            for (int i = 0; i < waitingListUsers.size(); i++) {
                selectedUsers.add(i);
            }
            ((WaitingListAdapter) lvWaitingList.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "All users selected", Toast.LENGTH_SHORT).show();
        });

        btnSendNotifications.setOnClickListener(v -> {
            // Check if there are any selected users
            if (selectedUsers.isEmpty()) {
                Toast.makeText(this, "Please select participants to notify.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Notify the selected users
            sendNotificationsToAllSelectedUsers();
        });


    }

    private void sendNotificationsToAllSelectedUsers() {
        // Collect selected user ID
        List<String> selectedUserID = new ArrayList<>();
        for (int position : selectedUsers) {
            selectedUserID.add(waitingListUsers.get(position));
        }
        // Simulate sending notifications
        for (String email : selectedUserID) {
            sendNotificationToUserIfAllowed(email);
        }
    }



    private void sendNotificationToUserIfAllowed(String userId) {
        // Reference the specific user's document
        DocumentReference userRef = db.collection("users").document(userId);

        // Fetch user's notification preference
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Boolean doNotReceiveAdminNotifications = documentSnapshot.getBoolean("doNotReceiveAdminNotifications");
                        if (doNotReceiveAdminNotifications != null && doNotReceiveAdminNotifications) {
                            // User doesn't want to receive notifications
                            Toast.makeText(this, "User " + userId + " does not want to receive notifications.", Toast.LENGTH_SHORT).show();
                        } else {
                            // User wants to receive notifications, proceed to send
                            sendNotification(userRef, userId);
                        }
                    } else {
                        // User document doesn't exist, proceed to send notification
                        sendNotification(userRef, userId);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error fetching user document, proceed to send notification
                    sendNotification(userRef, userId);
                });
    }

    private void sendNotification(DocumentReference userRef, String userId) {
        // Create the notification message with the event name
        String message;
        int status;
        message = "Congratulations! You are now in the Waiting List for " + eventId;
        status = 3;


        // Add notification under the user's notifications subcollection
        userRef.collection("notifications")
                .add(new NotificationData(userId, message, status, eventId))
                .addOnSuccessListener(documentReference -> {
                    Log.d("WaitingListActivity", "Notification sent to user: " + userId);
                    Toast.makeText(this, "Notification sent to " + userId, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("WaitingListActivity", "Failed to send notification to user: " + userId, e);
                    Toast.makeText(this, "Failed to send notification to " + userId, Toast.LENGTH_SHORT).show();
                });
    }


    private void fetchWaitingList() {
        db.collection("events").document(eventId).collection("waitingList")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        waitingListUsers.clear();
                        for (DocumentSnapshot document : querySnapshot) {
                            String email = document.getString("userId");
                            waitingListUsers.add(email != null ? email : getString(R.string.unknown_user));
                        }
                        tvTotalParticipants.setText(getString(R.string.total_participants, waitingListUsers.size()));
                        WaitingListAdapter adapter = new WaitingListAdapter(this, waitingListUsers, selectedUsers);
                        lvWaitingList.setAdapter(adapter);
                    } else {
                        tvTotalParticipants.setText(getString(R.string.total_participants, 0));
                        Toast.makeText(this, R.string.no_waiting_list_data, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, R.string.failed_to_load_waiting_list, Toast.LENGTH_SHORT).show();
                });
    }
}
