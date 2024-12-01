package com.example.event_lottery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class NotificationSettingsActivity extends AppCompatActivity {

    private SwitchCompat switchWinLottery;
    private SwitchCompat switchLoseLottery;
    private SwitchCompat switchAdminOrganizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        // Initialize back button and set click listener
        ImageButton backButton = findViewById(R.id.backButton2);
        backButton.setOnClickListener(v -> finish()); // Closes the activity to go back

        // Initialize switches with SwitchCompat type
        switchWinLottery = findViewById(R.id.switch_win_lottery);
        switchLoseLottery = findViewById(R.id.switch_lose_lottery);
        switchAdminOrganizer = findViewById(R.id.switch_admin_organizer);

        // Optional: Load saved states of switches from SharedPreferences
        loadSwitchStates();

        // Set listeners to handle switch state changes if needed
        switchWinLottery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle "Win Lottery" switch toggle
            saveSwitchState("winLottery", isChecked);
        });

        switchLoseLottery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle "Lose Lottery" switch toggle
            saveSwitchState("loseLottery", isChecked);
        });

        switchAdminOrganizer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle "Admin/Organizer" switch toggle
            saveSwitchState("adminOrganizer", isChecked);
        });
    }

    // Method to save switch state in SharedPreferences
    private void saveSwitchState(String key, boolean state) {
        SharedPreferences sharedPreferences = getSharedPreferences("NotificationSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, state);
        editor.apply();
    }

    // Method to load switch states from SharedPreferences
    private void loadSwitchStates() {
        SharedPreferences sharedPreferences = getSharedPreferences("NotificationSettings", MODE_PRIVATE);
        switchWinLottery.setChecked(sharedPreferences.getBoolean("winLottery", false));
        switchLoseLottery.setChecked(sharedPreferences.getBoolean("loseLottery", false));
        switchAdminOrganizer.setChecked(sharedPreferences.getBoolean("adminOrganizer", false));
    }
}
