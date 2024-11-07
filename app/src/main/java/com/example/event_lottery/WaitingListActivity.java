package com.example.event_lottery;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WaitingListActivity extends AppCompatActivity {

    private TextView tvTotalParticipants;
    private EditText etSearchUser;
    private ListView lvWaitingList;
    private Button btnSendNotifications;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);

        // Initialize views
        tvTotalParticipants = findViewById(R.id.tv_total_participants);
        etSearchUser = findViewById(R.id.et_search_user);
        lvWaitingList = findViewById(R.id.lv_waiting_list);
        btnSendNotifications = findViewById(R.id.btn_send_notifications);
        ivBack = findViewById(R.id.iv_back);

        // Set back button functionality
        ivBack.setOnClickListener(v -> finish());

        // Set up the ListView (initially empty)
        // Future code here to load participants in the waiting list
    }
}
