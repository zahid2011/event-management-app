package com.example.event_lottery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EntrantWaitingListAdapter extends ArrayAdapter<Event> {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
    private final Context context;

    public EntrantWaitingListAdapter(@NonNull Context context, @NonNull List<Event> events) {
        super(context, 0, events);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_event, parent, false);
        }

        // Retrieve the event at the current position
        Event event = getItem(position);

        // Reference to UI components
        TextView eventNameTextView = convertView.findViewById(R.id.event_name);
        TextView eventDateTimeTextView = convertView.findViewById(R.id.event_date_time);
        TextView capacityTextView = convertView.findViewById(R.id.event_capacity);
        TextView priceTextView = convertView.findViewById(R.id.event_price);
        Button detailsButton = convertView.findViewById(R.id.details_button);

        // Validate the event object
        if (event == null) {
            Toast.makeText(context, "Event data is missing or invalid!", Toast.LENGTH_SHORT).show();
            return convertView;
        }

        // Populate the UI with event data
        eventNameTextView.setText(event.getEventName() != null ? event.getEventName() : "Unknown Event");

        // Format the event's date and time
        Timestamp timestamp = event.getEventDateTime();
        if (timestamp != null) {
            String formattedDate = sdf.format(timestamp.toDate());
            eventDateTimeTextView.setText(formattedDate);
        } else {
            eventDateTimeTextView.setText("No date provided");
        }

        // Set the event capacity
        String capacity = event.getCapacity() != null ? event.getCapacity() : "N/A";
        capacityTextView.setText("Capacity: " + capacity);

        // Set the event price
        String price = event.getPrice() != null ? event.getPrice() : "N/A";
        priceTextView.setText("Price: $" + price);

        // Handle the "Details" button click
        detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EntrantEventWaitingDetails.class);

            // Validate and pass event details
            intent.putExtra("eventId", event.getEventId() != null ? event.getEventId() : "");
            intent.putExtra("eventName", event.getEventName() != null ? event.getEventName() : "Unknown Event");
            intent.putExtra("eventDate", timestamp != null ? sdf.format(timestamp.toDate()) : "No date provided");
            intent.putExtra("eventCapacity", capacity);
            intent.putExtra("eventPrice", price);
            intent.putExtra("eventDescription", event.getDescription() != null ? event.getDescription() : "No description available");

            // Debugging log
            Log.d("EntrantWaitingListAdapter", "Navigating to details for event: " + event.getEventName());

            // Start the details activity
            context.startActivity(intent);
        });

        return convertView;
    }
}
