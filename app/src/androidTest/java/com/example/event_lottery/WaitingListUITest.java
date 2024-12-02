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
public class WaitingListUITest {

    @Rule
    public ActivityScenarioRule<WaitingListActivity> activityScenarioRule =
            new ActivityScenarioRule<>(createWaitingListActivityIntent());

    private static Intent createWaitingListActivityIntent() {
        // Pass the required event_id to the activity via Intent
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), WaitingListActivity.class);
        intent.putExtra("event_id", "test_event_id");
        return intent;
    }

    @Test
    public void testBackButton() {
        // Check if the back button is displayed
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));

        // Click the back button
        onView(withId(R.id.backButton)).perform(click());

        // If the activity finishes without crashing, this test passes.
    }

    @Test
    public void testTotalParticipantsTextView() {
        // Check if the total participants TextView is displayed
        onView(withId(R.id.tv_total_participants)).check(matches(isDisplayed()));

        // Verify the default text
        onView(withId(R.id.tv_total_participants)).check(matches(withText("Total Participants: 0")));
    }

    @Test
    public void testListViewDisplayed() {
        // Check if the waiting list ListView is displayed
        onView(withId(R.id.lv_waiting_list)).check(matches(isDisplayed()));
    }
}
