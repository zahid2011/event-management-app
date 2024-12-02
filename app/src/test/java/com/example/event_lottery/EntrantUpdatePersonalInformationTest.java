package com.example.event_lottery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantUpdatePersonalInformationTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();
    }

    @Test
    public void testUpdatePersonalInformationSuccess() {
        String userEmail = "entrant@example.com";
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Jane Doe");
        updates.put("phoneNumber", "9876543210");

        boolean updated = testHelper.updateUserProfile(userEmail, updates);
        assertTrue(updated, "Profile update should be successful");

        Map<String, Object> updatedProfile = testHelper.getUserProfile(userEmail);
        assertEquals("Jane Doe", updatedProfile.get("name"), "Updated name should match");
        assertEquals("9876543210", updatedProfile.get("phoneNumber"), "Updated phone number should match");
    }

    @Test
    public void testUpdatePersonalInformationFailure() {
        String userEmail = "nonExistentUser@example.com";
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Jane Doe");

        boolean updated = testHelper.updateUserProfile(userEmail, updates);
        assertFalse(updated, "Profile update should fail for non-existent user");
    }
}