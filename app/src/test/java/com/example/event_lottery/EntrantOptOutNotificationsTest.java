package com.example.event_lottery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantOptOutNotificationsTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();
    }

    @Test
    public void testOptOutNotifications() {
        String userEmail = "entrant@example.com";
        testHelper.setNotificationPreference(userEmail, false); // disabling notifications

        // verifying that the notifications are disabled
        boolean isNotificationEnabled = testHelper.isNotificationEnabled(userEmail);
        assertFalse(isNotificationEnabled, "User should be able to opt out of notifications");

        // simulating sending a notification
        testHelper.sendNotification(userEmail, "Test Notification");

        // verifying no notification was sent
        assertTrue(testHelper.getNotificationLog().isEmpty(), "No notification should be sent after opting out");
    }

    @Test
    public void testReEnableNotifications() {
        String userEmail = "entrant@example.com";

        // disabling notifications
        testHelper.setNotificationPreference(userEmail, false);
        assertFalse(testHelper.isNotificationEnabled(userEmail), "Notifications should be disabled");
        testHelper.setNotificationPreference(userEmail, true); // enabling notifications

        // verifying that the notifications are re-enabled
        boolean isNotificationEnabled = testHelper.isNotificationEnabled(userEmail);
        assertTrue(isNotificationEnabled, "User should be able to re-enable notifications");
        testHelper.sendNotification(userEmail, "Test Notification"); // simulating sending a notification

        // verifying notification was sent
        assertFalse(testHelper.getNotificationLog().isEmpty(), "Notification should be sent after re-enabling");
    }
}
