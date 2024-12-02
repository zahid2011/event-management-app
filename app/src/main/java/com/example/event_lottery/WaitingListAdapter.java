package com.example.event_lottery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Set;

public class WaitingListAdapter extends ArrayAdapter<String> {

    private final List<String> participantList;
    private final Set<Integer> selectedUsers;

    public WaitingListAdapter(Context context, List<String> participantList, Set<Integer> selectedUsers) {
        super(context, 0, participantList);
        this.participantList = participantList;
        this.selectedUsers = selectedUsers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.waiting_list_item, parent, false);
        }

        TextView tvUsername = convertView.findViewById(R.id.tv_username);
        ImageView ivSelectionCircle = convertView.findViewById(R.id.iv_selection_circle);

        String name = participantList.get(position);
        tvUsername.setText(name);

        // Update selection circle state
        if (selectedUsers.contains(position)) {
            ivSelectionCircle.setImageResource(R.drawable.ic_check_circle); // Tick circle
        } else {
            ivSelectionCircle.setImageResource(R.drawable.ic_circle_outline); // Empty circle
        }

        // Handle row click to toggle selection
        convertView.setOnClickListener(v -> {
            if (selectedUsers.contains(position)) {
                selectedUsers.remove(position);
            } else {
                selectedUsers.add(position);
            }
            notifyDataSetChanged();
        });

        return convertView;
    }
}
