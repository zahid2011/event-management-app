package com.example.event_lottery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Import ZXing libraries
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class QRCodeScannerActivity extends AppCompatActivity {

    private static final String TAG = "QRCodeScannerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the QR code scanner
        IntentIntegrator integrator = new IntentIntegrator(this);
         // Allow rotation if needed
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR Code");
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

                // Parse the scanned data as URI
                try {
                    Uri uri = Uri.parse(scannedData);
                    String scheme = uri.getScheme();
                    String host = uri.getHost();
                    List<String> pathSegments = uri.getPathSegments();

                    if ("myapp".equals(scheme) && "event".equals(host) && pathSegments.size() > 0) {
                        String eventName = pathSegments.get(0); // assuming eventName is the first path segment
                        Log.d(TAG, "Extracted event name: " + eventName);

                        // Start Entrant_qr_code_activity with event name
                        Intent intent = new Intent(QRCodeScannerActivity.this, Entrant_qr_code_activity.class);
                        intent.putExtra("event_name", eventName);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid QR code format", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing QR code", e);
                    Toast.makeText(this, "Error parsing QR code", Toast.LENGTH_SHORT).show();
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
}
