package com.example.event_lottery;

public class Event {
    private String eventName;
    private String date;
    private int capacity;
    private double price;
    private String posterUrl;

    // Default constructor required for Firestore
    public Event() {}

    // Constructor with parameters
    public Event(String eventName, String date, int capacity, double price, String posterUrl) {
        this.eventName = eventName;
        this.date = date;
        this.capacity = capacity;
        this.price = price;
        this.posterUrl = posterUrl;
    }

    // Getters and setters
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}
