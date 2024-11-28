package com.example.event_lottery;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RunLotteryActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String eventId;
    private int eventCapacity = 0; // Default event capacity
    private boolean lotteryCompleted = false; // To prevent re-running the lottery
    private ProgressBar loadingSpinner;
    private EditText sampleSizeInput;
    private Button confirmButton, notifyAllButton, drawReplacementButton;
    private LinearLayout participantsLayout;
    private ImageView ivBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_lottery);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve event ID from the intent
        eventId = getIntent().getStringExtra("event_id");
        if (eventId == null || eventId.isEmpty()) {
            Log.e("RunLotteryActivity", "Event ID is null or empty");
            Toast.makeText(this, "Event ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        loadingSpinner = findViewById(R.id.loading_spinner);
        sampleSizeInput = findViewById(R.id.sample_size_input);
        confirmButton = findViewById(R.id.confirm_button);
        notifyAllButton = findViewById(R.id.notify_all_button);
        drawReplacementButton = findViewById(R.id.draw_replacement_button);
        participantsLayout = findViewById(R.id.participants_layout);
        ivBackArrow = findViewById(R.id.back_button);

        // Fetch event details from Firestore
        fetchEventDetails();

        // Set up button listeners
        setupListeners();

        ivBackArrow.setOnClickListener(v -> finish());

        // Focus the EditText and show the keyboard
        sampleSizeInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(sampleSizeInput, InputMethodManager.SHOW_IMPLICIT);
    }

    private void setupListeners() {
        // Confirm button
        confirmButton.setOnClickListener(v -> {
            if (lotteryCompleted) {
                Toast.makeText(this, "Lottery already completed. Cannot run again.", Toast.LENGTH_SHORT).show();
                return;
            }

            String sampleSizeText = sampleSizeInput.getText().toString().trim();
            if (!sampleSizeText.isEmpty()) {
                try {
                    int sampleSize = Integer.parseInt(sampleSizeText);

                    // Validate sample size
                    if (sampleSize > eventCapacity) {
                        Toast.makeText(this, "Sample size cannot exceed event capacity: " + eventCapacity, Toast.LENGTH_SHORT).show();
                    } else if (sampleSize <= 0) {
                        Toast.makeText(this, "Sample size must be greater than 0", Toast.LENGTH_SHORT).show();
                    } else {
                        confirmButton.setEnabled(false); // Prevent duplicate clicks
                        runLottery(sampleSize);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid sample size format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a valid sample size", Toast.LENGTH_SHORT).show();
            }
        });

        // Notify All button
        notifyAllButton.setOnClickListener(v -> Toast.makeText(this, "Notifying all selected participants", Toast.LENGTH_SHORT).show());

        // Draw Replacement button
        drawReplacementButton.setOnClickListener(v -> Toast.makeText(this, "Drawing replacement for declined participant", Toast.LENGTH_SHORT).show());
    }

    private void fetchEventDetails() {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String capacityStr = documentSnapshot.getString("capacity");
                        if (capacityStr != null && !capacityStr.isEmpty()) {
                            try {
                                eventCapacity = Integer.parseInt(capacityStr);
                                Log.d("RunLotteryActivity", "Event capacity fetched: " + eventCapacity);
                            } catch (NumberFormatException e) {
                                Log.e("RunLotteryActivity", "Invalid capacity format: " + capacityStr, e);
                                Toast.makeText(this, "Invalid event capacity", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Log.e("RunLotteryActivity", "Event capacity is missing");
                            Toast.makeText(this, "Event capacity not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Log.e("RunLotteryActivity", "Event not found");
                        Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("RunLotteryActivity", "Error fetching event details", e);
                    Toast.makeText(this, "Error fetching event details", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void runLottery(int sampleSize) {
        loadingSpinner.setVisibility(View.VISIBLE);

        CollectionReference waitingListRef = db.collection("events").document(eventId).collection("waitingList");
        CollectionReference selectedRef = db.collection("events").document(eventId).collection("selectedEntrants");

        waitingListRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<DocumentSnapshot> waitingList = new ArrayList<>(task.getResult().getDocuments());

                if (waitingList.isEmpty()) {
                    loadingSpinner.setVisibility(View.GONE);
                    Toast.makeText(this, "No users in the waiting list", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Shuffle waiting list for randomness
                Collections.shuffle(waitingList);

                // Limit to specified sample size
                List<DocumentSnapshot> selectedUsers = waitingList.subList(0, Math.min(sampleSize, waitingList.size()));

                // Update selected users in Firestore
                WriteBatch batch = db.batch();
                for (DocumentSnapshot user : selectedUsers) {
                    String docId = user.getId();
                    batch.set(selectedRef.document(docId), user.getData());
                }

                // Commit batch operation
                batch.commit().addOnCompleteListener(updateTask -> {
                    loadingSpinner.setVisibility(View.GONE);
                    confirmButton.setEnabled(true); // Re-enable button
                    if (updateTask.isSuccessful()) {
                        lotteryCompleted = true;
                        Toast.makeText(this, "Lottery completed successfully", Toast.LENGTH_SHORT).show();
                        displaySelectedParticipants(selectedUsers);
                    } else {
                        Toast.makeText(this, "Failed to complete the lottery", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                loadingSpinner.setVisibility(View.GONE);
                Toast.makeText(this, "Failed to fetch waiting list", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displaySelectedParticipants(List<DocumentSnapshot> selectedUsers) {
        participantsLayout.removeAllViews();

        for (DocumentSnapshot user : selectedUsers) {
            View participantView = getLayoutInflater().inflate(R.layout.participant_item, participantsLayout, false);

            TextView emailTextView = participantView.findViewById(R.id.participant_email);
            Button notifyButton = participantView.findViewById(R.id.notify_button);
            Button removeButton = participantView.findViewById(R.id.remove_button);

            String email = user.getString("email");
            if (email == null || email.isEmpty()) {
                email = "Email not provided";
            }

            emailTextView.setText(email);

            String finalEmail = email;
            notifyButton.setOnClickListener(v -> Toast.makeText(this, "Notifying " + finalEmail, Toast.LENGTH_SHORT).show());
            String finalEmail1 = email;
            removeButton.setOnClickListener(v -> {
                Toast.makeText(this, "Removing " + finalEmail1, Toast.LENGTH_SHORT).show();
                participantsLayout.removeView(participantView);
            });

            participantsLayout.addView(participantView);
        }
    }
}
