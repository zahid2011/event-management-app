package com.example.event_lottery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FinalizedParticipantsAdapter extends ArrayAdapter<String> {

    public FinalizedParticipantsAdapter(Context context, List<String> participants) {
        super(context, R.layout.participant_item, participants);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.participant_item, parent, false);
        }

        String userId = getItem(position);

        TextView userIDTextView = convertView.findViewById(R.id.participant_userID);
        userIDTextView.setText(userId);

      //  Button notifyButton = convertView.findViewById(R.id.notify_button);
    //    Button removeButton = convertView.findViewById(R.id.remove_button);

        //notifyButton.setOnClickListener(v -> {
            // Add logic to send notification
         //   Toast.makeText(getContext(), "Notification sent to " + userId, Toast.LENGTH_SHORT).show();
       // });

        //removeButton.setOnClickListener(v -> {
            // Remove participant logic
         //   Toast.makeText(getContext(), userId + " removed.", Toast.LENGTH_SHORT).show();
       // });

        return convertView;
    }
}
