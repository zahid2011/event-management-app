package com.example.event_lottery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends MainActivity {
    private EditText emailEditText, passwordEditText;
    private Button signInButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        backButton = findViewById(R.id.backButton2);

        // setting up the back button functionality
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // closes the current activity and returns to the previous one
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String registeredEmail = prefs.getString("registeredEmail", "");
                String registeredPassword = prefs.getString("registeredPassword", "");

                if (email.equals(registeredEmail) && password.equals(registeredPassword)) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    //DashboardActivity
                    Intent dashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(dashboardIntent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
