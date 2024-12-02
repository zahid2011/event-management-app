package com.example.event_lottery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

public class WaitingListAdapter extends ArrayAdapter<WaitingListActivity.WaitingListUser> {

    private final Context context;
    private final List<WaitingListActivity.WaitingListUser> users;

    public WaitingListAdapter(Context context, List<WaitingListActivity.WaitingListUser> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.waiting_list_item, parent, false);
        }

        // Get the current user
        WaitingListActivity.WaitingListUser user = getItem(position);

        // Bind views
        TextView tvUsername = convertView.findViewById(R.id.tv_username);

        tvUsername.setText(user.getName());

        return convertView;
    }
}
