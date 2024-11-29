package com.example.event_lottery;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ChosenEntrantsActivity extends AppCompatActivity {

    private String eventId;
    private RecyclerView recyclerViewChosenEntrants;
    private ChosenEntrantAdapter chosenEntrantAdapter;
    private List<ChosenEntrant> entrantList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_entrants_screen);

        // Retrieve eventId from intent
        eventId = getIntent().getStringExtra("eventId");
        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Event ID is missing.", Toast.LENGTH_SHORT).show();
            finish(); // Exit the activity if eventId is not provided
            return;
        }

        // Log for debugging
        Log.d("ChosenEntrantsActivity", "Received Event ID: " + eventId);

        // Initialize Firebase and RecyclerView
        db = FirebaseFirestore.getInstance();
        recyclerViewChosenEntrants = findViewById(R.id.recyclerView_chosen_entrants);
        entrantList = new ArrayList<>();
        chosenEntrantAdapter = new ChosenEntrantAdapter(entrantList);
        recyclerViewChosenEntrants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChosenEntrants.setAdapter(chosenEntrantAdapter);

        // Fetch the chosen entrants
        fetchChosenEntrants();
    }

    private void fetchChosenEntrants() {
        db.collection("events")
                .document(eventId)
                .collection("selectedEntrants")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        entrantList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String email = document.getString("email");
                            String location = document.getString("location");

                            if (email != null && location != null) {
                                entrantList.add(new ChosenEntrant(email, location));
                            }
                        }
                        chosenEntrantAdapter.notifyDataSetChanged();
                        Log.d("ChosenEntrantsActivity", "Fetched " + entrantList.size() + " entrants.");
                    } else {
                        Toast.makeText(this, "Failed to fetch chosen entrants.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
