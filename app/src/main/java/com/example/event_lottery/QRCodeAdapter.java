package com.example.event_lottery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class QRCodeAdapter extends ArrayAdapter<Event> {
    public QRCodeAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_qr_code, parent, false);
        }

        Event event = getItem(position);

        // Initialize UI components
        TextView eventNameTextView = convertView.findViewById(R.id.event_name);
        TextView eventDateTimeTextView = convertView.findViewById(R.id.event_date_time);
        TextView eventCapacityTextView = convertView.findViewById(R.id.event_capacity);
        TextView eventPriceTextView = convertView.findViewById(R.id.event_price);
        Button viewQRCodeButton = convertView.findViewById(R.id.view_qr_code_button);

        // Set event name
        eventNameTextView.setText(event.getEventName() != null ? event.getEventName() : "No Name");

        // Format and set event date and time
        if (event.getEventDateTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy 'at' hh:mm a", Locale.getDefault());
            String formattedDateTime = sdf.format(event.getEventDateTime().toDate());
            eventDateTimeTextView.setText(formattedDateTime);
        } else {
            eventDateTimeTextView.setText("Date & Time: N/A");
        }

        // Set capacity
        eventCapacityTextView.setText(event.getCapacity() != null ? "Capacity: " + event.getCapacity() : "Capacity: N/A");

        // Set price
        eventPriceTextView.setText(event.getPrice() != null ? "Price: $" + event.getPrice() : "Price: N/A");

        // Handle QR code button
        if (event.getQrContent() == null || event.getQrHash() == null) {
            // Disable the button and show a placeholder message
            viewQRCodeButton.setEnabled(false);
            viewQRCodeButton.setText("No QR Code");
        } else {
            // Enable the button and set the click listener
            viewQRCodeButton.setEnabled(true);
            viewQRCodeButton.setText("View QR Code");
            viewQRCodeButton.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), QRCodeDetailsActivity.class);
                intent.putExtra("eventId", event.getEventId());
                intent.putExtra("eventName", event.getEventName());
                intent.putExtra("eventDateTime", event.getEventDateTime().toDate().toString()); // Pass as a String
                intent.putExtra("description", event.getDescription());
                intent.putExtra("capacity", event.getCapacity());
                intent.putExtra("price", event.getPrice());
                intent.putExtra("geoLocation", event.isGeoLocationEnabled());
                intent.putExtra("qrContent", event.getQrContent());
                getContext().startActivity(intent);
            });
        }

        return convertView;
    }
}
