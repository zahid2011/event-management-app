package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        // "Event Management" button
        Button eventManagementButton = findViewById(R.id.event_management_button);
        eventManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EventManagementActivity
                Intent intent = new Intent(AdminDashboardActivity.this, EventManagementActivity.class);
                startActivity(intent);
            }
        });

        // Placeholder for other buttons - add functionality later
        Button profileManagementButton = findViewById(R.id.profile_management_button);
        profileManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminDashboardActivity.this, "Profile Management - Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        Button imageManagementButton = findViewById(R.id.image_management_button);
        imageManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminDashboardActivity.this, "Image Management - Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        Button qrCodeDataButton = findViewById(R.id.qr_code_data_button);
        qrCodeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminDashboardActivity.this, "QR Code Data - Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        Button facilitiesButton = findViewById(R.id.facilities_button);
        facilitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminDashboardActivity.this, "Facilities - Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        Button logoutButton = findViewById(R.id.button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
                // Clear the activity stack to prevent returning to the dashboard
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Finish the current activity
                Toast.makeText(AdminDashboardActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
