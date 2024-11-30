package com.example.event_lottery;

public class NotificationItem {
    private String email;
    private String message;
    private int status;

    public NotificationItem() {
        // Default constructor
    }

    public NotificationItem(String email, String message, int status) {
        this.email = email;
        this.message = message;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
