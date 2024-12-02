package com.example.event_lottery;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
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
public class AdminQRCodeManagementUiTest {

    @Rule
    public ActivityScenarioRule<QRCodeManagementActivity> activityScenarioRule =
            new ActivityScenarioRule<>(
                    new Intent(ApplicationProvider.getApplicationContext(), QRCodeManagementActivity.class)
                            .putExtra("TEST_MODE", true) // enabling test mode for mock data
            );

    @Test
    public void testBrowseQRCodeData() {
        // verifying the list view is displayed
        onView(withId(R.id.list_view)).check(matches(isDisplayed()));
        onView(withText("Sample Event 1")).check(matches(isDisplayed()));  // checking if the mock QR code data is visible in the list
    }

}

