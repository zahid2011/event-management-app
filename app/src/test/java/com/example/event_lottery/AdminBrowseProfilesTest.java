package com.example.event_lottery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdminBrowseProfilesTest {
    private AdminTestHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    void testBrowseAllProfiles() {
        List<User> profiles = testHelper.getAllProfiles();
        assertNotNull(profiles, "Profile list should not be null");
        assertEquals(2, profiles.size(), "Profile list should contain 2 profiles initially");
    }

    @Test
    void testBrowseSpecificProfile() {
        User user = testHelper.getProfileById("1");
        assertNotNull(user, "Profile with ID 1 should exist");
        assertEquals("john@example.com", user.getEmail(), "User email should be 'john@example.com'");
        assertEquals("Admin", user.getRole(), "User role should be 'Admin'");
    }
}
