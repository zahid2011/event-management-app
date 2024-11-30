package com.example.event_lottery;

public class NotificationItem {
    private String notificationId;
    private String email;
    private String message;
    private int status;
    private String eventName;

    public NotificationItem() {
        // Default constructor
    }

    public NotificationItem(String notificationId, String email, String message, int status, String eventName) {
        this.notificationId = notificationId;
        this.email = email;
        this.message = message;
        this.status = status;
        this.eventName = eventName;
    }

    public String getNotificationId() {
        return notificationId;
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

    public String getEventName() {
        return eventName;
    }
}
