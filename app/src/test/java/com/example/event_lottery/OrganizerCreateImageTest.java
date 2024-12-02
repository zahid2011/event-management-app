package com.example.event_lottery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrganizerCreateImageTest {
    private OrganizerEventTestHelper helper;

    @BeforeEach
    public void setUp() {
        helper = new OrganizerEventTestHelper();
    }

    @Test
    public void testCreateEventData_WithImageIncluded() {
        // Arrange
        String eventName = "Tech Conference";
        Date eventDateTime = new Date();
        String capacity = "100";
        String price = "50";
        String description = "A conference on technology.";
        boolean geolocationEnabled = true;
        String imagePath = "/images/event_poster.jpg";

        // Act
        Map<String, Object> eventData = helper.createEventData(
                eventName,
                eventDateTime,
                capacity,
                price,
                description,
                geolocationEnabled,
                imagePath
        );

        // Assert
        assertEquals(eventName, eventData.get("eventName"));
        assertEquals(eventDateTime, eventData.get("eventDateTime"));
        assertEquals(capacity, eventData.get("capacity"));
        assertEquals(price, eventData.get("price"));
        assertEquals(description, eventData.get("description"));
        assertEquals(geolocationEnabled, eventData.get("geolocationEnabled"));
        assertEquals(imagePath, eventData.get("imagePath"));
    }
}
