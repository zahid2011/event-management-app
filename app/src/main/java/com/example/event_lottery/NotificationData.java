package com.example.event_lottery;

public class NotificationData {
    private String email;
    private String message;
    private int status;
    private String eventName; // New field added

    // Default constructor (required for Firestore)
    public NotificationData() {
    }

    // Constructor to initialize fields
    public NotificationData(String email, String message, int status, String eventName) {
        this.email = email;
        this.message = message;
        this.status = status;
        this.eventName = eventName;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEventName() { // New getter
        return eventName;
    }

    public void setEventName(String eventName) { // New setter
        this.eventName = eventName;
    }
}
