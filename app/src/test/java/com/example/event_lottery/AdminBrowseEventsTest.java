package com.example.event_lottery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdminBrowseEventsTest {
    private AdminTestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    public void testBrowseAllEvents() {
        List<Event> events = testHelper.getAllEvents();
        assertNotNull(events, "Event list should not be null");
        assertEquals(2, events.size(), "Event list should contain 2 events initially");
    }

    @Test
    public void testBrowseSpecificEvent() {
        Event event = testHelper.getEventById("1");
        assertNotNull(event, "Event with ID 1 should exist");
        assertEquals("Event A", event.getEventName(), "Event name should be 'Event A'");
        assertEquals("100", event.getCapacity(), "Event capacity should be '100'");
        assertEquals("20.0", event.getPrice(), "Event price should be '20.0'");
    }
}
