package com.example.event_lottery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminTestHelper {
    private Map<String, Event> mockEvents;
    private Map<String, User> mockProfiles;
    private Map<String, String> mockQRCodes;
    private List<String> mockImages;

    /**
     * Constructor to initialize mock data for events, profiles, QR codes, and images.
     */
    public AdminTestHelper() {
        mockEvents = new HashMap<>();
        mockProfiles = new HashMap<>();
        mockQRCodes = new HashMap<>();
        mockImages = new ArrayList<>();
        initializeMockData();
    }

    /**
     * Initializes mock data for testing purposes.
     */
    private void initializeMockData() {
        mockEvents.put("1", new Event(
                "1", "Event A", null, "100", "20.0",
                "Description A", true, "QRContentA", "QRHashA"));
        mockEvents.put("2", new Event(
                "2", "Event B", null, "200", "30.0",
                "Description B", false, "QRContentB", "QRHashB"));

        // initializing mock profiles
        mockProfiles.put("1", new User(
                "1", "john@example.com", "john123", "John", "Doe", "password123", "Admin"));
        mockProfiles.put("2", new User(
                "2", "jane@example.com", "jane456", "Jane", "Smith", "password456", "User"));

        // initializing mock QR codes
        mockQRCodes.put("1", "QRContentA");
        mockQRCodes.put("2", "QRContentB");

        // initializing mock images
        mockImages.add("image1");
        mockImages.add("image2");
    }

    // Event Management

    /**
     * Retrieves an event by its ID.
     *
     * @param eventId The ID of the event to retrieve.
     * @return The Event object corresponding to the given ID, or null if not found.
     */
    public Event getEventById(String eventId) {
        return mockEvents.get(eventId);
    }

    /**
     * Deletes an event by its ID.
     *
     * @param eventId The ID of the event to delete.
     * @return true if the event was successfully deleted, false otherwise.
     */
    public boolean deleteEventById(String eventId) {
        return mockEvents.remove(eventId) != null;
    }

    /**
     * Adds a new event to the mock data.
     *
     * @param event The event to add.
     * @return true if the event was successfully added, false if an event with the same ID already exists.
     */
    public boolean addEvent(Event event) {
        if (!mockEvents.containsKey(event.getEventId())) {
            mockEvents.put(event.getEventId(), event);
            return true;
        }
        return false;
    }

    /**
     * Gets the total number of events.
     *
     * @return The count of events in the mock data.
     */
    public int getEventCount() {
        return mockEvents.size();
    }

    // Profile Management

    /**
     * Retrieves a user profile by its ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The User object corresponding to the given ID, or null if not found.
     */
    public User getProfileById(String userId) {
        return mockProfiles.get(userId);
    }

    /**
     * Deletes a user profile by its ID.
     *
     * @param userId The ID of the user to delete.
     * @return true if the user profile was successfully deleted, false otherwise.
     */
    public boolean deleteProfileById(String userId) {
        return mockProfiles.remove(userId) != null;
    }

    /**
     * Adds a new user profile to the mock data.
     *
     * @param user The user profile to add.
     * @return true if the profile was successfully added, false if a profile with the same ID already exists.
     */
    public boolean addProfile(User user) {
        if (!mockProfiles.containsKey(user.getId())) {
            mockProfiles.put(user.getId(), user);
            return true;
        }
        return false;
    }

    /**
     * Gets the total number of user profiles.
     *
     * @return The count of user profiles in the mock data.
     */
    public int getProfileCount() {
        return mockProfiles.size();
    }

    // QR Code Management

    /**
     * Retrieves a QR code by its associated event ID.
     *
     * @param eventId The ID of the event for which to retrieve the QR code.
     * @return The QR code content as a String, or null if not found.
     */
    public String getQRCodeByEventId(String eventId) {
        return mockQRCodes.get(eventId);
    }

    /**
     * Deletes a QR code by its associated event ID.
     *
     * @param eventId The ID of the event for which to delete the QR code.
     * @return true if the QR code was successfully deleted, false otherwise.
     */
    public boolean deleteQRCodeByEventId(String eventId) {
        return mockQRCodes.remove(eventId) != null;
    }

    /**
     * Adds a new QR code to the mock data.
     *
     * @param eventId The ID of the event for which to add the QR code.
     * @param qrContent The content of the QR code.
     * @return true if the QR code was successfully added, false if a QR code for the same event ID already exists.
     */
    public boolean addQRCode(String eventId, String qrContent) {
        if (!mockQRCodes.containsKey(eventId)) {
            mockQRCodes.put(eventId, qrContent);
            return true;
        }
        return false;
    }

    /**
     * Gets the total number of QR codes.
     *
     * @return The count of QR codes in the mock data.
     */
    public int getQRCodeCount() {
        return mockQRCodes.size();
    }

    // Image Management

    /**
     * Deletes an image by its ID.
     *
     * @param imageId The ID of the image to delete.
     * @return true if the image was successfully deleted, false otherwise.
     */
    public boolean deleteImage(String imageId) {
        return mockImages.remove(imageId);
    }

    /**
     * Retrieves all images from the mock data.
     *
     * @return A list of all image IDs.
     */
    public List<String> getAllImages() {
        return new ArrayList<>(mockImages);
    }

    /**
     * Adds a new image to the mock data.
     *
     * @param imageId The ID of the image to add.
     * @return true if the image was successfully added, false if the image already exists.
     */
    public boolean addImage(String imageId) {
        if (!mockImages.contains(imageId)) {
            mockImages.add(imageId);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all events from the mock data.
     *
     * @return A list of all Event objects.
     */
    public List<Event> getAllEvents() {
        return new ArrayList<>(mockEvents.values());
    }

    /**
     * Retrieves all user profiles from the mock data.
     *
     * @return A list of all User objects.
     */
    public List<User> getAllProfiles() {
        return new ArrayList<>(mockProfiles.values());
    }
}
