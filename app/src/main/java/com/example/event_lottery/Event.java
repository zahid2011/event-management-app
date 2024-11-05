package com.example.event_lottery;

import java.util.Date;

public class Event {
    private String eventName;
    private Date eventDateTime;
    private String capacity;
    private String price;
    private String description;

    public Event() {} // Default constructor needed for Firestore

    public Event(String eventName, Date eventDateTime, String capacity, String price, String description) {
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.capacity = capacity;
        this.price = price;
    }

    public String getEventName() {
        return eventName;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription(){
        return description;
    }
}
