package com.example.event_lottery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {

    private Button btnCreateEvent, btnGenerateQr, btnCancel, btnBackToDashboard;
    private FirebaseFirestore db;
    private ImageView qrCodeImageView, imgEventImage;
    private EditText etEventDateTime;
    private Calendar calendar;
    private Switch switchGeolocation;
    private String imageUrl; // To store the image URL


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        db = FirebaseFirestore.getInstance();

        btnCreateEvent = findViewById(R.id.btn_create_event);
        btnGenerateQr = findViewById(R.id.btn_generate_qr);
        btnCancel = findViewById(R.id.btn_cancel);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        btnBackToDashboard = findViewById(R.id.btn_back_to_dashboard);
        switchGeolocation = findViewById(R.id.switch_geolocation);
        imgEventImage = findViewById(R.id.img_event_image);

        etEventDateTime = findViewById(R.id.et_event_datetime);
        calendar = Calendar.getInstance();

        etEventDateTime.setOnClickListener(v -> showDatePicker());

        btnBackToDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(CreateEventActivity.this, OrganizerDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnCreateEvent.setOnClickListener(v -> {
            String eventName = ((EditText) findViewById(R.id.et_event_name)).getText().toString().trim();
            String eventDateTime = etEventDateTime.getText().toString().trim();
            String capacity = ((EditText) findViewById(R.id.et_capacity)).getText().toString().trim();
            String price = ((EditText) findViewById(R.id.et_price)).getText().toString().trim();
            String description = ((EditText) findViewById(R.id.et_event_description)).getText().toString().trim();

            if (eventName.isEmpty() || eventDateTime.isEmpty() || capacity.isEmpty() || price.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date;
            try {
                date = dateFormat.parse(eventDateTime);
            } catch (ParseException e) {
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean geolocationEnabled = switchGeolocation.isChecked();

            // Save event without image URL initially
            saveEventToFirestore(eventName, date, capacity, price, description, geolocationEnabled, imageUrl);
        });

        btnGenerateQr.setOnClickListener(v -> {
            String eventName = ((EditText) findViewById(R.id.et_event_name)).getText().toString().trim();
            if (eventName.isEmpty()) {
                Toast.makeText(this, "Please enter an event name first", Toast.LENGTH_SHORT).show();
                return;
            }

            String qrContent = "myapp://event/" + eventName;
            Bitmap qrCodeBitmap = generateQRCode(qrContent);
            if (qrCodeBitmap != null) {
                qrCodeImageView.setImageBitmap(qrCodeBitmap);

                String qrHash = generateHashFromBitmap(qrCodeBitmap);
                if (qrHash != null) {
                    db.collection("events").document(eventName)
                            .set(Collections.singletonMap("qrhash", qrHash), SetOptions.merge())
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "QR Code stored successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to store QR Code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(this, "Failed to generate QR hash", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to generate QR Code", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    showTimePicker();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    etEventDateTime.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void saveEventToFirestore(String eventName, Date eventDateTime, String capacity, String price, String description, boolean geolocationEnabled, String imageUrl) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventName", eventName);
        eventData.put("eventDateTime", eventDateTime);
        eventData.put("capacity", capacity);
        eventData.put("price", price);
        eventData.put("description", description);
        eventData.put("geolocationEnabled", geolocationEnabled);
        eventData.put("imageUrl", imageUrl);


        db.collection("events").document(eventName)
                .set(eventData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event created successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to create event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void onAddImageClicked(View view) {
        // Create an input dialog to prompt for the image URL
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image URL");

        // Input field for the URL
        final EditText input = new EditText(this);
        input.setHint("Enter image URL");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String url = input.getText().toString().trim();

            if (!url.isEmpty()) {
                // Load the image into the ImageView
                loadImageFromUrl(url);

                // Store the URL in the imageUrl variable
                imageUrl = url;

                Toast.makeText(this, "Image URL added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "URL cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }



    private void loadImageFromUrl(String url) {
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_image_placeholder) // Optional: Placeholder image
                .error(R.drawable.ic_error) // Optional: Error image
                .into(imgEventImage); // Load into the ImageView
    }




    public Bitmap generateQRCode(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            int width = 300;
            int height = 300;
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String generateHashFromBitmap(Bitmap bitmap) {
        byte[] bitmapBytes = bitmapToByteArray(bitmap);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(bitmapBytes);

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
