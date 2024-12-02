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
    private boolean testMode = false;
    private AdminFacility mockFacility; // mock facility data for testing

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_facility_details);

        db = FirebaseFirestore.getInstance();

        // initializing UI components
        facilityNameTextView = findViewById(R.id.facility_name);
        facilityDescriptionTextView = findViewById(R.id.facility_description);
        facilityAddressTextView = findViewById(R.id.facility_address);
        phoneTextView = findViewById(R.id.facility_phone);
        geolocationStatusTextView = findViewById(R.id.geolocation_status);
        removeFacilityButton = findViewById(R.id.remove_facility_button);
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(v -> finish());

        // checks if we are in test mode
        testMode = getIntent().getBooleanExtra("TEST_MODE", false);
        if (testMode) {
            // Using mock data instead of fetching from Firebase
            String mockFacilityId = getIntent().getStringExtra("facilityId");
            String mockFacilityName = getIntent().getStringExtra("facilityName");
            String mockFacilityDescription = getIntent().getStringExtra("facilityDescription");
            String mockFacilityAddress = getIntent().getStringExtra("facilityAddress");
            String mockFacilityPhone = getIntent().getStringExtra("phone");
            boolean mockGeolocationEnabled = getIntent().getBooleanExtra("geolocationEnabled", false);

            // Populating mock facility data
            AdminFacility mockFacility = new AdminFacility(
                    mockFacilityId, mockFacilityName, mockFacilityDescription,
                    mockFacilityAddress, mockFacilityPhone, mockGeolocationEnabled
            );

            // Populating the UI with the mock data
            initializeUI(mockFacility);
            return;
        }

        // normal flow: fetchs facility ID from the intent
        facilityId = getIntent().getStringExtra("facilityId");
        if (facilityId == null || facilityId.isEmpty()) {
            Toast.makeText(this, "Facility ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // fetchs facility details from Firebase
        fetchFacilityDetails();

        // handles remove facility button click
        removeFacilityButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    /**
     * Initializes the UI with the facility details.
     *
     * @param facility The AdminFacility object containing the facility details to display.
     */
    private void initializeUI(AdminFacility facility) {
        facilityNameTextView = findViewById(R.id.facility_name);
        facilityDescriptionTextView = findViewById(R.id.facility_description);
        facilityAddressTextView = findViewById(R.id.facility_address);
        phoneTextView = findViewById(R.id.facility_phone);
        geolocationStatusTextView = findViewById(R.id.geolocation_status);
        removeFacilityButton = findViewById(R.id.remove_facility_button);
        backButton = findViewById(R.id.back_button);

        this.mockFacility = facility;

        // Populating UI with facility details
        facilityNameTextView.setText(facility.getFacilityName());
        facilityDescriptionTextView.setText(facility.getFacilityDescription());
        facilityAddressTextView.setText(facility.getFacilityAddress());
        phoneTextView.setText(facility.getPhone());
        geolocationStatusTextView.setText(facility.isGeolocationEnabled() ? "Enabled" : "Disabled");

        removeFacilityButton.setOnClickListener(v -> showDeleteConfirmationDialog()); // handling the remove facility action
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Fetches facility details from Firebase Firestore and updates the UI accordingly.
     */
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

    /**
     * Displays a confirmation dialog to the user before deleting the facility.
     */
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Facility")
                .setMessage("Are you sure you want to delete this facility?")
                .setPositiveButton("Yes", (dialog, which) -> deleteFacility())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Deletes the facility from Firebase Firestore or performs mock deletion if in test mode.
     */
    private void deleteFacility() {
        if (testMode) {
            // mock deletion logic for test mode
            Log.d(TAG, "Mock facility deletion: " + mockFacility.getFacilityName());
            Toast.makeText(this, "Mock facility deleted successfully", Toast.LENGTH_SHORT).show();
            finish(); // ending the activity to simulate deletion
        } else {
            // Actual Firebase deletion logic
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
}
