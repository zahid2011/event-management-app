package com.example.event_lottery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChosenEntrantAdapter extends RecyclerView.Adapter<ChosenEntrantAdapter.ViewHolder> {

    private final List<ChosenEntrant> entrantList;

    public ChosenEntrantAdapter(List<ChosenEntrant> entrantList) {
        this.entrantList = entrantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chosen_entrant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChosenEntrant entrant = entrantList.get(position);
        holder.emailTextView.setText(entrant.getEmail());
        holder.locationTextView.setText(entrant.getLocation());
    }

    @Override
    public int getItemCount() {
        return entrantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView, locationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.texty_email);
            locationTextView = itemView.findViewById(R.id.text_location);
        }
    }
}
