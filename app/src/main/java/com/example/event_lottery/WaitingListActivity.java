package com.example.event_lottery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.widget.EditText;
import android.widget.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WaitingListActivity extends AppCompatActivity {


    private TextView tvTotalParticipants;
    private EditText etSearchUser;
    private ImageView ivSearchIcon;
    private ListView lvWaitingList;
    private Button btnSendNotifications;


    private FirebaseFirestore db;
    private WaitingListAdapter adapter;
    private List<String> participantList;
    private String eventId;
    private EditText entrantEmailEditText;
    private Button btnJoinWaitingList;
    private EditText entrantNameEditText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list); // Make sure the layout name matches


        // Initialize UI components
        tvTotalParticipants = findViewById(R.id.tv_total_participants);
        etSearchUser = findViewById(R.id.et_search_user);
        ivSearchIcon = findViewById(R.id.iv_search_icon);
        lvWaitingList = findViewById(R.id.lv_waiting_list);
        btnSendNotifications = findViewById(R.id.btn_send_notifications);
        entrantEmailEditText = findViewById(R.id.entrant_email);
        entrantNameEditText = findViewById(R.id.entrant_name);
        btnJoinWaitingList = findViewById(R.id.btn_join_waiting_list);
        btnJoinWaitingList.setOnClickListener(v -> joinWaitingList());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();


        // Get eventId from Intent
        eventId = getIntent().getStringExtra("eventName");


        // Initialize participant list and adapter
        participantList = new ArrayList<>();
        adapter = new WaitingListAdapter(this, participantList);
        lvWaitingList.setAdapter(adapter);


        // Load participants from Firestore
        loadParticipants();


        // Set up button click listeners
        btnSendNotifications.setOnClickListener(v -> sendNotifications());
    }


    private void loadParticipants() {
        if (eventId == null) {
            Toast.makeText(this, "Event ID not found", Toast.LENGTH_SHORT).show();
            return;
        }


        db.collection("events").document(eventId).collection("waiting_list")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        participantList.clear(); // Clear list to avoid duplicates
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String participantName = document.getString("name");
                            if (participantName != null) {
                                participantList.add(participantName); // Add participant names to list
                            }
                        }
                        adapter.notifyDataSetChanged();
                        tvTotalParticipants.setText("Total Participants: " + participantList.size());
                    } else {
                        Toast.makeText(WaitingListActivity.this, "Failed to load waiting list.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void sendNotifications() {
        // Placeholder functionality for sending notifications
        Toast.makeText(this, "Notifications sent to all participants.", Toast.LENGTH_SHORT).show();
        // Implement actual notification sending logic here if needed
    }
    private void joinWaitingList() {
        String email = entrantEmailEditText.getText().toString().trim();
        String name = entrantNameEditText.getText().toString().trim(); // Add an EditText for name

        if (email.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please enter both name and email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to store entrant information
        Map<String, Object> entrantData = new HashMap<>();
        entrantData.put("name", name);
        entrantData.put("email", email);

        // Add entrant to the "Waiting_list" subcollection of the event
        db.collection("events").document(eventId).collection("Waiting_list")
                .add(entrantData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Joined waiting list successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to join waiting list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
