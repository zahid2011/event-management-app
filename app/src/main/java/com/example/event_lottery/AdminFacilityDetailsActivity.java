package com.example.event_lottery;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminFacilityDetailsActivity extends AppCompatActivity {
    private static final String TAG = "AdminFacilityDetails";

    private TextView facilityNameTextView, facilityDescriptionTextView, facilityAddressTextView, facilityAddress2TextView, phoneTextView, geolocationStatusTextView;
    private Button removeFacilityButton;
    private ImageButton backButton;

    private String facilityId; // unique facility ID from Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_facility_details);


        db = FirebaseFirestore.getInstance();

        // retrieving facility ID from the intent
        facilityId = getIntent().getStringExtra("facilityId");
        if (facilityId == null || facilityId.isEmpty()) {
            Toast.makeText(this, "Facility ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        facilityNameTextView = findViewById(R.id.facility_name);
        facilityDescriptionTextView = findViewById(R.id.facility_description);
        facilityAddressTextView = findViewById(R.id.facility_address);
        phoneTextView = findViewById(R.id.facility_phone);
        geolocationStatusTextView = findViewById(R.id.geolocation_status);
        removeFacilityButton = findViewById(R.id.remove_facility_button);
        backButton = findViewById(R.id.back_button);

        // setting up back button
        backButton.setOnClickListener(v -> finish());

        fetchFacilityDetails();

        // removing facility button
        removeFacilityButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void fetchFacilityDetails() {
        db.collection("facilities").document(facilityId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        AdminFacility facility = documentSnapshot.toObject(AdminFacility.class);
                        if (facility != null) {
                            facilityNameTextView.setText(facility.getFacilityName());
                            facilityDescriptionTextView.setText(facility.getFacilityDescription());
                            facilityAddressTextView.setText(facility.getFacilityAddress());
                            phoneTextView.setText(facility.getPhone());
                            geolocationStatusTextView.setText(facility.isGeolocationEnabled() ? "Enabled" : "Disabled");
                        }
                    } else {
                        Toast.makeText(this, "Facility not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching facility details", e);
                    Toast.makeText(this, "Error fetching details", Toast.LENGTH_SHORT).show();
                });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Facility")
                .setMessage("Are you sure you want to delete this facility?")
                .setPositiveButton("Yes", (dialog, which) -> deleteFacility())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteFacility() {
        db.collection("facilities").document(facilityId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminFacilityDetailsActivity.this, "Facility deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting facility", e);
                    Toast.makeText(AdminFacilityDetailsActivity.this, "Error deleting facility", Toast.LENGTH_SHORT).show();
                });
    }
}
