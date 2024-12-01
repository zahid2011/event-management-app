package com.example.event_lottery;

import java.util.HashMap;
import java.util.Map;
public class AdminFacilityTestHelper {
    private Map<String, AdminFacility> mockFacilities;

    // Constructor to initialize mock data
    public AdminFacilityTestHelper() {
        mockFacilities = new HashMap<>();
        initializeMockData();
    }

    // Initializing the mock facility data
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

    // fetching a facility by ID
    public AdminFacility getFacilityById(String facilityId) {
        return mockFacilities.get(facilityId);
    }

    // adding a new facility
    public boolean addFacility(AdminFacility facility) {
        if (mockFacilities.containsKey(facility.getFacilityId())) {
            return false; // means facility with the same ID already exists
        }
        mockFacilities.put(facility.getFacilityId(), facility);
        return true;
    }

    // deleting a facility by ID
    public boolean deleteFacilityById(String facilityId) {
        return mockFacilities.remove(facilityId) != null;
    }

    // getting the total count of facilities
    public int getFacilityCount() {
        return mockFacilities.size();
    }
}
