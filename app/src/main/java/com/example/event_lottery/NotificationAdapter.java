package com.example.event_lottery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class NotificationAdapter extends BaseAdapter {

    private Context context;
    private List<NotificationItem> notificationList;
    private LayoutInflater inflater;

    public NotificationAdapter(Context context, List<NotificationItem> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // Return position if IDs are not available
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.notification_item, null);
            holder = new ViewHolder();
            holder.messageTextView = convertView.findViewById(R.id.notification_message);
            holder.statusTextView = convertView.findViewById(R.id.notification_status);
            holder.detailsButton = convertView.findViewById(R.id.notification_details_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NotificationItem notificationItem = notificationList.get(position);

        holder.messageTextView.setText(notificationItem.getMessage());
        holder.statusTextView.setText("Status: " + notificationItem.getStatus());

        holder.detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationDetailsActivity.class);
            intent.putExtra("notificationId", notificationItem.getNotificationId());
            intent.putExtra("email", notificationItem.getEmail());
            intent.putExtra("message", notificationItem.getMessage());
            intent.putExtra("status", notificationItem.getStatus());
            intent.putExtra("eventName", notificationItem.getEventName());
            context.startActivity(intent);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView messageTextView;
        TextView statusTextView;
        Button detailsButton;
    }
}
