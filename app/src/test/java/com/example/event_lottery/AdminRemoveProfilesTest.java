package com.example.event_lottery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminRemoveProfilesTest {
    private AdminTestHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    void testDeleteProfileById_Success() {
        boolean isDeleted = testHelper.deleteProfileById("1");
        assertTrue(isDeleted, "Profile should be successfully deleted");

        User deletedUser = testHelper.getProfileById("1");
        assertNull(deletedUser, "Deleted profile should not exist in the mock data");
    }

    @Test
    void testDeleteProfileById_Failure() {
        boolean isDeleted = testHelper.deleteProfileById("nonExistentId");
        assertFalse(isDeleted, "Non-existent profile should not be deleted");
    }

    @Test
    void testAddProfile() {
        User newUser = new User("3", "alex@example.com", "alex123", "Alex", "Johnson", "password789", "User");

        boolean isAdded = testHelper.addProfile(newUser);
        assertTrue(isAdded, "New profile should be added successfully");

        User addedUser = testHelper.getProfileById("3");
        assertNotNull(addedUser, "Added profile should exist in the mock data");
    }

    @Test
    void testProfileCount() {
        int count = testHelper.getProfileCount();
        assertEquals(2, count, "Profile count should be 2 initially");

        testHelper.deleteProfileById("1");
        count = testHelper.getProfileCount();
        assertEquals(1, count, "Profile count should decrease by 1 after deletion");
    }
}
