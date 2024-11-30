package com.example.event_lottery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
public class FacilityListAdapter extends ArrayAdapter<AdminFacility> {
    private final Context context;
    private final List<AdminFacility> facilities;

    public FacilityListAdapter(@NonNull Context context, @NonNull List<AdminFacility> facilities) {
        super(context, 0, facilities);
        this.context = context;
        this.facilities = facilities;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_facility_item, parent, false);
        }

        // getting the facility at the current position
        AdminFacility facility = facilities.get(position);

        // referencing UI components
        TextView facilityNameTextView = convertView.findViewById(R.id.facility_name);
        Button detailsButton = convertView.findViewById(R.id.details_button);

        facilityNameTextView.setText(facility.getFacilityName());

        // handling the Details button click
        detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminFacilityDetailsActivity.class);
            intent.putExtra("facilityId", facility.getFacilityId());
            context.startActivity(intent);
        });

        return convertView;
    }
}
