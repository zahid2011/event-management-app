package com.example.event_lottery;

import com.google.firebase.Timestamp;

public class Event {
    private String eventName;
    private Timestamp eventDateTime;
    private String capacity;
    private String price;

    // Default constructor (required for Firebase or other serialization libraries)
    public Event() {}

    // Constructor with parameters
    public Event(String eventName, Timestamp eventDateTime, String capacity, String price) {
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.capacity = capacity;
        this.price = price;
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
}