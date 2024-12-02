package com.example.event_lottery;

import com.example.event_lottery.Event;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;

/**
 * This class represents the activity to display a list of events retrieved from Firebase Firestore.
 * It uses a {@link ListView} to show the events and an {@link EventAdapter} to bind the data.
 * Users can navigate back to the previous screen using the back button.
 */

public class EventListActivity extends AppCompatActivity {
    private ListView eventListView;
    private EventAdapter eventAdapter;
    private FirebaseFirestore db;
    private List<Event> events;

    /**
     * Called when the activity is created. Initializes the user interface components,
     * sets up the event adapter, and loads events from Firebase Firestore.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     * being shut down, this contains the data it most recently supplied.
     * Otherwise, it is null.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        eventListView = findViewById(R.id.list_view);
        db = FirebaseFirestore.getInstance();
        events = new ArrayList<Event>();

        //the back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Handles the back button click event. Closes the current activity and navigates back to the previous one.
             *
             * @param v The view that was clicked.
             */
            @Override

            public void onClick(View v) {
                finish(); // This closes the current activity and goes back to the previous one
            }
        });

        // Set up the adapter with an empty list initially
        eventAdapter = new EventAdapter(this, events);
        eventListView.setAdapter(eventAdapter);

        // Retrieve data from Firebase Firestore
        loadEventsFromFirebase();
    }
    /**
     * Loads events from the Firebase Firestore "events" collection and updates the ListView.
     * This method listens for real-time changes to the Firestore collection.
     */




    private void loadEventsFromFirebase() {
        CollectionReference eventsRef = db.collection("events");
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {


            /**
             * Callback triggered when the Firestore collection changes or an error occurs.
             * Updates the list of events and refreshes the adapter.
             *
             * @param value The latest snapshot of the "events" collection.
             * @param error The exception that occurred, or null if no error.
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(EventListActivity.this, "Error loading events", Toast.LENGTH_SHORT).show();
                    return;
                }

                events.clear(); // Clear the existing list to avoid duplicates
                for (QueryDocumentSnapshot doc : value) {
                    Event event = doc.toObject(Event.class);
                    events.add(event);
                }

                // Notify the adapter of the data change
                eventAdapter.notifyDataSetChanged();
            }
        });
    }
}
