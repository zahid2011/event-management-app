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
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testNavigateToBrowseEvents() {
        // clicking on the "Browse Events" button
        onView(withId(R.id.event_management_button))
                .perform(ViewActions.click());

        // verifying that the intent to EventManagementActivity is launched
        Intents.intended(IntentMatchers.hasComponent(EventManagementActivity.class.getName()));
    }

}
