package com.example.event_lottery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantProvidePersonalInformationTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();
    }

    @Test
    public void testProvidePersonalInformation() {
        String userEmail = "entrant@example.com";
        Map<String, Object> userProfile = testHelper.getUserProfile(userEmail);

        assertNotNull(userProfile, "User profile should not be null");
        assertEquals("John Doe", userProfile.get("name"), "User name should match");
        assertEquals("1234567890", userProfile.get("phoneNumber"), "User phone number should match");
    }
}