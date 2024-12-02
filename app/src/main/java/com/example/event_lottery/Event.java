package com.example.event_lottery;

import com.google.firebase.Timestamp;

public class Event {
    private String eventId;
    private String eventName;
    private Timestamp eventDateTime;
    private String capacity;
    private String price;
    private String description;
    private String qrContent;
    private String qrHash;
    private boolean geoLocationEnabled;

    // constructor 
    public Event() {}


    public Event(String eventId, String eventName, Timestamp eventDateTime, String capacity, String price,
                 String description, boolean geoLocationEnabled, String qrContent, String qrHash) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.capacity = capacity;
        this.price = price;
        this.description = description;
        this.geoLocationEnabled = geoLocationEnabled;
        this.qrContent = qrContent;
        this.qrHash = qrHash;
    }
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    // Getters and Setters
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Timestamp getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Timestamp eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQrContent() { return qrContent; }

    public void setQrContent(String qrContent) { this.qrContent = qrContent; }

    public String getQrHash() { return qrHash; }

    public void setQrHash(String qrHash) { this.qrHash = qrHash; }

    public boolean isGeoLocationEnabled() { return geoLocationEnabled; }

    public void setGeoLocationEnabled(boolean geoLocation) { this.geoLocationEnabled= geoLocation; }
}
