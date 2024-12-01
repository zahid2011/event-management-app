package com.example.event_lottery;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QRCodeManagementActivity extends AppCompatActivity {
    private ListView qrCodeListView;
    private QRCodeAdapter qrCodeAdapter;
    private FirebaseFirestore db;
    private List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_list_qr_codes);

        qrCodeListView = findViewById(R.id.list_view);
        db = FirebaseFirestore.getInstance();
        events = new ArrayList<>();

        // Back button functionality
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Set up the adapter with an empty list initially
        qrCodeAdapter = new QRCodeAdapter(this, events);
        qrCodeListView.setAdapter(qrCodeAdapter);

        // Load events with QR codes from Firebase in real-time
        listenToQRCodesFromFirebase();
    }

    private void listenToQRCodesFromFirebase() {
        CollectionReference eventsRef = db.collection("events");

        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(QRCodeManagementActivity.this, "Error listening for updates", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshots != null) {
                    events.clear(); // Clear the list before adding new events
                    for (QueryDocumentSnapshot doc : snapshots) {
                        // Ensure the document contains qrContent and qrhash fields
                        if (doc.contains("qrContent") && doc.contains("qrhash")) {
                            String qrContent = doc.getString("qrContent");
                            String qrHash = doc.getString("qrhash");

                            // Only add events with valid QR code data
                            if (qrContent != null && !qrContent.isEmpty() && qrHash != null && !qrHash.isEmpty()) {
                                Event event = doc.toObject(Event.class);
                                event.setEventId(doc.getId());
                                event.setQrContent(qrContent);
                                event.setQrHash(qrHash);
                                events.add(event);
                            }
                        }
                    }
                    qrCodeAdapter.notifyDataSetChanged(); // Notify adapter of changes
                }
            }
        });
    }
}
