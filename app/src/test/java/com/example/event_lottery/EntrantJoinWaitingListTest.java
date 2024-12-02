package com.example.event_lottery;
import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantJoinWaitingListTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();
    }

    @Test
    public void testJoinWaitingListSuccess() {
        // Adding a user to the waiting list
        boolean added = testHelper.addUserToWaitingList("event1", "entrant@example.com");

        // assert user was successfully added
        assertTrue(added, "User should be added to the waiting list");

        // validating that the user is present in the waiting list
        Map<String, Object> eventDetails = testHelper.getEventDetails("event1");
        assertNotNull(eventDetails, "Event details should not be null");
        List<String> waitingList = (List<String>) eventDetails.get("waitingList");
        assertNotNull(waitingList, "Waiting list should not be null");
        assertTrue(waitingList.contains("entrant@example.com"), "Waiting list should contain the user");
    }

    @Test
    public void testJoinWaitingListEventNotFound() {
        // attempting to add a user to a non-existent event
        boolean added = testHelper.addUserToWaitingList("nonExistentEvent", "entrant@example.com");

        // assert the user was not added
        assertFalse(added, "User should not be added to a non-existent event");
    }

    @Test
    public void testJoinWaitingListDuplicateUser() {
        // adding a user to the waiting list
        testHelper.addUserToWaitingList("event1", "entrant@example.com");

        // attempting to add the same user again
        boolean addedAgain = testHelper.addUserToWaitingList("event1", "entrant@example.com");

        // asserting duplicate addition is not allowed
        assertFalse(addedAgain, "User should not be added to the waiting list twice");

        // validating that the user is only present once in the waiting list
        Map<String, Object> eventDetails = testHelper.getEventDetails("event1");
        assertNotNull(eventDetails, "Event details should not be null");
        List<String> waitingList = (List<String>) eventDetails.get("waitingList");
        assertNotNull(waitingList, "Waiting list should not be null");
        assertEquals(1, waitingList.size(), "Waiting list should contain the user only once");
    }
}
