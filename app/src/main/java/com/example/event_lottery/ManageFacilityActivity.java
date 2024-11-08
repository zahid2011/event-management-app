package com.example.event_lottery;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;


//In this code, it is set so that if you enter a facility name and the owner of that facility who created it,
// and then press get, it will populate all the fields with the data that was saved intially so that you can edit it and save it again if you wish


public class ManageFacilityActivity extends AppCompatActivity {


    // Declare UI elements
    public EditText etFacilityName;
    public EditText etFacilityAddress;
    private EditText etFacilityAddress2;
    public EditText etPhone;
    public EditText etEmail;
    public EditText etFacilityDescription;
    private EditText etFacilityOwner;
    private Switch switchGeolocation;
    private Button btnCreate;
    public Button btnSaveChanges;
    public Button btnGetFacility;
    private FirebaseFirestore db;
    private String lastToastMessage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_profile); // Use your layout file


        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();


        // Link UI elements with XML IDs
        etFacilityName = findViewById(R.id.et_facility_name);
        etFacilityOwner = findViewById(R.id.et_facility_owner);
        //etFacilityName = findViewById(R.id.et_facility_name);
        etFacilityAddress = findViewById(R.id.et_facility_address);
      //  etFacilityAddress2 = findViewById(R.id.et_facility_address_2);
        etPhone = findViewById(R.id.et_phone);
       // etEmail = findViewById(R.id.et_email);
        etFacilityDescription = findViewById(R.id.et_facility_description);
        switchGeolocation = findViewById(R.id.switch_geolocation);
     //   btnCreate = findViewById(R.id.btn_create);
        btnSaveChanges = findViewById(R.id.btn_save_changes);
        btnGetFacility = findViewById(R.id.btn_get_facility);


       // btnCreate.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View v) {
          //      saveFacilityProfile();
         //   }
     //   });


        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFacilityProfile();
            }
        });

        Button btnBackToDashboard = findViewById(R.id.btn_back_to_dashboard);
        btnBackToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the OrganizerDashboardActivity
                Intent intent = new Intent(ManageFacilityActivity.this, OrganizerDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Close ManageFacilityActivity
            }
        });


btnGetFacility.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v){
        String facilityName = etFacilityName.getText().toString().trim();
        String facilityOwner = etFacilityOwner.getText().toString().trim();
        if (!facilityName.isEmpty()) {
            fetchFacilityData(facilityName);
        } else {
            Toast.makeText(ManageFacilityActivity.this, "Enter facility name to search", Toast.LENGTH_SHORT).show();
        }
    }
    });
}

    private void saveFacilityProfile() {
        // Get input values
        String facilityName = etFacilityName.getText().toString().trim();
        String facilityAddress = etFacilityAddress.getText().toString().trim();
        //String facilityAddress2 = etFacilityAddress2.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        //String email = etEmail.getText().toString().trim();
        String facilityDescription = etFacilityDescription.getText().toString().trim();
        boolean geolocationEnabled = switchGeolocation.isChecked();


        // Validation check for required fields
        if (facilityName.isEmpty() || facilityAddress.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }


        // Create a map to store facility profile data
        Map<String, Object> facilityData = new HashMap<>();
        facilityData.put("facilityName", facilityName);
        facilityData.put("facilityAddress", facilityAddress);
       // facilityData.put("facilityAddress2", facilityAddress2);
        facilityData.put("phone", phone);
       // facilityData.put("email", email);
        facilityData.put("facilityDescription", facilityDescription);
        facilityData.put("geolocationEnabled", geolocationEnabled);


        // Save the data to Firestore under a document named after the facility
        db.collection("facilities").document(facilityName)
                .set(facilityData)
                .addOnSuccessListener(aVoid -> Toast.makeText(ManageFacilityActivity.this, "Facility profile saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ManageFacilityActivity.this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    public void showToast(String message) {
        lastToastMessage = message;  // Store message in variable for testing
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();  // Show regular toast
    }

    // Getter to access the last Toast message in tests
    public String getLastToastMessage() {
        return lastToastMessage;
    }

private void fetchFacilityData(String facilityName) {
    db.collection("facilities").document(facilityName).get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        etFacilityAddress.setText(document.getString("facilityAddress"));
                        //etFacilityAddress2.setText(document.getString("facilityAddress2"));
                        etPhone.setText(document.getString("phone"));
                        //etEmail.setText(document.getString("email"));
                        etFacilityDescription.setText(document.getString("facilityDescription"));
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
