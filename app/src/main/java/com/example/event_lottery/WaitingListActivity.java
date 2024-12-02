package com.example.event_lottery;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class WaitingListActivity extends AppCompatActivity {

    private ListView lvWaitingList;
    private List<WaitingListUser> waitingListUsers;
    private FirebaseFirestore db;
    private String eventId;
    private TextView tvTotalParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);

        lvWaitingList = findViewById(R.id.lv_waiting_list);
        tvTotalParticipants = findViewById(R.id.tv_total_participants);
        waitingListUsers = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        eventId = getIntent().getStringExtra("event_id");
        if (eventId == null) {
            Toast.makeText(this, "Event ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchWaitingList();
    }

    private void fetchWaitingList() {
        db.collection("events").document(eventId).collection("waitingList")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        waitingListUsers.clear();
                        for (DocumentSnapshot document : querySnapshot) {
                            String email = document.getString("userId");
                            waitingListUsers.add(new WaitingListUser(email != null ? email : "Unknown"));
                        }
                        tvTotalParticipants.setText("Total Participants: " + waitingListUsers.size());
                        WaitingListAdapter adapter = new WaitingListAdapter(this, waitingListUsers);
                        lvWaitingList.setAdapter(adapter);
                    } else {
                        tvTotalParticipants.setText("Total Participants: 0");
                        Toast.makeText(this, "No waiting list data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load waiting list", Toast.LENGTH_SHORT).show();
                });
    }

    public static class WaitingListUser {
        private final String name;

        public WaitingListUser(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
