package com.example.event_lottery;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChosenEntrantsActivity extends AppCompatActivity {
    private EntrantAdapter entrantAdapter;
    private final List<User> entrantList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_entrants_screen);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_chosen_entrants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Initialize Adapter and set to RecyclerView
        entrantAdapter = new EntrantAdapter(entrantList);
        recyclerView.setAdapter(entrantAdapter);

        // Initialize Firebase and fetch data
        fetchChosenEntrants();

        // Set up Send Notifications button
        Button sendNotificationsButton = findViewById(R.id.send_notifications_button);
        sendNotificationsButton.setOnClickListener(v -> sendNotifications());
    }

    private void fetchChosenEntrants() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.orderByChild("isChosen").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                entrantList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        entrantList.add(user);
                        // Log user details to confirm retrieval
                        Log.d("ChosenEntrantsActivity", "User fetched: " + user.getUsername() + ", Email: " + user.getEmail());
                    }
                }
                entrantAdapter.notifyDataSetChanged(); // Update RecyclerView with new data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChosenEntrantsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("ChosenEntrantsActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void sendNotifications() {
        // Iterate over the list of chosen entrants and perform notification logic
        for (User user : entrantList) {
            // Implement notification logic here (e.g., using Firebase Cloud Messaging)
            // sendNotificationToUser(user); // Example placeholder method
        }
        Toast.makeText(this, "Notifications sent to all chosen entrants", Toast.LENGTH_SHORT).show();
    }
}
