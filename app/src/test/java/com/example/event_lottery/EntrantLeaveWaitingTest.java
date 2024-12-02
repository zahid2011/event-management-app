package com.example.event_lottery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantLeaveWaitingTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();
        // initializiong mock data for event1
        testHelper.addUserToWaitingList("event1", "testUser@example.com");
    }

    @Test
    public void testLeaveWaitingListSuccess() {
        // adding a user to the waiting list
        testHelper.addUserToWaitingList("event1", "entrant@example.com");

        // attempting to remove the user from the waiting list
        boolean removed = testHelper.removeUserFromWaitingList("event1", "entrant@example.com");
        assertTrue(removed, "User should be removed from the waiting list");  // Asserting that the user was successfully removed

        // verifying that the waiting list no longer contains the user
        Map<String, Object> eventDetails = testHelper.getEventDetails("event1");
        assertNotNull(eventDetails, "Event details should not be null");
        List<String> waitingList = (List<String>) eventDetails.get("waitingList");
        assertNotNull(waitingList, "Waiting list should not be null");
        assertFalse(waitingList.contains("entrant@example.com"), "Waiting list should not contain the removed user");
    }

    @Test
    public void testLeaveWaitingListNotFound() {
        // attempting to remove a user from a non-existent event
        boolean removed = testHelper.removeUserFromWaitingList("nonExistentEvent", "entrant@example.com");
        assertFalse(removed, "User should not be removed from a non-existent event");// asserting that the removal operation failed
    }

    @Test
    public void testLeaveWaitingListUserNotInList() {
        // ensuring the user is not in the waiting list
        Map<String, Object> eventDetails = testHelper.getEventDetails("event1");
        assertNotNull(eventDetails, "Event details should not be null");
        List<String> waitingList = (List<String>) eventDetails.get("waitingList");
        assertNotNull(waitingList, "Waiting list should not be null");
        assertFalse(waitingList.contains("entrant@example.com"), "User should not be in the waiting list initially");

        // attempting to remove the user from the waiting list
        boolean removed = testHelper.removeUserFromWaitingList("event1", "entrant@example.com");
        assertFalse(removed, "User should not be removed if they are not in the waiting list");// Asserting that the removal operation failed
    }
}