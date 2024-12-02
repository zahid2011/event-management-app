package com.example.event_lottery;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;

class EventsTest {
    private Events event;
    private String eventName = "Sample Event";
    private Date eventDateTime;
    private String capacity = "100";
    private String price = "10";
    private String description = "Sample description";
    @BeforeEach
    void setUp() {
        eventDateTime = new Date();
        event = new Events(eventName, eventDateTime, capacity, price, description);
    }
    @Test
    void testConstructor() {
        assertEquals(eventName, event.getEventName());
        assertEquals(eventDateTime, event.getEventDateTime());
        assertEquals(capacity, event.getCapacity());
        assertEquals(price, event.getPrice());
        assertEquals(description, event.getDescription());
    }
    @Test
    void testDefaultConstructor() {
        Events defaultEvent = new Events();
        assertNotNull(defaultEvent);
    }
    @Test
    void testGetEventName() {
        assertEquals(eventName, event.getEventName());
    }
    @Test
    void testGetEventDateTime() {
        assertEquals(eventDateTime, event.getEventDateTime());
    }
    @Test
    void testGetCapacity() {
        assertEquals(capacity, event.getCapacity());
    }
    @Test
    void testGetPrice() {
        assertEquals(price, event.getPrice());
    }
    @Test
    void testGetDescription() {
        assertEquals(description, event.getDescription());
    }
}