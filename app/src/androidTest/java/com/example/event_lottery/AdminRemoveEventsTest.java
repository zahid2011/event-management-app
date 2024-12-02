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
}
