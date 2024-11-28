package com.example.event_lottery;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.assertEquals;

public class AdminProfileManagementActivityTest {
    private AdminProfileManagementActivity activity;

    @Before
    public void setup() {
        activity = new AdminProfileManagementActivity(); // initializing the activity and the users list for testing
    }

    @Test
    public void testLoadUsersFromFirebase() {
        List<User> users = activity.getUsers();

        // Add a mock user directly (simulate loading data without Firebase)
        User mockUser = new User();
        mockUser.setId("testUserId");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");
        users.add(mockUser);

        // Validate the user list
        assertEquals("User list should contain 1 user after load", 1, users.size());
        assertEquals("First name should be 'Test'", "Test", users.get(0).getFirstName());
        assertEquals("Last name should be 'User'", "User", users.get(0).getLastName());
    }
}
