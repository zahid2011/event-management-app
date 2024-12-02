package com.example.event_lottery;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FinalizedParticipantActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String eventId;
    private ListView finalizedParticipantsListView;
    private FinalizedParticipantsAdapter adapter;
    private List<String> finalizedParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalized_participants);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get event ID passed from intent
        eventId = getIntent().getStringExtra("eventId");

        // Initialize UI components
        finalizedParticipantsListView = findViewById(R.id.lv_waiting_list);

        // Initialize participant list and adapter
        finalizedParticipants = new ArrayList<>();
        adapter = new FinalizedParticipantsAdapter(this, finalizedParticipants);
        finalizedParticipantsListView.setAdapter(adapter);

        // Fetch finalized participants
        fetchFinalizedParticipants();

        // Back button functionality
        ImageView backButton = findViewById(R.id.iv_back);
        backButton.setOnClickListener(v -> finish());
    }

    private void fetchFinalizedParticipants() {
        db.collection("events").document(eventId).collection("finalizedEntrants").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<DocumentSnapshot> finalizedUsers = new ArrayList<>(task.getResult().getDocuments());
                        if (!finalizedUsers.isEmpty()) {
                            finalizedParticipants.clear();
                            for (DocumentSnapshot user : finalizedUsers) {
                                String userId = user.getString("userId");
                                if (userId != null && !userId.isEmpty()) {
                                    finalizedParticipants.add(userId);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "No finalized participants found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error fetching finalized participants.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
