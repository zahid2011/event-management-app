package com.example.event_lottery;
import static androidx.test.espresso.Espresso.onData;
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
public class AdminProfileManagementUITest {

    @Rule
    public ActivityScenarioRule<AdminProfileManagementActivity> activityScenarioRule =
            new ActivityScenarioRule<>(
                    new Intent(ApplicationProvider.getApplicationContext(), AdminProfileManagementActivity.class)
                            .putExtra("TEST_MODE", true) // enabling test mode
            );

    @Test
    public void testBrowseProfiles() {
        onView(withId(R.id.list_view)).check(matches(isDisplayed())); // verifying the ListView is displayed
    }

    @Test
    public void testRemoveSpecificProfile() {
        // Click the "Details" button
        onView(withId(R.id.details_button))
                .perform(click());

        // On the profile details page clicks the "Remove Profile" button
        onView(withId(R.id.remove_profile_button))
                .perform(click());

        // Confirm the delete action in the dialog
        onView(withText("Yes"))
                .perform(click());

    }
}