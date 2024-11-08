package com.example.event_lottery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WaitingListActivity extends AppCompatActivity {

    private ListView lvWaitingList;
    private List<WaitingListUser> waitingListUsers;
    private FirebaseFirestore db;
    private String eventId;
    private Set<Integer> selectedUsers; // Track selected user positions
    private TextView tvTotalParticipants; // TextView for total participants
    private Button btnSelectAll; // Button for selecting all users

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);

        lvWaitingList = findViewById(R.id.lv_waiting_list);
        Button btnSendNotifications = findViewById(R.id.btn_send_notifications);
        tvTotalParticipants = findViewById(R.id.tv_total_participants); // Initialize participant count TextView
        btnSelectAll = findViewById(R.id.btn_select_all); // Initialize the "Select All" button
        waitingListUsers = new ArrayList<>();
        selectedUsers = new HashSet<>();
        db = FirebaseFirestore.getInstance();

        // Get the event ID from the intent
        eventId = getIntent().getStringExtra("event_id");
        if (eventId == null) {
            Toast.makeText(this, "Event ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch waiting list data
        fetchWaitingList();

        // Set click listener for "Select All" button
        btnSelectAll.setOnClickListener(v -> {
            // Select all users
            for (int i = 0; i < waitingListUsers.size(); i++) {
                selectedUsers.add(i);
            }
            // Notify the adapter to update the ListView
            ((ArrayAdapter) lvWaitingList.getAdapter()).notifyDataSetChanged();
            Toast.makeText(this, "All users selected", Toast.LENGTH_SHORT).show();
        });

        // Set up Send Notifications button click listener
        btnSendNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(WaitingListActivity.this, NotificationActivity.class);
            intent.putExtra("event_id", eventId); // Pass event ID
            intent.putStringArrayListExtra("selected_users", new ArrayList<>(getSelectedUserEmails()));
            startActivity(intent);
        });
    }

    private void fetchWaitingList() {
        // Firestore reference: Adjust collection path as needed
        DocumentReference eventRef = db.collection("events").document(eventId);
        eventRef.collection("waitingList")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Populate the waiting list
                        waitingListUsers.clear();
                        for (DocumentSnapshot document : querySnapshot) {
                            String email = document.getString("email");
                            waitingListUsers.add(new WaitingListUser(email != null ? email : "Unknown", R.drawable.ic_image_placeholder));
                        }

                        // Update the total participants TextView
                        tvTotalParticipants.setText("Total Participants: " + waitingListUsers.size());

                        // Set up the adapter
                        WaitingListAdapter adapter = new WaitingListAdapter(this, waitingListUsers);
                        lvWaitingList.setAdapter(adapter);
                    } else {
                        tvTotalParticipants.setText("Total Participants: 0"); // No participants
                        Toast.makeText(this, "No waiting list data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("WaitingListActivity", "Error fetching waiting list", e);
                    Toast.makeText(this, "Failed to load waiting list", Toast.LENGTH_SHORT).show();
                });
    }

    private List<String> getSelectedUserEmails() {
        List<String> emails = new ArrayList<>();
        for (int position : selectedUsers) {
            emails.add(waitingListUsers.get(position).getName()); // Assuming 'name' is the email
        }
        return emails;
    }

    // Custom Adapter for Waiting List
    class WaitingListAdapter extends ArrayAdapter<WaitingListUser> {

        private Context context;
        private List<WaitingListUser> users;

        public WaitingListAdapter(Context context, List<WaitingListUser> users) {
            super(context, 0, users);
            this.context = context;
            this.users = users;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Inflate custom layout
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.waiting_list_item, parent, false);
            }

            // Get the current user
            WaitingListUser user = users.get(position);

            // Bind views
            ImageView ivUserIcon = convertView.findViewById(R.id.iv_user_icon);
            TextView tvUsername = convertView.findViewById(R.id.tv_username);
            Button btnDetails = convertView.findViewById(R.id.btn_details);

            ivUserIcon.setImageResource(user.getIcon());
            tvUsername.setText(user.getName());

            // Highlight if the user is selected
            if (selectedUsers.contains(position)) {
                convertView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            }

            // Handle row click to select/unselect user
            convertView.setOnClickListener(v -> {
                if (selectedUsers.contains(position)) {
                    selectedUsers.remove(position);
                    Toast.makeText(context, "Unselected: " + user.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    selectedUsers.add(position);
                    Toast.makeText(context, "Selected: " + user.getName(), Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged(); // Refresh the ListView to update selection
            });

            // Set up "Details" button (non-functional for now)
            btnDetails.setOnClickListener(v -> {
                // Placeholder: Show a message when clicked
                Toast.makeText(context, "Details for " + user.getName(), Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }
    }

    // Model Class for Users
    class WaitingListUser {
        private String name;
        private int icon;

        public WaitingListUser(String name, int icon) {
            this.name = name;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public int getIcon() {
            return icon;
        }
    }
}