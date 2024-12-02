package com.example.event_lottery;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EntrantUserWaitingListActivity extends AppCompatActivity {
    private static final String TAG = "UserWaitingListActivity";
    private FirebaseFirestore db;
    private String userEmail;
    private ListView listView;
    private EntrantWaitingListAdapter entrantWaitingListAdapter;
    private List<Event> eventsList;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_joined_waiting_list);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Get user email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("USER_ID", null);

        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI components
        listView = findViewById(R.id.list_view);
        eventsList = new ArrayList<>();
        entrantWaitingListAdapter = new EntrantWaitingListAdapter(this, eventsList);
        listView.setAdapter(entrantWaitingListAdapter);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Fetch events where the user exists in the waiting list
        fetchEventsWithUserInWaitingList();
    }

    /**
     * Fetches events from Firestore where the user is in the waiting list.
     */
    private void fetchEventsWithUserInWaitingList() {
        db.collection("events") // Fetch all events
                .get()
                .addOnSuccessListener(eventSnapshots -> {
                    if (eventSnapshots != null && !eventSnapshots.isEmpty()) {
                        List<DocumentSnapshot> eventDocs = eventSnapshots.getDocuments();
                        checkWaitingLists(eventDocs); // Check each event's waiting list for the user
                    } else {
                        Log.d(TAG, "No events found.");
                        Toast.makeText(this, "No events available.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching events: ", e);
                    Toast.makeText(this, "Failed to load events. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Checks the waiting lists of each event to see if the user is present.
     *
     * @param eventDocs The list of event documents to check.
     */
    private void checkWaitingLists(List<DocumentSnapshot> eventDocs) {
        List<Event> tempEventList = new ArrayList<>();
        int[] processedCount = {0}; // Counter for processed documents

        for (DocumentSnapshot eventDoc : eventDocs) {
            String documentId = eventDoc.getId(); // Use the Firestore document ID as the eventId
            DocumentReference waitingListRef = db.collection("events").document(documentId).collection("waitingList").document(userEmail);

            waitingListRef.get()
                    .addOnSuccessListener(waitingListDoc -> {
                        processedCount[0]++; // Increment processed count
                        if (waitingListDoc.exists()) {
                            Event event = eventDoc.toObject(Event.class);
                            if (event != null) {
                                event.setEventId(documentId); // Set the document ID as the eventId
                                tempEventList.add(event);
                                Log.d(TAG, "User is in waiting list for event: " + event.getEventName());
                            }
                        }

                        // Update the UI once all documents are processed
                        if (processedCount[0] == eventDocs.size()) {
                            updateEventList(tempEventList);
                        }
                    })
                    .addOnFailureListener(e -> {
                        processedCount[0]++; // Increment processed count on failure
                        Log.e(TAG, "Error checking waiting list for event ID: " + documentId, e);

                        // Update the UI once all documents are processed
                        if (processedCount[0] == eventDocs.size()) {
                            updateEventList(tempEventList);
                        }
                    });
        }
    }

    /**
     * Called when the activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        fetchEventsWithUserInWaitingList(); // Re-fetch the waiting list when the activity resumes
    }

    /**
     * Updates the event list displayed in the UI.
     *
     * @param tempEventList The updated list of events to display.
     */
    private void updateEventList(List<Event> tempEventList) {
        eventsList.clear();
        eventsList.addAll(tempEventList);
        entrantWaitingListAdapter.notifyDataSetChanged();
        Log.d(TAG, "Updated event list with " + tempEventList.size() + " events.");
    }
}
