package com.example.event_lottery;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AdminFacilityDetailsUITest {
    private AdminFacility mockFacility = new AdminFacility(
            "1",
            "Community Hall",
            "A large hall for events.",
            "123 Main Street, Cityville",
            "123456789",
            true
    );

    @Rule
    public ActivityScenarioRule<AdminFacilityDetailsActivity> activityScenarioRule =
            new ActivityScenarioRule<>(
                    new Intent(ApplicationProvider.getApplicationContext(), AdminFacilityDetailsActivity.class)
                            .putExtra("TEST_MODE", true)
                            .putExtra("facilityId", "1")
                            .putExtra("facilityName", "Community Hall")
                            .putExtra("facilityDescription", "A large hall for events.")
                            .putExtra("facilityAddress", "123 Main Street, Cityville")
                            .putExtra("phone", "123456789")
                            .putExtra("geolocationEnabled", true)
            );

    @Test
    public void testFacilityDetailsDisplay() {
        // verifying that the facility details are displayed correctly
        onView(withId(R.id.facility_name)).check(matches(withText("Community Hall")));
        onView(withId(R.id.facility_description)).check(matches(withText("A large hall for events.")));
        onView(withId(R.id.facility_address)).check(matches(withText("123 Main Street, Cityville")));
        onView(withId(R.id.facility_phone)).check(matches(withText("123456789")));
        onView(withId(R.id.geolocation_status)).check(matches(withText("Enabled")));
    }

    @Test
    public void testBackButton() {
        onView(withId(R.id.back_button)).perform(click());
    }

    @Test
    public void testDeleteFacility() {
        // simulating clicking the delete button
        onView(withId(R.id.remove_facility_button)).perform(click());

        // confirming the delete action in the dialog
        onView(withText("Yes")).perform(click());

    }
}