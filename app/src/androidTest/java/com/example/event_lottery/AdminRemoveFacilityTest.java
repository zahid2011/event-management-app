package com.example.event_lottery;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AdminRemoveFacilityTest {
    private Map<String, AdminFacility> mockFacilities;

    @Before
    public void setUp() {
        // initializing the mock data before each test
        mockFacilities = new HashMap<>();
        initializeMockData();
    }

    // mock facility data
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
    public void testGetFacilityById() {
        AdminFacility facility = mockFacilities.get("1");
        assertNotNull("Facility with ID 1 should exist", facility);
        assertEquals("Facility name should be 'Community Hall'", "Community Hall", facility.getFacilityName());
    }

    @Test
    public void testDeleteFacilityById() {
        boolean isDeleted = mockFacilities.remove("1") != null;
        assertTrue("Facility with ID 1 should be deleted", isDeleted);
        assertNull("Facility with ID 1 should no longer exist", mockFacilities.get("1"));
    }

    @Test
    public void testAddFacility() {
        AdminFacility newFacility = new AdminFacility(
                "4", "Library", "A quiet place to study.",
                "987 Knowledge Lane, EduTown", "777888999", true);

        boolean isAdded = mockFacilities.putIfAbsent(newFacility.getFacilityId(), newFacility) == null;
        assertTrue("New facility should be added successfully", isAdded);
        assertEquals("Facility count should increase by 1", 4, mockFacilities.size());
    }

    @Test
    public void testFacilityCount() {
        int count = mockFacilities.size();
        assertEquals("Facility count should be 3 initially", 3, count);

        mockFacilities.remove("1");
        count = mockFacilities.size();
        assertEquals("Facility count should decrease by 1 after removal", 2, count);
    }
}
