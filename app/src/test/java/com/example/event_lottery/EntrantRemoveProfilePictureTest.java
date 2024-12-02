package com.example.event_lottery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantRemoveProfilePictureTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();
    }

    @Test
    public void testRemoveProfilePicture() {
        String userEmail = "entrant@example.com";
        Map<String, Object> updates = new HashMap<>();
        updates.put("profileImageUrl", null);

        boolean updated = testHelper.updateUserProfile(userEmail, updates);
        assertTrue(updated, "Profile picture removal should be successful");

        Map<String, Object> userProfile = testHelper.getUserProfile(userEmail);
        assertNull(userProfile.get("profileImageUrl"), "Profile picture URL should be null after removal");
    }
}