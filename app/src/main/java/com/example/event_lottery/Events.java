package com.example.event_lottery;


import java.util.Date;

/**
 * Represents an event with details such as name, date, capacity, price, description, and image URL.
 * This class is used to manage event data and supports interaction with Firestore.
 */


public class Events {
    private String eventName;
    private Date eventDateTime;
    private String capacity;
    private String price;
    private String description;
    private String imageUrl;


    /**
     * Default constructor needed for Firestore.
     */

    public Events() {} // Default constructor needed for Firestore

    /**
     * Constructs an Events object with the specified details.
     *
     * @param eventName      the name of the event
     * @param eventDateTime  the date and time of the event
     * @param capacity       the capacity of attendees
     * @param price          the price for the event
     * @param description    the description of the event
     */


    public Events(String eventName, Date eventDateTime, String capacity, String price, String description) {
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.capacity = capacity;
        this.price = price;
        this.description = description;
    }

    /**
     * Gets the name of the event.
     *
     * @return the event name
     */

    public String getEventName() {
        return eventName;
    }

    /**
     * Gets the date and time of the event.
     *
     * @return the event date and time
     */


    public Date getEventDateTime() {
        return eventDateTime;
    }

    /**
     * Gets the capacity of attendees for the event.
     *
     * @return the event capacity
     */


    public String getCapacity() {
        return capacity;
    }

    /**
     * Gets the price of the event.
     *
     * @return the event price
     */


    public String getPrice() {
        return price;
    }

    /**
     * Gets the description of the event.
     *
     * @return the event description
     */


    public String getDescription(){
        return description;
    }

    /**
     * Gets the image URL associated with the event.
     *
     * @return the image URL
     */

    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image URL for the event.
     *
     * @param imageUrl the image URL to set
     */

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}