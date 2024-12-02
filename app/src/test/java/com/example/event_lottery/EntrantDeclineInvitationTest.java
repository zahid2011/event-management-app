package com.example.event_lottery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantDeclineInvitationTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();
    }

    @Test
    public void testDeclineInvitationSuccess() {
        String eventId = "event1";
        String userEmail = "entrant@example.com";

        // sending invitation
        boolean invitationSent = testHelper.sendInvitation(eventId, userEmail);
        assertTrue(invitationSent, "Invitation should be sent successfully");

        // decline invitation
        boolean declined = testHelper.declineInvitation(userEmail);
        assertTrue(declined, "Invitation should be declined successfully");

        // verifying user is added to declined slots
        String nextUser = testHelper.getNextUserFromDeclined(eventId);
        assertEquals(userEmail, nextUser, "Declined user should be added to the declined slots");
    }

    @Test
    public void testDeclineInvitationFailure() {
        String userEmail = "nonExistentUser@example.com";

        // attempting` to decline a non-existent invitation
        boolean declined = testHelper.declineInvitation(userEmail);
        assertFalse(declined, "Invitation should not be declined for a non-existent user");
    }
}