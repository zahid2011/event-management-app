package com.example.event_lottery;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
public class AdminBrowseProfilesTest {
    private AdminTestHelper testHelper;

    @Before
    public void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    public void testBrowseAllProfiles() {
        List<User> profiles = testHelper.getAllProfiles();
        assertNotNull("Profile list should not be null", profiles);
        assertEquals("Profile list should contain 2 profiles initially", 2, profiles.size());
    }

    @Test
    public void testBrowseSpecificProfile() {
        User user = testHelper.getProfileById("1");
        assertNotNull("Profile with ID 1 should exist", user);
        assertEquals("User email should be 'john@example.com'", "john@example.com", user.getEmail());
        assertEquals("User role should be 'Admin'", "Admin", user.getRole());
    }
}
