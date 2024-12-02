package com.example.event_lottery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;




import androidx.appcompat.app.AppCompatActivity;


import com.example.event_lottery.OrganizerDashboardActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;




import java.util.HashMap;
import java.util.Map;




public class ManageFacilityActivity extends AppCompatActivity {

    private EditText etFacilityName, etFacilityAddress, etPhone, etFacilityDescription, etFacilityOwner;
    private Button btnCreate, btnSaveChanges, btnGetFacility;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_profile);


        db = FirebaseFirestore.getInstance();


        etFacilityName = findViewById(R.id.et_facility_name);
        etFacilityOwner = findViewById(R.id.et_facility_owner);
        etFacilityAddress = findViewById(R.id.et_facility_address);
        etPhone = findViewById(R.id.et_phone);
        etFacilityDescription = findViewById(R.id.et_facility_description);
        btnSaveChanges = findViewById(R.id.btn_create);
        btnGetFacility = findViewById(R.id.btn_get_facility);

        // fetching user data from SharedPreferences and update facility owner
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", "");
        etFacilityOwner.setText(userId);


        // "Create/Save Changes" button functionality
        btnSaveChanges.setOnClickListener(v -> saveFacilityProfile());

        // Back button setup
        ImageButton btnBackToDashboard = findViewById(R.id.backButton);
        if (btnBackToDashboard == null) {
            Log.e("ManageFacilityActivity", "ImageButton backButton is null. Check the layout file.");
        } else {
            btnBackToDashboard.setOnClickListener(v -> {
                // checking if the intent has the "fromMainActivity" flag
                boolean fromMainActivity = getIntent().getBooleanExtra("fromMainActivity", false);

                Intent intent;
                if (fromMainActivity) {
                    // navigating back to MainActivity
                    intent = new Intent(ManageFacilityActivity.this, MainActivity.class);
                } else {
                    // navigating back to OrganizerDashboardActivity
                    intent = new Intent(ManageFacilityActivity.this, OrganizerDashboardActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        btnGetFacility.setOnClickListener(v -> {
            String facilityName = etFacilityName.getText().toString().trim();
            if (!facilityName.isEmpty()) {
                fetchFacilityData(facilityName);
            } else {
                Toast.makeText(ManageFacilityActivity.this, "Enter facility name to search", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFacilityProfile() {
        String facilityName = etFacilityName.getText().toString().trim();
        String facilityAddress = etFacilityAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String facilityDescription = etFacilityDescription.getText().toString().trim();
        String facilityOwner = etFacilityOwner.getText().toString().trim();

        if (facilityName.isEmpty() || facilityAddress.isEmpty() || phone.isEmpty() || facilityOwner.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> facilityData = new HashMap<>();
        facilityData.put("facilityName", facilityName);
        facilityData.put("facilityAddress", facilityAddress);
        facilityData.put("phone", phone);
        facilityData.put("facilityDescription", facilityDescription);
        facilityData.put("facilityOwner", facilityOwner);

        // saving the data to Firestore under a document named after the facility
        db.collection("facilities").document(facilityName)
                .set(facilityData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ManageFacilityActivity.this, "Facility profile created successfully", Toast.LENGTH_SHORT).show();

                    // updating the user's role2 in Firestore to "organiser"
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    String userId = sharedPreferences.getString("USER_ID", "");

                    if (!userId.isEmpty()) {
                        db.collection("users").document(userId).update("role2", "Organiser")
                                .addOnSuccessListener(roleUpdateTask -> {
                                    Toast.makeText(ManageFacilityActivity.this, "You have been granted organizer privileges.", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ManageFacilityActivity.this, "Failed to update role: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }

                    // Navigating back to the previous page
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(ManageFacilityActivity.this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void fetchFacilityData(String facilityName) {
        db.collection("facilities").document(facilityName).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            etFacilityAddress.setText(document.getString("facilityAddress"));
                            etPhone.setText(document.getString("phone"));
                            etFacilityDescription.setText(document.getString("facilityDescription"));
                            etFacilityOwner.setText(document.getString("facilityOwner"));
                            Toast.makeText(ManageFacilityActivity.this, "Facility data loaded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ManageFacilityActivity.this, "Facility not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ManageFacilityActivity.this, "Error fetching facility data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
