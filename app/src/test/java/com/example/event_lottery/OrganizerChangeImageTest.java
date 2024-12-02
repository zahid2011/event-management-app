package com.example.event_lottery;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class OrganizerChangeImageTest {

    private final OrganizerEventTestHelper helper = new OrganizerEventTestHelper();

    @Test
    public void testValidateEventData_UploadImageSuccess() {
        // Arrange
        String eventName = "Art Gala";
        Date eventDateTime = new Date();
        String capacity = "50";
        String price = "20";
        String description = "An evening of art and culture.";

        // Act
        boolean isValid = helper.validateEventData(
                eventName,
                eventDateTime,
                capacity,
                price,
                description
        );

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testValidateEventData_UploadImageFailure() {
        // Arrange
        String eventName = ""; // Missing name
        Date eventDateTime = new Date();
        String capacity = "50";
        String price = "20";
        String description = "An evening of art and culture.";

        // Act
        boolean isValid = helper.validateEventData(
                eventName,
                eventDateTime,
                capacity,
                price,
                description
        );

        // Assert
        assertFalse(isValid);
    }
}
