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

public class EventListActivity extends AppCompatActivity {
    private ListView eventListView;
    private EventAdapter eventAdapter;
    private FirebaseFirestore db;
    private List<Event> events;


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
    private void loadEventsFromFirebase() {
        CollectionReference eventsRef = db.collection("events");
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
