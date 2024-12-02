package com.example.event_lottery;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OrganizerDashboardUITest {
    @Rule
    public ActivityScenarioRule<OrganizerDashboardActivity> activityScenarioRule =
            new ActivityScenarioRule<>(OrganizerDashboardActivity.class);

    @Before
    public void setUp() {
        Intents.init(); // Initialize Intents
    }

    @After
    public void tearDown() {
        Intents.release(); // Release Intents
    }

    @Test
    public void testBackButton() {
        // Check if back button navigates to MainActivity
        onView(withId(R.id.backButton)).perform(click());
        intended(hasComponent(MainActivity.class.getName())); // Validate intent
    }

    @Test
    public void testCreateNewEventsButton() {
        // Check if the "Create New Events" card is displayed
        onView(withId(R.id.btn_create_event)).check(matches(isDisplayed()));

        // Perform click and verify navigation
        onView(withId(R.id.btn_create_event)).perform(click());
        intended(hasComponent(CreateEventActivity.class.getName()));
    }

    @Test
    public void testOngoingEventsButton() {
        // Test "Ongoing Events" navigation
        onView(withId(R.id.btn_ongoing_events)).perform(click());
        intended(hasComponent(OngoingEventsActivity.class.getName()));
    }



    @Test
    public void testManageFacilityButton() {
        // Test "Manage Facility" navigation
        onView(withId(R.id.btn_manage_facility)).perform(click());
        intended(hasComponent(ManageFacilityActivity.class.getName()));
    }
}
