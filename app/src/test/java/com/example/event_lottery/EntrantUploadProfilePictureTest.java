package com.example.event_lottery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantUploadProfilePictureTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();
    }

    @Test
    public void testUploadProfilePicture() {
        String userEmail = "entrant@example.com";
        Map<String, Object> updates = new HashMap<>();
        updates.put("profileImageUrl", "http://example.com/profile.jpg");

        boolean updated = testHelper.updateUserProfile(userEmail, updates);
        assertTrue(updated, "Profile picture upload should be successful");

        Map<String, Object> userProfile = testHelper.getUserProfile(userEmail);
        assertEquals("http://example.com/profile.jpg", userProfile.get("profileImageUrl"), "Profile picture URL should match");
    }
}