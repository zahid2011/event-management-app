package com.example.event_lottery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EntrantAdapter extends RecyclerView.Adapter<EntrantAdapter.EntrantViewHolder> {
    private List<User> entrantList;

    public EntrantAdapter(List<User> entrantList) {
        this.entrantList = entrantList;
    }

    @NonNull
    @Override
    public EntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entrant_item, parent, false); // Ensure `entrant_item.xml` exists
        return new EntrantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrantViewHolder holder, int position) {
        User user = entrantList.get(position);
        holder.usernameTextView.setText(user.getUsername());

        // Example of button click handling
        holder.statusButton.setOnClickListener(v ->
                Toast.makeText(holder.itemView.getContext(), "Status: Chosen", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    static class EntrantViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        Button statusButton;

        EntrantViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            statusButton = itemView.findViewById(R.id.status_button);
        }
    }
}
