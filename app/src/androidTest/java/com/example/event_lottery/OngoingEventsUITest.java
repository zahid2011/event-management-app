package com.example.event_lottery;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.IdlingPolicies;

import java.util.concurrent.TimeUnit;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OngoingEventsUITest {



    @Rule
    public ActivityScenarioRule<OngoingEventsActivity> activityScenarioRule =
            new ActivityScenarioRule<>(createOngoingEventsActivityIntent());

    @Before
    public void setUpTimeout() {
        IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
        IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
    }

    private static Intent createOngoingEventsActivityIntent() {
        // Prepare an Intent to start the OngoingEventsActivity
        return new Intent(ApplicationProvider.getApplicationContext(), OngoingEventsActivity.class);
    }

    @Test
    public void testBackButton() {
        // Verify that the back button is displayed
        onView(withId(R.id.back_button)).check(matches(isDisplayed()));

        // Perform a click on the back button
        onView(withId(R.id.back_button)).perform(click());

        // If no crash occurs, the test passes
    }

    @Test
    public void testTotalEventsTextView() {
        // Verify that the total events TextView is displayed
        onView(withId(R.id.total_events)).check(matches(isDisplayed()));

        // Verify the default text for the total events
        onView(withId(R.id.total_events)).check(matches(withText("Total Events: 0")));
    }

    @Test
    public void testSearchBarDisplayed() {
        // Verify that the search bar EditText is displayed
        onView(withId(R.id.search_input)).check(matches(isDisplayed()));

        // Verify that the search button is displayed
        onView(withId(R.id.search_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testEventsContainerDisplayed() {
        // Verify that the events container is displayed
        onView(withId(R.id.events_container)).check(matches(isDisplayed()));
    }


}
