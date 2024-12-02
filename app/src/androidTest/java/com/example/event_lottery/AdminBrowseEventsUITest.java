package com.example.event_lottery;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class AdminBrowseEventsUITest {

    @Rule
    public ActivityTestRule<AdminDashboardActivity> activityRule =
            new ActivityTestRule<>(AdminDashboardActivity.class);

    @Before
    public void setUp() {
        Intents.init(); // Initialize intents to monitor navigation
    }

    @After
    public void tearDown() {
        Intents.release(); // Release intents after the test
    }

    @Test
    public void testNavigateToBrowseEvents() {
        // Click on the "Browse Events" button
        onView(withId(R.id.event_management_button))
                .perform(ViewActions.click());

        // Verify that the intent to EventManagementActivity is launched
        Intents.intended(IntentMatchers.hasComponent(EventManagementActivity.class.getName()));
    }

    @Test
    public void testEventsListDisplayed() {
        // Launch the event management screen
        Intent intent = new Intent();
        ActivityScenario.launch(EventManagementActivity.class);

        // Verify the first event is displayed
        onView(withText("Event A")) // Replace "Event A" with your mock event name
                .check(ViewAssertions.matches(withText("Event A")));
    }
}
