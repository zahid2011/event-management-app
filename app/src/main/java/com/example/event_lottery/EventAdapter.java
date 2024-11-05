package com.example.event_lottery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        // Assuming `list_item_event.xml` has been updated with the correct TextView IDs
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

        return convertView;
    }
}