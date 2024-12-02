package com.example.event_lottery;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class AdminRemoveEventsTest {
    private AdminTestHelper testHelper;

    @Before
    public void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    public void testDeleteEventById_Success() {
        boolean isDeleted = testHelper.deleteEventById("1");
        assertTrue("Event should be successfully deleted", isDeleted);

        Event deletedEvent = testHelper.getEventById("1");
        assertNull("Deleted event should not exist in the mock data", deletedEvent);
    }

    @Test
    public void testDeleteEventById_Failure() {
        boolean isDeleted = testHelper.deleteEventById("nonExistentId");
        assertFalse("Non-existent event should not be deleted", isDeleted);
    }

    @Test
    public void testAddEvent() {
        Event newEvent = new Event(
                "3", "Event C", null, "300", "40.0", "Description C", true, "QRContentC", "QRHashC");

        boolean isAdded = testHelper.addEvent(newEvent);
        assertTrue("New event should be added successfully", isAdded);

        Event addedEvent = testHelper.getEventById("3");
        assertNotNull("Added event should exist in the mock data", addedEvent);
    }

    @Test
    public void testEventCount() {
        int count = testHelper.getEventCount();
        assertEquals("Event count should be 2 initially", 2, count);

        testHelper.deleteEventById("1");
        count = testHelper.getEventCount();
        assertEquals("Event count should decrease by 1 after deletion", 1, count);
    }
}
