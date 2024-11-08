package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Import ZXing libraries
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeScannerActivity extends AppCompatActivity {

    private static final String TAG = "QRCodeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the QR code scanner
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(true); // Allow rotation if needed
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Retrieve scan result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedData = result.getContents();
                Log.d(TAG, "Scanned data: " + scannedData);

                // Extract event name from scanned data
                String eventName = extractEventName(scannedData);

                if (eventName != null && !eventName.isEmpty()) {
                    // Start EventDetailsActivity with event name
                    Intent intent = new Intent(QRCodeScannerActivity.this, Entrant_qr_code_activity.class);
                    intent.putExtra("event_name", eventName);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Event name not found in QR code", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                // Scan was canceled
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // No result obtained
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String extractEventName(String scannedData) {
        String eventName = "";
        try {
            if (scannedData.contains("Event:")) {
                String[] parts = scannedData.split("Event:");
                if (parts.length > 1) {
                    String rest = parts[1];
                    String[] eventNameParts = rest.split("Description:");
                    eventName = eventNameParts[0].trim();
                    Log.d(TAG, "Extracted event name: " + eventName);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error extracting event name", e);
        }
        return eventName;
    }
}
