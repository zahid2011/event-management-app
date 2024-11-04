package com.example.event_lottery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateEventActivity extends AppCompatActivity {

    private Button btnCreateEvent, btnGenerateQr, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        btnCreateEvent = findViewById(R.id.btn_create_event);
        btnGenerateQr = findViewById(R.id.btn_generate_qr);
        btnCancel = findViewById(R.id.btn_cancel);

        // onClickListener for the creatfge button
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(CreateEventActivity.this, "Event Created!", Toast.LENGTH_SHORT).show();


                finish();
            }
        });

        // Set onClickListener for the Generate QR Code button
        btnGenerateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action for Generate QR Code button
                Toast.makeText(CreateEventActivity.this, "QR Code Generated!", Toast.LENGTH_SHORT).show();

               //QR code generation functionality here if needed
            }
        });

        //onClickListener for the Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish(); // This will close the current activity and return
            }
        });
    }
}
