package com.example.event_lottery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntrantUnitTestHelper {
    private final Map<String, Map<String, Object>> mockEvents = new HashMap<>();
    private final Map<String, Map<String, Object>> mockUsers = new HashMap<>();
    private final Map<String, Boolean> userNotificationPreferences = new HashMap<>();
    private final Map<String, String> eventInvitations = new HashMap<>();
    private final List<String> notificationLog = new ArrayList<>();

    public EntrantUnitTestHelper() {
        initializeMockData();
    }

    // initializing mock data for events and users
    private void initializeMockData() {
        // Mock Event 1
        Map<String, Object> event1 = new HashMap<>();
        event1.put("eventName", "Tech Conference 2024");
        event1.put("waitingList", new ArrayList<String>());
        event1.put("requiresGeolocation", true);
        event1.put("declinedSlots", new ArrayList<String>());
        mockEvents.put("event1", event1);

        // Mock Event 2
        Map<String, Object> event2 = new HashMap<>();
        event2.put("eventName", "Music Festival");
        event2.put("waitingList", new ArrayList<String>());
        event2.put("requiresGeolocation", false);
        event2.put("declinedSlots", new ArrayList<String>());
        mockEvents.put("event2", event2);

        // Mock User
        Map<String, Object> user1 = new HashMap<>();
        user1.put("email", "entrant@example.com");
        user1.put("name", "John Doe");
        user1.put("phoneNumber", "1234567890");
        user1.put("profileImageUrl", null); // No profile image by default
        mockUsers.put("entrant@example.com", user1);

        // Default notification preference
        userNotificationPreferences.put("entrant@example.com", true); // Notifications enabled by default
    }

    // adding user to a waiting list
    public boolean addUserToWaitingList(String eventId, String userEmail) {
        validateInput(eventId, "Event ID");
        validateInput(userEmail, "User Email");

        if (!mockEvents.containsKey(eventId)) return false;
        List<String> waitingList = (List<String>) mockEvents.get(eventId).get("waitingList");
        if (waitingList.contains(userEmail)) return false; // Avoid duplicates
        waitingList.add(userEmail);
        return true;
    }

    // removing user from a waiting list
    public boolean removeUserFromWaitingList(String eventId, String userEmail) {
        validateInput(eventId, "Event ID");
        validateInput(userEmail, "User Email");

        if (!mockEvents.containsKey(eventId)) return false;
        List<String> waitingList = (List<String>) mockEvents.get(eventId).get("waitingList");
        return waitingList.remove(userEmail);
    }

    // getting event details
    public Map<String, Object> getEventDetails(String eventId) {
        validateInput(eventId, "Event ID");
        return mockEvents.get(eventId);
    }

    // getting user profile
    public Map<String, Object> getUserProfile(String userEmail) {
        validateInput(userEmail, "User Email");
        return mockUsers.get(userEmail);
    }

    // updating user profile
    public boolean updateUserProfile(String userEmail, Map<String, Object> updates) {
        validateInput(userEmail, "User Email");

        if (!mockUsers.containsKey(userEmail)) return false;
        mockUsers.get(userEmail).putAll(updates);
        return true;
    }

    // (TODO) deterministic profile picture URL based on user name
    public String generateProfilePicture(String userEmail) {
        validateInput(userEmail, "User Email");

        if (!mockUsers.containsKey(userEmail)) return null;
        String name = (String) mockUsers.get(userEmail).get("name");
        return "http://example.com/avatar/" + name.hashCode() + ".png";
    }

    // checking if notifications are enabled for a user
    public boolean isNotificationEnabled(String userEmail) {
        validateInput(userEmail, "User Email");

        if (!mockUsers.containsKey(userEmail)) return false;
        return userNotificationPreferences.getOrDefault(userEmail, true);
    }

    // setting notification preference for a user
    public void setNotificationPreference(String userEmail, boolean preference) {
        validateInput(userEmail, "User Email");

        if (!mockUsers.containsKey(userEmail)) {
            throw new IllegalArgumentException("User does not exist: " + userEmail);
        }
        userNotificationPreferences.put(userEmail, preference);
    }

    // simulating sending a notification
    public void sendNotification(String userEmail, String message) {
        validateInput(userEmail, "User Email");

        if (isNotificationEnabled(userEmail)) {
            notificationLog.add("Notification to " + userEmail + ": " + message);
        }
    }

    // retrieving the notification log
    public List<String> getNotificationLog() {
        return new ArrayList<>(notificationLog);
    }

    // selecting the next user from declined slots
    public String getNextUserFromDeclined(String eventId) {
        validateInput(eventId, "Event ID");

        if (!mockEvents.containsKey(eventId)) return null;
        List<String> declinedSlots = (List<String>) mockEvents.get(eventId).get("declinedSlots");
        return declinedSlots.isEmpty() ? null : declinedSlots.remove(0);
    }

    // adding user to declined slots
    public void addToDeclinedSlots(String eventId, String userEmail) {
        validateInput(eventId, "Event ID");
        validateInput(userEmail, "User Email");

        if (!mockEvents.containsKey(eventId)) return;
        List<String> declinedSlots = (List<String>) mockEvents.get(eventId).get("declinedSlots");
        if (!declinedSlots.contains(userEmail)) {
            declinedSlots.add(userEmail);
        }
    }

    // sending an invitation to a user
    public boolean sendInvitation(String eventId, String userEmail) {
        validateInput(eventId, "Event ID");
        validateInput(userEmail, "User Email");

        if (!mockEvents.containsKey(eventId) || !mockUsers.containsKey(userEmail)) return false;
        eventInvitations.put(userEmail, eventId);
        return true;
    }

    // accept invitation
    public boolean acceptInvitation(String userEmail) {
        validateInput(userEmail, "User Email");

        if (!eventInvitations.containsKey(userEmail)) return false;
        eventInvitations.remove(userEmail);
        return true;
    }

    // decline invitation
    public boolean declineInvitation(String userEmail) {
        validateInput(userEmail, "User Email");

        if (!eventInvitations.containsKey(userEmail)) return false;
        String eventId = eventInvitations.remove(userEmail);
        addToDeclinedSlots(eventId, userEmail);
        return true;
    }

    // helper method to validate input
    private void validateInput(String input, String inputName) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException(inputName + " cannot be null or empty");
        }
    }
}