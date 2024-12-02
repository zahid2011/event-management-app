package com.example.event_lottery;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AdminRemoveProfilesTest {
    private AdminTestHelper testHelper;

    @Before
    public void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    public void testDeleteProfileById_Success() {
        boolean isDeleted = testHelper.deleteProfileById("1");
        assertTrue("Profile should be successfully deleted", isDeleted);

        User deletedUser = testHelper.getProfileById("1");
        assertNull("Deleted profile should not exist in the mock data", deletedUser);
    }

    @Test
    public void testDeleteProfileById_Failure() {
        boolean isDeleted = testHelper.deleteProfileById("nonExistentId");
        assertFalse("Non-existent profile should not be deleted", isDeleted);
    }

    @Test
    public void testAddProfile() {
        User newUser = new User("3", "alex@example.com", "alex123", "Alex", "Johnson", "password789", "User");

        boolean isAdded = testHelper.addProfile(newUser);
        assertTrue("New profile should be added successfully", isAdded);

        User addedUser = testHelper.getProfileById("3");
        assertNotNull("Added profile should exist in the mock data", addedUser);
    }

    @Test
    public void testProfileCount() {
        int count = testHelper.getProfileCount();
        assertEquals("Profile count should be 2 initially", 2, count);

        testHelper.deleteProfileById("1");
        count = testHelper.getProfileCount();
        assertEquals("Profile count should decrease by 1 after deletion", 1, count);
    }
}
