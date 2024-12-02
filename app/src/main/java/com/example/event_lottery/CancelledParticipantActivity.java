package com.example.event_lottery;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CancelledParticipantActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String eventId;
    private LinearLayout cancelledParticipantsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancelled_entrants);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get the event ID from intent
        eventId = getIntent().getStringExtra("eventId");

        // Initialize UI components
        cancelledParticipantsLayout = findViewById(R.id.cancelledParticipantsLayout); // Adjust the ID based on your XML if needed

        // Fetch and display canceled participants
        fetchCancelledParticipants();
    }

    private void fetchCancelledParticipants() {
        db.collection("events").document(eventId).collection("cancelledEntrants").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<DocumentSnapshot> cancelledUsers = new ArrayList<>(task.getResult().getDocuments());
                        if (!cancelledUsers.isEmpty()) {
                            displayCancelledParticipants(cancelledUsers);
                        } else {
                            Toast.makeText(this, "No cancelled participants found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error fetching cancelled participants.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayCancelledParticipants(List<DocumentSnapshot> cancelledUsers) {
        cancelledParticipantsLayout.removeAllViews(); // Clear previous views if any

        for (DocumentSnapshot user : cancelledUsers) {
            // Inflate the participant item layout
            LinearLayout participantView = (LinearLayout) getLayoutInflater().inflate(R.layout.participant_item, null);

            TextView userNameTextView = participantView.findViewById(R.id.participant_userID); // Adjust IDs based on `participant_item.xml`
          //  Button statusButton = participantView.findViewById(R.id.status_button);

            String userId = user.getString("userId");
            userNameTextView.setText(userId != null ? userId : "Unknown User");

            //statusButton.setOnClickListener(v -> {
                // Logic to handle status button click
              //  Toast.makeText(this, "Status clicked for " + userId, Toast.LENGTH_SHORT).show();
           // });

            // Add the participant view to the parent layout
            cancelledParticipantsLayout.addView(participantView);
        }
    }
}
