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
    private boolean testMode;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_managament);
        db = FirebaseFirestore.getInstance();

        userListView = findViewById(R.id.list_view);
        users = new ArrayList<>();
        adminUserAdapter = new AdminUserAdapter(this, users);
        userListView.setAdapter(adminUserAdapter);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // checking if test mode is enabled
        testMode = getIntent().getBooleanExtra("TEST_MODE", false);
        if (testMode) {
            initializeMockData(); // Load mock data
        } else {
            loadUsersFromFirebase(); // Load from Firebase
        }
    }

    /**
     * Called when the activity becomes visible to the user.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // reloading the list from Firebase whenever the activity is resumed
        if (!testMode) {
            loadUsersFromFirebase();
        }
    }

    /**
     * Initializes mock data for testing purposes.
     */
    private void initializeMockData() {
        users.clear(); // Ensure no other data exists
        users.add(new User("1", "mockuser@example.com", "mockuser", "John", "Doe", "password123", "User"));
        adminUserAdapter.notifyDataSetChanged();
    }

    /**
     * Loads the list of users from Firebase Firestore and updates the adapter.
     */
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