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

        // Additional participant management logic can be added here
    }
}
