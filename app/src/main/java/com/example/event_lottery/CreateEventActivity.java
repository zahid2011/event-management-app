package com.example.event_lottery;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


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
    private ImageView qrCodeImageView;
    private EditText etEventDateTime;
    private Calendar calendar;
    private Switch switchGeolocation; // New switch for geolocation toggle


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }


        db = FirebaseFirestore.getInstance(); // Initialize Firestore


        btnCreateEvent = findViewById(R.id.btn_create_event);
        btnGenerateQr = findViewById(R.id.btn_generate_qr);
        btnCancel = findViewById(R.id.btn_cancel);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        btnBackToDashboard = findViewById(R.id.btn_back_to_dashboard);
        switchGeolocation = findViewById(R.id.switch_geolocation); // Initialize the switch


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
            TextView evtView = findViewById(R.id.et_event_name);
            String evtID = evtView.getText().toString();


            TextView tevtView = findViewById(R.id.et_event_datetime);
            String tevtID = tevtView.getText().toString();


            TextView vtView = findViewById(R.id.et_capacity);
            String vtID = vtView.getText().toString();


            TextView tView = findViewById(R.id.et_price);
            String tID = tView.getText().toString();


            TextView desc = findViewById(R.id.et_event_description);
            String descID = desc.getText().toString();


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date;
            try {
                date = dateFormat.parse(tevtID);
            } catch (ParseException e) {
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }


            if (evtID.isEmpty() || tevtID.isEmpty() || vtID.isEmpty() || tID.isEmpty() || descID.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean geolocationEnabled = switchGeolocation.isChecked();


            Events newEvent = new Events(evtID, date, vtID, tID, descID);
            saveEventToFirestore(newEvent);
        });


        //  switchGeolocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
        //  if (isChecked) {
        //     Toast.makeText(CreateEventActivity.this, "Geolocation required for this event", Toast.LENGTH_SHORT).show();
        //  } else {
        //  Toast.makeText(CreateEventActivity.this, "Geolocation requirement removed", Toast.LENGTH_SHORT).show();
        //}
        //    });




        btnGenerateQr.setOnClickListener(v ->


        {
            TextView evtView = findViewById(R.id.et_event_name);
            String evtID = evtView.getText().toString();
            String qrContent = "myapp://event/" + evtID;


            Bitmap qrCodeBitmap = generateQRCode(qrContent);
            if (qrCodeBitmap != null) {
                qrCodeImageView.setImageBitmap(qrCodeBitmap);




                String qrHash = generateHashFromBitmap(qrCodeBitmap);
                if (qrHash != null) {
                    db.collection("events").document(evtID)
                            .set(Collections.singletonMap("qrhash", qrHash), SetOptions.merge())
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Event and QR hash stored successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to store event data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(this, "Failed to generate QR hash", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to generate QR Code", Toast.LENGTH_SHORT).show();
            }
        });




        btnCancel.setOnClickListener(v -> finish()); // Close the activity
    }

    // Setter for Firestore to allow testing with mock instances
    public void setFirestore(FirebaseFirestore firestore) {
        this.db = firestore;
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


                    // Format date and time and display it in EditText
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    etEventDateTime.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }


    private void saveEventToFirestore(Events event) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventName", event.getEventName());
        eventData.put("eventDateTime", event.getEventDateTime());
        eventData.put("capacity", event.getCapacity());
        eventData.put("price", event.getPrice());
        eventData.put("description", event.getDescription());
        eventData.put("geolocationEnabled", switchGeolocation.isChecked());


        // Save event data to Firestore including waitlist
        db.collection("events").document(event.getEventName())
                .set(eventData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CreateEventActivity.this, "Event Created Successfully", Toast.LENGTH_SHORT).show();
                    createWaitingList(event.getEventName()); // Call to create an empty waiting list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateEventActivity.this, "Failed to create event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }




    private void createWaitingList(String eventName) {
        // Example entry in the waiting list
        Map<String, Object> waitingEntry = new HashMap<>();
        waitingEntry.put("userId", "sampleUserId");
        waitingEntry.put("timestamp", new Date());


        // Add this entry to the "waitingList" subcollection under the event document
        db.collection("events").document(eventName)
                .collection("waitingList")
                .add(waitingEntry)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Waiting list entry created", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to create waiting list entry: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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


    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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
}