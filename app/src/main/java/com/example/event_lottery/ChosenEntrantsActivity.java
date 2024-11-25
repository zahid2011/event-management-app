package com.example.event_lottery;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChosenEntrantsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String eventId;
    private LinearLayout userListLayout; // Parent layout for user items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosen_entrants_screen);

        eventId = getIntent().getStringExtra("event_id");
        db = FirebaseFirestore.getInstance();
        userListLayout = findViewById(R.id.user_list_layout); // Assume this is the parent LinearLayout for user items

        fetchChosenEntrants(); // Fetch chosen entrants from Firestore
    }

    private void fetchChosenEntrants() {
        db.collection("events").document(eventId).collection("entrants")
                .whereEqualTo("status", "chosen")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot) {
                        String userName = doc.getString("username"); // Assuming a field "username"
                        addUserItem(userName);
                    }
                })
                .addOnFailureListener(e -> Log.e("ChosenEntrantsActivity", "Error fetching chosen entrants", e));
    }

    private void addUserItem(String userName) {
        // Create a new LinearLayout for the user item
        LinearLayout userItemLayout = new LinearLayout(this);
        userItemLayout.setOrientation(LinearLayout.HORIZONTAL);
        userItemLayout.setPadding(8, 8, 8, 8);

        // Create a TextView for the username
        TextView userTextView = new TextView(this);
        userTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        userTextView.setText("@" + userName);
        userTextView.setTextSize(16);
        userTextView.setTextColor(getResources().getColor(android.R.color.black));
        userTextView.setPadding(16, 0, 16, 0);

        // Create a Button for the status
        Button statusButton = new Button(this);
        statusButton.setText("Status");
        statusButton.setTextColor(getResources().getColor(android.R.color.white));
        statusButton.setBackgroundColor(getResources().getColor(R.color.button_color)); // Replace with actual color if needed
        statusButton.setPadding(16, 8, 16, 8);

        // Add views to the user item layout
        userItemLayout.addView(userTextView);
        userItemLayout.addView(statusButton);

        // Add the user item layout to the parent layout (user_list_layout)
        userListLayout.addView(userItemLayout);
    }

}