package com.example.event_lottery;


import java.util.Date;


public class Events {
    private String eventName;
    private Date eventDateTime;
    private String capacity;
    private String price;
    private String description;


    public Events() {} // Default constructor needed for Firestore


    public Events(String eventName, Date eventDateTime, String capacity, String price, String description) {
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.capacity = capacity;
        this.price = price;
        this.description = description;
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
