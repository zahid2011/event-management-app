package com.example.event_lottery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;


import com.example.event_lottery.R;

import java.util.List;


public class WaitingListAdapter extends ArrayAdapter<String> {


    public WaitingListAdapter(Context context, List<String> participantList) {
        super(context, 0, participantList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.participant_list_item, parent, false);
        }


        TextView participantName = convertView.findViewById(R.id.tv_participant_name);


        String name = getItem(position);
        participantName.setText(name);


        return convertView;
    }
}
