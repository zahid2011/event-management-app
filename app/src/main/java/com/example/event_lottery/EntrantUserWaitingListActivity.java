package com.example.event_lottery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EntrantUserWaitingListActivity extends AppCompatActivity {
    private static final String TAG = "UserWaitingListActivity";
    private FirebaseFirestore db;
    private String userId;
    private ListView listView;
    private EntrantWaitingListAdapter entrantWaitingListAdapter;
    private List<Event> eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_joined_waiting_list);

        // initializing Firebase Firestore
        db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", null);

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        listView = findViewById(R.id.list_view);
        eventsList = new ArrayList<>();


        entrantWaitingListAdapter = new EntrantWaitingListAdapter(this, eventsList);
        listView.setAdapter(entrantWaitingListAdapter);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        fetchUserWaitingListEvents();
    }

    private void fetchUserWaitingListEvents() {
        db.collection("events").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            List<Event> tempEventList = new ArrayList<>();
                            int totalEvents = querySnapshot.size();
                            int[] completedEvents = {0};

                            for (DocumentSnapshot eventDocument : querySnapshot.getDocuments()) {
                                String eventId = eventDocument.getId();
                                Log.d(TAG, "Checking event: " + eventId);

                                db.collection("events").document(eventId).collection("waitingList")
                                        .whereEqualTo("userId", userId)
                                        .get()
                                        .addOnCompleteListener(waitingListTask -> {
                                            if (waitingListTask.isSuccessful() && !waitingListTask.getResult().isEmpty()) {
                                                Event event = eventDocument.toObject(Event.class);
                                                if (event != null) {
                                                    tempEventList.add(event);
                                                    Log.d(TAG, "Added event: " + event.getEventName());
                                                }
                                            } else {
                                                Log.d(TAG, "User not found in waiting list for event: " + eventId);
                                            }

                                            completedEvents[0]++;
                                            if (completedEvents[0] == totalEvents) {
                                                eventsList.clear();
                                                eventsList.addAll(tempEventList);
                                                entrantWaitingListAdapter.notifyDataSetChanged();
                                            }
                                        });
                            }
                        }
                    } else {
                        Log.e(TAG, "Error getting events", task.getException());
                        Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
