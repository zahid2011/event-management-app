package com.example.event_lottery;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class OrganizerLimitWaitingListTest {

    private final OrganizerEventTestHelper helper = new OrganizerEventTestHelper();

    @Test
    public void testLimitWaitingList_SuccessfulValidation() {
        // Arrange
        String eventName = "Charity Ball";
        Date eventDateTime = new Date();
        String capacity = "100";
        String price = "75";
        String description = "A charity ball event.";
        boolean geolocationEnabled = true;
        String imagePath = "/images/event_poster.jpg";
        int maxWaitingListLimit = 50;
        int currentWaitingListSize = 30;

        // Act
        boolean isWithinLimit = currentWaitingListSize < maxWaitingListLimit;

        // Assert
        assertTrue(isWithinLimit, "The current waiting list size should be within the allowed limit.");
    }

    @Test
    public void testLimitWaitingList_ExceedsLimit() {
        // Arrange
        String eventName = "Tech Meetup";
        Date eventDateTime = new Date();
        String capacity = "200";
        String price = "20";
        String description = "A meetup for tech enthusiasts.";
        boolean geolocationEnabled = false;
        String imagePath = "/images/event_poster.jpg";
        int maxWaitingListLimit = 50;
        int currentWaitingListSize = 55;

        // Act
        boolean isWithinLimit = currentWaitingListSize < maxWaitingListLimit;

        // Assert
        assertFalse(isWithinLimit, "The current waiting list size should exceed the allowed limit.");
    }
}
