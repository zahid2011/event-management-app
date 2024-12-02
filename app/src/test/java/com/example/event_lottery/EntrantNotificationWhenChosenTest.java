package com.example.event_lottery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantNotificationWhenChosenTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();
    }

    /**
     * Test if notifications are correctly enabled by default.
     */
    @Test
    public void testNotificationsEnabledByDefault() {
        String userEmail = "entrant@example.com";

        // checking if notifications are enabled for the user
        boolean isNotificationEnabled = testHelper.isNotificationEnabled(userEmail);
        assertTrue(isNotificationEnabled, "Notifications should be enabled by default for the user");
    }

    /**
     * Test if the user receives a notification when chosen from the waiting list.
     */
    @Test
    public void testNotificationWhenChosen() {
        String userEmail = "entrant@example.com";

        // simulating the user being chosen from the waiting list
        testHelper.sendNotification(userEmail, "You have been chosen for the event!");

        // verify the notification log
        assertTrue(testHelper.getNotificationLog().contains(
                "Notification to entrant@example.com: You have been chosen for the event!"
        ), "Notification should be sent to the user when chosen from the waiting list");
    }

    /**
     * Test if disabling notifications prevents the user from receiving a notification.
     */
    @Test
    public void testDisableNotificationsPreventsNotification() {
        String userEmail = "entrant@example.com";

        // disabling notifications for the user
        testHelper.setNotificationPreference(userEmail, false);

        testHelper.sendNotification(userEmail, "You have been chosen for the event!");

        // verifying the notification log
        assertFalse(testHelper.getNotificationLog().contains(
                "Notification to entrant@example.com: You have been chosen for the event!"
        ), "Notification should not be sent when notifications are disabled");
    }

    /**
     * Test if enabling notifications after disabling allows the user to receive notifications again.
     */
    @Test
    public void testEnableNotificationsAfterDisabling() {
        String userEmail = "entrant@example.com";

        // disable notifications for the user
        testHelper.setNotificationPreference(userEmail, false);
        assertFalse(testHelper.isNotificationEnabled(userEmail), "Notifications should be disabled for the user");

        // enable notifications for the user
        testHelper.setNotificationPreference(userEmail, true);

        // simulating the user being chosen from the waiting list
        testHelper.sendNotification(userEmail, "You have been chosen for the event!");

        // verifying the notification log
        assertTrue(testHelper.getNotificationLog().contains(
                "Notification to entrant@example.com: You have been chosen for the event!"
        ), "Notification should be sent when notifications are re-enabled");
    }

    /**
     * Test if notifications are handled correctly for non-existent users.
     */
    @Test
    public void testNotificationForNonExistentUser() {
        String nonExistentUserEmail = "nonExistentUser@example.com";

        // attempting to send a notification to a non-existent user
        testHelper.sendNotification(nonExistentUserEmail, "You have been chosen for the event!");

        // verifying that no notification was sent
        assertFalse(testHelper.getNotificationLog().contains(
                "Notification to nonExistentUser@example.com: You have been chosen for the event!"
        ), "Notification should not be sent to non-existent users");
    }
}