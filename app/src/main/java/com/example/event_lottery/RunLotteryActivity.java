package com.example.event_lottery;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String eventName; // New field to store the event name

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

    public void onRunLotteryClicked(View view) {
        // Trigger lottery logic
        String sampleSizeText = sampleSizeInput.getText().toString().trim();
        if (!sampleSizeText.isEmpty()) {
            try {
                int sampleSize = Integer.parseInt(sampleSizeText);
                if (sampleSize > 0 && sampleSize <= eventCapacity) {
                    runLottery(sampleSize);
                } else {
                    Toast.makeText(this, "Invalid sample size. Must be between 1 and event capacity.", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid sample size format", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a sample size", Toast.LENGTH_SHORT).show();
        }
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
        notifyAllButton.setOnClickListener(v -> {
            if (!lotteryCompleted) {
                Toast.makeText(this, "Please run the lottery before notifying participants.", Toast.LENGTH_SHORT).show();
                return;
            }

            sendNotificationsToAllParticipants(); // Notify both winners and non-winners
        });

        // Draw Replacement button
        drawReplacementButton.setOnClickListener(v -> {
            if (!lotteryCompleted) {
                Toast.makeText(this, "Please run the lottery before drawing replacements.", Toast.LENGTH_SHORT).show();
                return;
            }

            drawReplacementForDeclinedParticipant();
        });

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

                                // Fetch event name
                                eventName = documentSnapshot.getString("eventName"); // Adjust the field name as per your Firestore structure
                                if (eventName == null || eventName.isEmpty()) {
                                    eventName = "Unnamed Event";
                                }
                                Log.d("RunLotteryActivity", "Event name fetched: " + eventName);

                                // Check if selected entrants already exist
                                fetchSelectedParticipants();

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

    private void fetchSelectedParticipants() {
        db.collection("events").document(eventId).collection("selectedEntrants").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<DocumentSnapshot> selectedUsers = new ArrayList<>(task.getResult().getDocuments());

                        if (!selectedUsers.isEmpty()) {
                            lotteryCompleted = true; // Mark lottery as completed
                            displaySelectedParticipants(selectedUsers); // Display the selected participants
                        } else {
                            Log.d("RunLotteryActivity", "No selected entrants found.");
                        }
                    } else {
                        Log.e("RunLotteryActivity", "Failed to fetch selected entrants", task.getException());
                        Toast.makeText(this, "Error fetching selected entrants", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Updated runLottery Method to Remove Selected Entrants from the Waiting List
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
                    String userId = user.getString("userId");
                    if (userId != null && !userId.isEmpty()) {
                        // Add to selected entrants
                        batch.set(selectedRef.document(userId), user.getData());
                        // Remove from waiting list
                        batch.delete(waitingListRef.document(user.getId()));
                    } else {
                        Log.e("RunLotteryActivity", "userId is missing for user: " + user.getId());
                    }
                }

                // Commit batch operation
                batch.commit().addOnCompleteListener(updateTask -> {
                    loadingSpinner.setVisibility(View.GONE);
                    confirmButton.setEnabled(true); // Re-enable button
                    if (updateTask.isSuccessful()) {
                        lotteryCompleted = true;
                        Toast.makeText(this, "Lottery completed successfully", Toast.LENGTH_SHORT).show();
                        fetchSelectedParticipants(); // Refresh the list
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

            TextView userIDTextView = participantView.findViewById(R.id.participant_userID);
            Button notifyButton = participantView.findViewById(R.id.notify_button);
            Button removeButton = participantView.findViewById(R.id.remove_button);


            String userId = user.getString("userId");
            if (userId == null || userId.isEmpty()) {
                userId = "User ID not provided";
            }

            userIDTextView.setText(userId);

            String finalUserId = userId;
            notifyButton.setOnClickListener(v -> {
                sendNotificationToUser(finalUserId, true);
                Toast.makeText(this, "Notification sent to " + finalUserId, Toast.LENGTH_SHORT).show();

            });

            removeButton.setOnClickListener(v -> {
                db.collection("events")
                        .document(eventId)
                        .collection("selectedEntrants")
                        .document(user.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {

                            Toast.makeText(this, "Removed " + finalUserId, Toast.LENGTH_SHORT).show();
                            participantsLayout.removeView(participantView);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to remove " + finalUserId, Toast.LENGTH_SHORT).show();

                        });
            });

            participantsLayout.addView(participantView);
        }
    }

    private void sendNotificationToUser(String userId, boolean isWinner) {
        // Reference the specific user's document
        DocumentReference userRef = db.collection("users").document(userId);

        // Create the notification message with the event name
        String message;
        int status;
        if (isWinner) {
            message = "Congratulations! You have won the lottery for " + eventName;
            status = 1;
        } else {
            message = "We regret to inform you that you did not win the lottery for " + eventName;
            status = 0;
        }

        // Add notification under the user's notifications subcollection
        userRef.collection("notifications")
                .add(new NotificationData(userId, message, status, eventName))
                .addOnSuccessListener(documentReference ->
                        Log.d("RunLotteryActivity", "Notification sent to user: " + userId))
                .addOnFailureListener(e ->
                        Log.e("RunLotteryActivity", "Failed to send notification to user: " + userId, e));
    }

    private void sendNotificationsToAllParticipants() {
        CollectionReference selectedEntrantsRef = db.collection("events").document(eventId).collection("selectedEntrants");
        CollectionReference waitingListRef = db.collection("events").document(eventId).collection("waitingList");

        // Fetch all selected entrants
        selectedEntrantsRef.get().addOnCompleteListener(selectedTask -> {
            if (selectedTask.isSuccessful() && selectedTask.getResult() != null) {
                List<DocumentSnapshot> selectedUsers = selectedTask.getResult().getDocuments();

                // Fetch all waiting list entrants
                waitingListRef.get().addOnCompleteListener(waitingTask -> {
                    if (waitingTask.isSuccessful() && waitingTask.getResult() != null) {
                        List<DocumentSnapshot> waitingUsers = waitingTask.getResult().getDocuments();

                        // Create a set of userIds of selected users
                        Set<String> selectedUserIds = new HashSet<>();
                        for (DocumentSnapshot user : selectedUsers) {
                            String userId = user.getString("userId");
                            if (userId != null && !userId.isEmpty()) {
                                selectedUserIds.add(userId);
                                sendNotificationToUser(userId, true); // Send winning notification
                            }
                        }

                        // Now, for users in waiting list who are not in selected entrants, send losing notification
                        for (DocumentSnapshot user : waitingUsers) {
                            String userId = user.getString("userId");
                            if (userId != null && !userId.isEmpty() && !selectedUserIds.contains(userId)) {
                                sendNotificationToUser(userId, false); // Send losing notification
                            }
                        }

                        Toast.makeText(this, "Notifications sent to all participants!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("RunLotteryActivity", "Failed to fetch waiting list", waitingTask.getException());
                        Toast.makeText(this, "Error fetching waiting list.", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Log.e("RunLotteryActivity", "Failed to fetch selected entrants", selectedTask.getException());
                Toast.makeText(this, "Error fetching selected participants.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void drawReplacementForDeclinedParticipant() {
        loadingSpinner.setVisibility(View.VISIBLE);

        CollectionReference waitingListRef = db.collection("events").document(eventId).collection("waitingList");
        CollectionReference selectedRef = db.collection("events").document(eventId).collection("selectedEntrants");

        waitingListRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<DocumentSnapshot> waitingList = new ArrayList<>(task.getResult().getDocuments());

                if (waitingList.isEmpty()) {
                    loadingSpinner.setVisibility(View.GONE);
                    Toast.makeText(this, "No users available in the waiting list for replacement.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Filter the waiting list for users with status 1 or no status
                DocumentSnapshot replacementUser = null;
                for (DocumentSnapshot user : waitingList) {
                    Long status = user.getLong("status"); // Assuming status is stored as a number
                    if (status == null || status == 1) {
                        // If status is null (no status) or status is 1, select the user
                        replacementUser = user;
                        break;
                    } else if (status == 0) {
                        // Skip users with status 0
                        continue;
                    }
                }

                if (replacementUser == null) {
                    loadingSpinner.setVisibility(View.GONE);
                    Toast.makeText(this, "No eligible users found for replacement.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userId = replacementUser.getString("userId");
                if (userId != null && !userId.isEmpty()) {
                    // Update Firestore to add the user to selected entrants
                    WriteBatch batch = db.batch();

                    // Add to selected entrants
                    batch.set(selectedRef.document(userId), replacementUser.getData());

                    // Remove from waiting list
                    batch.delete(waitingListRef.document(replacementUser.getId()));

                    // Commit the batch
                    batch.commit().addOnCompleteListener(updateTask -> {
                        loadingSpinner.setVisibility(View.GONE);
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(this, "Replacement drawn successfully.", Toast.LENGTH_SHORT).show();
                            fetchSelectedParticipants(); // Refresh the selected participants list
                        } else {
                            Toast.makeText(this, "Failed to draw replacement.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    loadingSpinner.setVisibility(View.GONE);
                    Toast.makeText(this, "Invalid user data. Unable to draw replacement.", Toast.LENGTH_SHORT).show();
                }
            } else {
                loadingSpinner.setVisibility(View.GONE);
                Toast.makeText(this, "Failed to fetch waiting list.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}














