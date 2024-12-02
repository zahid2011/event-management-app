package com.example.event_lottery;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
public class AdminBrowseEventsTest {
    private AdminTestHelper testHelper;

    @Before
    public void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    public void testBrowseAllEvents() {
        List<Event> events = testHelper.getAllEvents();
        assertNotNull("Event list should not be null", events);
        assertEquals("Event list should contain 2 events initially", 2, events.size());
    }

    @Test
    public void testBrowseSpecificEvent() {
        Event event = testHelper.getEventById("1");
        assertNotNull("Event with ID 1 should exist", event);
        assertEquals("Event name should be 'Event A'", "Event A", event.getEventName());
        assertEquals("Event capacity should be '100'", "100", event.getCapacity());
        assertEquals("Event price should be '20.0'", "20.0", event.getPrice());
    }
}
