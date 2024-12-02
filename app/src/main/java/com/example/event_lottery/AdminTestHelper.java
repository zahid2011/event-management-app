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

    public AdminTestHelper() {
        mockEvents = new HashMap<>();
        mockProfiles = new HashMap<>();
        mockQRCodes = new HashMap<>();
        mockImages = new ArrayList<>();
        initializeMockData();
    }

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
    public Event getEventById(String eventId) {
        return mockEvents.get(eventId);
    }

    public boolean deleteEventById(String eventId) {
        return mockEvents.remove(eventId) != null;
    }

    public boolean addEvent(Event event) {
        if (!mockEvents.containsKey(event.getEventId())) {
            mockEvents.put(event.getEventId(), event);
            return true;
        }
        return false;
    }

    public int getEventCount() {
        return mockEvents.size();
    }

    // Profile Management
    public User getProfileById(String userId) {
        return mockProfiles.get(userId);
    }

    public boolean deleteProfileById(String userId) {
        return mockProfiles.remove(userId) != null;
    }

    public boolean addProfile(User user) {
        if (!mockProfiles.containsKey(user.getId())) {
            mockProfiles.put(user.getId(), user);
            return true;
        }
        return false;
    }

    public int getProfileCount() {
        return mockProfiles.size();
    }

    // QR Code Management
    public String getQRCodeByEventId(String eventId) {
        return mockQRCodes.get(eventId);
    }

    public boolean deleteQRCodeByEventId(String eventId) {
        return mockQRCodes.remove(eventId) != null;
    }

    public boolean addQRCode(String eventId, String qrContent) {
        if (!mockQRCodes.containsKey(eventId)) {
            mockQRCodes.put(eventId, qrContent);
            return true;
        }
        return false;
    }

    public int getQRCodeCount() {
        return mockQRCodes.size();
    }

    // Image Management
    public boolean deleteImage(String imageId) {
        return mockImages.remove(imageId);
    }

    public List<String> getAllImages() {
        return new ArrayList<>(mockImages);
    }

    public boolean addImage(String imageId) {
        if (!mockImages.contains(imageId)) {
            mockImages.add(imageId);
            return true;
        }
        return false;
    }
    public List<Event> getAllEvents() {
        return new ArrayList<>(mockEvents.values());
    }
    public List<User> getAllProfiles() {
        return new ArrayList<>(mockProfiles.values());
    }

}