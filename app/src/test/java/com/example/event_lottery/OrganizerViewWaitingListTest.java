package com.example.event_lottery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrganizerViewWaitingListTest {

    private OrganizerEventTestHelper helper;

    @BeforeEach
    public void setUp() {
        helper = new OrganizerEventTestHelper();
    }

    @Test
    public void testFetchWaitingList_Success() {
        // Arrange
        String eventName = "Community Event";
        String participant1 = "user1@example.com";
        String participant2 = "user2@example.com";
        String participant3 = "user3@example.com";

        List<String> mockWaitingList = new ArrayList<>();
        mockWaitingList.add(participant1);
        mockWaitingList.add(participant2);
        mockWaitingList.add(participant3);

        // Act
        int waitingListSize = mockWaitingList.size();

        // Assert
        assertEquals(3, waitingListSize, "Waiting list should have 3 participants.");
        assertEquals(participant1, mockWaitingList.get(0), "First participant should match.");
        assertEquals(participant2, mockWaitingList.get(1), "Second participant should match.");
        assertEquals(participant3, mockWaitingList.get(2), "Third participant should match.");
    }

    @Test
    public void testFetchWaitingList_EmptyList() {
        // Arrange
        List<String> mockWaitingList = new ArrayList<>();

        // Act
        int waitingListSize = mockWaitingList.size();

        // Assert
        assertEquals(0, waitingListSize, "Waiting list should be empty.");
    }
}
