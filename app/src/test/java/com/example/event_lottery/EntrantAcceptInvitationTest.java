package com.example.event_lottery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantAcceptInvitationTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();
    }

    @Test
    public void testAcceptInvitationSuccess() {
        String eventId = "event1";
        String userEmail = "entrant@example.com";

        // Send invitation
        boolean invitationSent = testHelper.sendInvitation(eventId, userEmail);
        assertTrue(invitationSent, "Invitation should be sent successfully");

        // Accept invitation
        boolean accepted = testHelper.acceptInvitation(userEmail);
        assertTrue(accepted, "Invitation should be accepted successfully");

        // Verify no pending invitations
        assertFalse(testHelper.getNotificationLog().contains(userEmail), "No pending invitation should remain");
    }

    @Test
    public void testAcceptInvitationFailure() {
        String userEmail = "nonExistentUser@example.com";

        // Attempt to accept a non-existent invitation
        boolean accepted = testHelper.acceptInvitation(userEmail);
        assertFalse(accepted, "Invitation should not be accepted for a non-existent user");
    }
}
