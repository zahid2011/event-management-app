package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationDetailsActivity extends AppCompatActivity {

    private TextView messageTextView;
    private TextView statusTextView;
    private Button acceptButton;
    private Button rejectButton;
    private Button tryAgainButton;
    private Button backButton;

    private String email;
    private String message;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        messageTextView = findViewById(R.id.notification_message);
        statusTextView = findViewById(R.id.notification_status);
        acceptButton = findViewById(R.id.accept_button);
        rejectButton = findViewById(R.id.reject_button);
        tryAgainButton = findViewById(R.id.try_again_button);
        backButton = findViewById(R.id.back_button);

        // Get data from intent
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        message = intent.getStringExtra("message");
        status = intent.getIntExtra("status", 0);

        messageTextView.setText(message);
        statusTextView.setText("Status: " + status);

        // Depending on status, show appropriate buttons
        if (status == 1) {
            acceptButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);
            tryAgainButton.setVisibility(View.GONE);
        } else if (status == 0) {
            acceptButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
            tryAgainButton.setVisibility(View.VISIBLE);
        }

        acceptButton.setOnClickListener(v -> {
            // Handle accept action
            Toast.makeText(NotificationDetailsActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
            // Implement the accept logic here
        });

        rejectButton.setOnClickListener(v -> {
            // Handle reject action
            Toast.makeText(NotificationDetailsActivity.this, "Rejected", Toast.LENGTH_SHORT).show();
            // Implement the reject logic here
        });

        tryAgainButton.setOnClickListener(v -> {
            // Handle try again action
            Toast.makeText(NotificationDetailsActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
            // Implement the try again logic here
        });

        backButton.setOnClickListener(v -> {
            finish(); // Go back to previous activity
        });
    }
}
