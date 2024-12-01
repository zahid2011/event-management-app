package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        // Back Button functionality
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(AdminDashboardActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // Close AdminDashboardActivity
            }
        });

        // "Event Management" card
        CardView eventManagementButton = findViewById(R.id.event_management_button);
        eventManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigating to EventManagementActivity
                Intent intent = new Intent(AdminDashboardActivity.this, EventManagementActivity.class);
                startActivity(intent);
            }
        });

        // Profile Management Card
        CardView profileManagementButton = findViewById(R.id.profile_management_button);
        profileManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AdminProfileManagementActivity
                Intent intent = new Intent(AdminDashboardActivity.this, AdminProfileManagementActivity.class);
                startActivity(intent);
            }
        });

        // Image Management Card
        CardView imageManagementButton = findViewById(R.id.image_management_button);
        imageManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AdminImageManagementActivity
                Intent intent = new Intent(AdminDashboardActivity.this, AdminImageManagementActivity.class);
                startActivity(intent);
            }
        });

        // QR Code Data Card
        CardView qrCodeDataButton = findViewById(R.id.qr_code_data_button);
        qrCodeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, QRCodeManagementActivity.class);
                startActivity(intent);
            }
        });

        // Facilities Card
        CardView facilitiesButton = findViewById(R.id.facilities_button);
        facilitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AdminListFacilityActivity
                Intent intent = new Intent(AdminDashboardActivity.this, AdminListFacilityAcitivity.class);
                startActivity(intent);
            }
        });
    }
}
