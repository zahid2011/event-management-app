package com.example.event_lottery;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)

class OrganizerCreateEventActivityTest {

    private OrganizerEventTestHelper eventHelper;

    @BeforeEach
    void setUp() {
        eventHelper = new OrganizerEventTestHelper();
    }

    @Test
    void testCreateEventData() {
        // Arrange
        String eventName = "TestEvent";
        Date eventDateTime = new Date();
        String capacity = "50";
        String price = "10.99";
        String description = "Test Description";
        boolean geolocationEnabled = true;
        String imagePath = "path/to/image.jpg";

        // Act
        Map<String, Object> eventData = eventHelper.createEventData(eventName, eventDateTime, capacity, price, description, geolocationEnabled, imagePath);

        // Assert
        assertNotNull(eventData);
        assertEquals(eventName, eventData.get("eventName"));
        assertEquals(eventDateTime, eventData.get("eventDateTime"));
        assertEquals(capacity, eventData.get("capacity"));
        assertEquals(price, eventData.get("price"));
        assertEquals(description, eventData.get("description"));
        assertEquals(geolocationEnabled, eventData.get("geolocationEnabled"));
        assertEquals(imagePath, eventData.get("imagePath"));
    }

    @Test
    void testValidateEventData_ValidData() {
        // Arrange
        String eventName = "TestEvent";
        Date eventDateTime = new Date();
        String capacity = "50";
        String price = "10.99";
        String description = "Test Description";

        // Act
        boolean isValid = eventHelper.validateEventData(eventName, eventDateTime, capacity, price, description);

        // Assert
        assertTrue(isValid, "Valid event data should pass validation.");
    }

    @Test
    void testValidateEventData_InvalidData() {
        // Arrange
        String eventName = "";
        Date eventDateTime = null;
        String capacity = "50";
        String price = "";
        String description = "Test Description";

        // Act
        boolean isValid = eventHelper.validateEventData(eventName, eventDateTime, capacity, price, description);

        // Assert
        assertFalse(isValid, "Invalid event data should fail validation.");
    }
}