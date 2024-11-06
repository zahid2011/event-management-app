package com.example.event_lottery;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.FirebaseFirestore;
import android.graphics.Bitmap;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CreateEventActivity extends AppCompatActivity {


    private Button btnCreateEvent, btnGenerateQr, btnCancel, btnBackToDashboard;
    private FirebaseFirestore db;
    private ImageView qrCodeImageView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        btnCreateEvent = findViewById(R.id.btn_create_event);
        btnGenerateQr = findViewById(R.id.btn_generate_qr);
        btnCancel = findViewById(R.id.btn_cancel);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        btnBackToDashboard = findViewById(R.id.btn_back_to_dashboard);

        btnBackToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to OrganizerDashboardActivity
                Intent intent = new Intent(CreateEventActivity.this, OrganizerDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Close the CreateEventActivity
            }
        });


        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = null;
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
                try {
                    // Parse the string into a Date object
                    date = dateFormat.parse(tevtID);
                } catch (ParseException e) {
                    e.printStackTrace();
                    //return null; // Return null if parsing fails
                }
                // Basic validation
                if (evtID.isEmpty() || tevtID.isEmpty() || vtID.isEmpty() || tID.isEmpty() || descID.isEmpty()) {
                    Toast.makeText(CreateEventActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Create Event object
                Events newEvent = new Events(evtID, date, vtID, tID, descID);




                // Save event to Firestore
                saveEventToFirestore(newEvent);
                Toast.makeText(CreateEventActivity.this, "Event saved", Toast.LENGTH_SHORT).show();
            }
        });


        btnGenerateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = null;
                TextView evtView = findViewById(R.id.et_event_name);
                String evtID = evtView.getText().toString();


                TextView dateTimeView = findViewById(R.id.et_event_datetime);
                String tevtID = dateTimeView.getText().toString();

                TextView desc = findViewById(R.id.et_event_description);
                String descID = desc.getText().toString();


                TextView vtView = findViewById(R.id.et_capacity);
                String vtID = vtView.getText().toString();


                TextView tView = findViewById(R.id.et_price);
                String tID = tView.getText().toString();


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    // Parse the string into a Date object
                    date = dateFormat.parse(tevtID);
                } catch (ParseException e) {
                    e.printStackTrace();
                    //return null; // Return null if parsing fails
                }


                String qrContent = "Event: " + evtID + "\nDescription: " + descID + "\nCapacity: " + vtID +
                        "\nDateTime: " + date + "\nPrice: " + tID;


                // Generate QR Code Bitmap
                Toast.makeText(CreateEventActivity.this, "before bitmap", Toast.LENGTH_SHORT).show();
                Bitmap qrCodeBitmap = generateQRCode(qrContent);
                if (qrCodeBitmap != null) {
                    Toast.makeText(CreateEventActivity.this, "before hash", Toast.LENGTH_SHORT).show();
                    qrCodeImageView.setImageBitmap(qrCodeBitmap);


                    // Generate hash of the QR code bitmap
                    String qrHash = generateHashFromBitmap(qrCodeBitmap);
                    Toast.makeText(CreateEventActivity.this, qrHash, Toast.LENGTH_SHORT).show();
                    // Store the hash in Firebase
                    if (qrHash != null) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        //Map<String, Object> eventData = new HashMap<>();
                        //  eventData.put("eventName", evtID);
                        // eventData.put("capacity", vtID);
                        //  eventData.put("dateTime", formattedDateTime);
                        // eventData.put("price", tID);
                        //  eventData.put("qrHash", qrHash);


                        db.collection("events").document(evtID)
                                .update("qrhash",qrHash)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(CreateEventActivity.this, "Event and QR hash stored successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(CreateEventActivity.this, "Failed to store event data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(CreateEventActivity.this, "Failed to generate QR hash", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateEventActivity.this, "Failed to generate QR Code", Toast.LENGTH_SHORT).show();
                }
            }
        });






        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   finish(); // Close the current activity and return
            }
        });
    }


    private void saveEventToFirestore(Events event) {
        db.collection("events").document(event.getEventName()) // Use event name as the document ID
                .set(event)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CreateEventActivity.this, "Event Created Successfully", Toast.LENGTH_SHORT).show();
                    //   finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateEventActivity.this, "Failed to create event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
