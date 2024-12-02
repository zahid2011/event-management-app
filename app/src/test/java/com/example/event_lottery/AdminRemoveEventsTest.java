package com.example.event_lottery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminRemoveEventsTest {
    private AdminTestHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    void testDeleteEventById_Success() {
        boolean isDeleted = testHelper.deleteEventById("1");
        assertTrue(isDeleted, "Event should be successfully deleted");

        Event deletedEvent = testHelper.getEventById("1");
        assertNull(deletedEvent, "Deleted event should not exist in the mock data");
    }

    @Test
    void testDeleteEventById_Failure() {
        boolean isDeleted = testHelper.deleteEventById("nonExistentId");
        assertFalse(isDeleted, "Non-existent event should not be deleted");
    }

    @Test
    void testAddEvent() {
        Event newEvent = new Event(
                "3", "Event C", null, "300", "40.0", "Description C", true, "QRContentC", "QRHashC");

        boolean isAdded = testHelper.addEvent(newEvent);
        assertTrue(isAdded, "New event should be added successfully");

        Event addedEvent = testHelper.getEventById("3");
        assertNotNull(addedEvent, "Added event should exist in the mock data");
    }

    @Test
    void testEventCount() {
        int count = testHelper.getEventCount();
        assertEquals(2, count, "Event count should be 2 initially");

        testHelper.deleteEventById("1");
        count = testHelper.getEventCount();
        assertEquals(1, count, "Event count should decrease by 1 after deletion");
    }
}
