package com.example.event_lottery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WaitingListActivity extends AppCompatActivity {

    private ListView lvWaitingList;
    private List<WaitingListUser> waitingListUsers;
    private Set<Integer> selectedUsers; // Track selected user positions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);

        lvWaitingList = findViewById(R.id.lv_waiting_list);
        selectedUsers = new HashSet<>();

        // Dynamically populate user list (replace this with actual data fetching logic)
        waitingListUsers = new ArrayList<>();
        for (int i = 1; i <= 20; i++) { // Example: 20 users
            waitingListUsers.add(new WaitingListUser("User " + i, R.drawable.ic_image_placeholder));
        }

        // Set up the adapter
        WaitingListAdapter adapter = new WaitingListAdapter(this, waitingListUsers);
        lvWaitingList.setAdapter(adapter);
    }

    // Custom Adapter for Waiting List
    class WaitingListAdapter extends ArrayAdapter<WaitingListUser> {

        private Context context;
        private List<WaitingListUser> users;

        public WaitingListAdapter(Context context, List<WaitingListUser> users) {
            super(context, 0, users);
            this.context = context;
            this.users = users;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Inflate custom layout
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.waiting_list_item, parent, false);
            }

            // Get the current user
            WaitingListUser user = users.get(position);

            // Bind views
            ImageView ivUserIcon = convertView.findViewById(R.id.iv_user_icon);
            TextView tvUsername = convertView.findViewById(R.id.tv_username);
            Button btnDetails = convertView.findViewById(R.id.btn_details);

            ivUserIcon.setImageResource(user.getIcon());
            tvUsername.setText(user.getName());

            // Highlight if the user is selected
            if (selectedUsers.contains(position)) {
                convertView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            }

            // Handle row click to select/unselect user
            convertView.setOnClickListener(v -> {
                if (selectedUsers.contains(position)) {
                    selectedUsers.remove(position);
                    Toast.makeText(context, "Unselected: " + user.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    selectedUsers.add(position);
                    Toast.makeText(context, "Selected: " + user.getName(), Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged(); // Refresh the ListView to update selection
            });

            // Set up "Details" button (non-functional for now)
            btnDetails.setOnClickListener(v -> {
                // Placeholder: Show a message when clicked
                Toast.makeText(context, "Details for " + user.getName(), Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }
    }

    // Model Class for Users
    class WaitingListUser {
        private String name;
        private int icon;

        public WaitingListUser(String name, int icon) {
            this.name = name;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public int getIcon() {
            return icon;
        }
    }
}
