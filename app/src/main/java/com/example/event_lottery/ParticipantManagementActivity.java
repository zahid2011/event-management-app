package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ParticipantManagementActivity extends AppCompatActivity {

    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_management_page);

        // Retrieve `eventId` from intent
        eventId = getIntent().getStringExtra("eventId");
        if (eventId == null || eventId.isEmpty()) {
            finish(); // Exit if no eventId is passed
            return;
        }

        Button chosenEntrantsButton = findViewById(R.id.btn_chosen_entrants);
        chosenEntrantsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ParticipantManagementActivity.this, ChosenEntrantsActivity.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        });

        Button finalizeListButton = findViewById(R.id.btn_final_registered_list);
        finalizeListButton.setOnClickListener(v -> {
            Intent intent = new Intent(ParticipantManagementActivity.this, FinalizedParticipantActivity.class);
            intent.putExtra("eventId", eventId); // Pass any needed data like eventId
            startActivity(intent);
        });

        Button cancelledListButton = findViewById(R.id.btn_cancelled_entrants);
        cancelledListButton.setOnClickListener(v -> {
            Intent intent = new Intent(ParticipantManagementActivity.this, CancelledParticipantActivity.class);
            intent.putExtra("eventId", eventId); // Pass the event ID if needed
            startActivity(intent);
        });


        // Additional participant management logic can be added here
    }
}
