package com.example.event_lottery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AdminRemoveFacilityTest {
    private Map<String, AdminFacility> mockFacilities;

    @BeforeEach
    void setUp() {
        // Initializing the mock data before each test
        mockFacilities = new HashMap<>();
        initializeMockData();
    }

    // Mock facility data
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

    @Test
    void testGetFacilityById() {
        AdminFacility facility = mockFacilities.get("1");
        assertNotNull(facility, "Facility with ID 1 should exist");
        assertEquals("Community Hall", facility.getFacilityName(), "Facility name should be 'Community Hall'");
    }

    @Test
    void testDeleteFacilityById() {
        boolean isDeleted = mockFacilities.remove("1") != null;
        assertTrue(isDeleted, "Facility with ID 1 should be deleted");
        assertNull(mockFacilities.get("1"), "Facility with ID 1 should no longer exist");
    }

    @Test
    void testAddFacility() {
        AdminFacility newFacility = new AdminFacility(
                "4", "Library", "A quiet place to study.",
                "987 Knowledge Lane, EduTown", "777888999", true);

        boolean isAdded = mockFacilities.putIfAbsent(newFacility.getFacilityId(), newFacility) == null;
        assertTrue(isAdded, "New facility should be added successfully");
        assertEquals(4, mockFacilities.size(), "Facility count should increase by 1");
    }

    @Test
    void testFacilityCount() {
        int count = mockFacilities.size();
        assertEquals(3, count, "Facility count should be 3 initially");

        mockFacilities.remove("1");
        count = mockFacilities.size();
        assertEquals(2, count, "Facility count should decrease by 1 after removal");
    }
}
