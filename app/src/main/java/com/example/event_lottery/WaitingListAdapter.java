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

/**
 * Custom adapter for displaying a waiting list of participants.
 * <p>
 * Each list item shows a participant's username and a selection circle
 * indicating whether the participant has been selected.
 * </p>
 */

public class WaitingListAdapter extends ArrayAdapter<String> {

    private final List<String> participantList;
    private final Set<Integer> selectedUsers;

    /**
     * Constructor for the WaitingListAdapter.
     *
     * @param context         The current context.
     * @param participantList The list of participant names.
     * @param selectedUsers   The set of indices representing selected users.
     */

    public WaitingListAdapter(Context context, List<String> participantList, Set<Integer> selectedUsers) {
        super(context, 0, participantList);
        this.participantList = participantList;
        this.selectedUsers = selectedUsers;
    }

    /**
     * Provides a view for an adapter view (ListView, GridView, etc.).
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent view that this view will be attached to.
     * @return The View for the specified position in the adapter.
     */

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
