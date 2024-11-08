package com.example.event_lottery;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminProfileManagementActivity extends AppCompatActivity {
    private ListView userListView;
    private AdminUserAdapter adminUserAdapter;
    private FirebaseFirestore db;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_managament);

        userListView = findViewById(R.id.list_view);
        db = FirebaseFirestore.getInstance();
        users = new ArrayList<>();

        // Set up the adapter with an empty list initially
        adminUserAdapter = new AdminUserAdapter(this, users);
        userListView.setAdapter(adminUserAdapter);

        // Load users from Firebase
        loadUsersFromFirebase();

        // Back button functionality
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void loadUsersFromFirebase() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        users.clear();
                        QuerySnapshot result = task.getResult();
                        for (QueryDocumentSnapshot document : result) {
                            User user = document.toObject(User.class);
                            user.setId(document.getId());
                            users.add(user);
                        }
                        adminUserAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminProfileManagementActivity.this, "Error loading users", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
