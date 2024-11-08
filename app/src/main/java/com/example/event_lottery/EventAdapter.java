package com.example.event_lottery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends ArrayAdapter<Event> {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());

    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_event, parent, false);
        }

        Event event = getItem(position);

        TextView eventNameTextView = convertView.findViewById(R.id.event_name);
        TextView eventDateTimeTextView = convertView.findViewById(R.id.event_date_time);
        TextView capacityTextView = convertView.findViewById(R.id.event_capacity);
        TextView priceTextView = convertView.findViewById(R.id.event_price);

        // Set the values to the TextViews
        eventNameTextView.setText(event.getEventName());

        // Convert Timestamp to a formatted date string
        Timestamp timestamp = event.getEventDateTime();
        if (timestamp != null) {
            String formattedDate = sdf.format(timestamp.toDate());
            eventDateTimeTextView.setText(formattedDate);
        } else {
            eventDateTimeTextView.setText(""); // Or handle null case as needed
        }

        capacityTextView.setText("Capacity: " + event.getCapacity());
        priceTextView.setText("Price: $" + event.getPrice());

        Button detailsButton = convertView.findViewById(R.id.details_button);
        detailsButton.setOnClickListener(v -> {

            Intent intent = new Intent(getContext(), AdminEventDetailsActivity.class);

            // passing the event details to the intent, including eventId and eventDescription
            intent.putExtra("eventId", event.getEventId()); 
            intent.putExtra("eventName", event.getEventName());
            intent.putExtra("eventDate", timestamp != null ? sdf.format(timestamp.toDate()) : "");
            intent.putExtra("eventCapacity", event.getCapacity()); 
            intent.putExtra("eventPrice", event.getPrice());       
            intent.putExtra("eventDescription", event.getDescription()); 

            // Start the activity
            getContext().startActivity(intent);
        });

        return convertView;
    }

}
