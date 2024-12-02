package com.example.event_lottery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantNotificationWhenNotChosenTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();
    }

    /**
     * Test if the user receives a notification when not chosen from the waiting list.
     */
    @Test
    public void testNotificationWhenNotChosen() {
        String userEmail = "entrant@example.com";

        // simulating the user not being chosen from the waiting list
        testHelper.sendNotification(userEmail, "Sorry! You were not chosen for the event.");

        // verifying the notification log
        assertTrue(testHelper.getNotificationLog().contains(
                "Notification to entrant@example.com: Sorry! You were not chosen for the event."
        ), "Notification should be sent to the user when not chosen from the waiting list");
    }

    /**
     * Test if disabling notifications prevents the user from receiving a notification when not chosen.
     */
    @Test
    public void testNotificationDisabledWhenNotChosen() {
        String userEmail = "entrant@example.com";
        testHelper.setNotificationPreference(userEmail, false);// disabling notifications for the user
        // simulating the user not being chosen from the waiting list
        testHelper.sendNotification(userEmail, "Sorry! You were not chosen for the event.");

        // verifying the notification log
        assertFalse(testHelper.getNotificationLog().contains(
                "Notification to entrant@example.com: Sorry! You were not chosen for the event."
        ), "Notification should not be sent when notifications are disabled");
    }

    /**
     * Test if notifications are handled correctly for non-existent users when not chosen.
     */
    @Test
    public void testNotificationForNonExistentUserWhenNotChosen() {
        String nonExistentUserEmail = "nonExistentUser@example.com";

        // attempt to send a notification to a non-existent user
        testHelper.sendNotification(nonExistentUserEmail, "Sorry! You were not chosen for the event.");

        // verifying that no notification was sent
        assertFalse(testHelper.getNotificationLog().contains(
                "Notification to nonExistentUser@example.com: Sorry! You were not chosen for the event."
        ), "Notification should not be sent to non-existent users");
    }
}