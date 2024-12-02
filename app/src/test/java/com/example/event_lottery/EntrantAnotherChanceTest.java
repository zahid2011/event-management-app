package com.example.event_lottery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntrantAnotherChanceTest {
    private EntrantUnitTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new EntrantUnitTestHelper();

        // adding declined users
        testHelper.addToDeclinedSlots("event1", "user1@example.com");
        testHelper.addToDeclinedSlots("event1", "user2@example.com");
    }

    @Test
    public void testGetNextUserFromDeclined() {
        // geting the next user from declined slots
        String nextUser = testHelper.getNextUserFromDeclined("event1");

        // verifying the first user is returned
        assertEquals("user1@example.com", nextUser, "The first user from the declined slots should be returned");
    }

    @Test
    public void testDeclinedSlotsEmptyAfterAllUsers() {
        // retrieving all users from declined slots
        testHelper.getNextUserFromDeclined("event1");
        testHelper.getNextUserFromDeclined("event1");

        // attempting to retrieve another user
        String nextUser = testHelper.getNextUserFromDeclined("event1");
        assertNull(nextUser, "No user should be returned when declined slots are empty");
    }
}
