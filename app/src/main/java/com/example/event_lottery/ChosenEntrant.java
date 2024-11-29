package com.example.event_lottery;

public class ChosenEntrant {
    private String email;
    private String location;

    public ChosenEntrant(String email, String location) {
        this.email = email;
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }
}
