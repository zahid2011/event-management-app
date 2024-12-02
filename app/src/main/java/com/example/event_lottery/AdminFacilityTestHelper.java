package com.example.event_lottery;
import java.util.HashMap;
import java.util.Map;

public class AdminFacilityTestHelper {
    private Map<String, AdminFacility> mockFacilities;

    /**
     * Constructor to initialize mock facility data.
     */
    public AdminFacilityTestHelper() {
        mockFacilities = new HashMap<>();
        initializeMockData();
    }

    /**
     * Initializes the mock facility data with some predefined facilities.
     */
    private void initializeMockData() {
        mockFacilities.put("1", new AdminFacility(
                "1", "Community Hall", "A large hall for events.",
                "123 Main Street, Cityville", "123456789", true));

        mockFacilities.put("2", new AdminFacility(
                "2", "Sports Complex", "An indoor and outdoor sports facility.",
                "456 Sports Ave, Townsville", "987654321", false));

        mockFacilities.put("3", new AdminFacility(
                "3", "Conference Center", "Venue for corporate meetings.",
                "789 Business Blvd, Metropolis", "555123456", true));
    }

    /**
     * Fetches a facility by its ID.
     *
     * @param facilityId The ID of the facility to fetch.
     * @return The AdminFacility object corresponding to the given ID, or null if not found.
     */
    public AdminFacility getFacilityById(String facilityId) {
        return mockFacilities.get(facilityId);
    }

    /**
     * Adds a new facility to the mock data.
     *
     * @param facility The facility to add.
     * @return true if the facility was added successfully, false if a facility with the same ID already exists.
     */
    public boolean addFacility(AdminFacility facility) {
        if (mockFacilities.containsKey(facility.getFacilityId())) {
            return false; // means facility with the same ID already exists
        }
        mockFacilities.put(facility.getFacilityId(), facility);
        return true;
    }

    /**
     * Deletes a facility by its ID.
     *
     * @param facilityId The ID of the facility to delete.
     * @return true if the facility was deleted successfully, false if the facility was not found.
     */
    public boolean deleteFacilityById(String facilityId) {
        return mockFacilities.remove(facilityId) != null;
    }

    /**
     * Gets the total count of facilities.
     *
     * @return The total number of facilities in the mock data.
     */
    public int getFacilityCount() {
        return mockFacilities.size();
    }
}
