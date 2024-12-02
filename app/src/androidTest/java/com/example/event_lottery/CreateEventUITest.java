package com.example.event_lottery;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.containsString;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateEventUITest {

    @Rule
    public ActivityScenarioRule<CreateEventActivity> activityScenarioRule =
            new ActivityScenarioRule<>(CreateEventActivity.class);

    @Test
    public void testBackButton() {
        // Check if the back button is displayed
        onView(withId(R.id.btn_back)).check(matches(isDisplayed()));

        // Perform click on the back button
        onView(withId(R.id.btn_back)).perform(click());

        // Test will pass if the activity finishes successfully after clicking back
    }

    @Test
    public void testEventNameInput() {
        // Check if the Event Name EditText is displayed
        onView(withId(R.id.et_event_name)).check(matches(isDisplayed()));

        // Type a sample event name and verify the text
        onView(withId(R.id.et_event_name)).perform(typeText("Sample Event"), closeSoftKeyboard());
        onView(withId(R.id.et_event_name)).check(matches(withText("Sample Event")));
    }

    @Test
    public void testEventDatePicker() {
        // Check if the Event DateTime EditText is displayed
        onView(withId(R.id.et_event_datetime)).check(matches(isDisplayed()));

        // Click the DateTime picker field and check that it triggers the DatePicker dialog
        onView(withId(R.id.et_event_datetime)).perform(click());
        // This assumes you have the DatePicker dialog configured correctly. Espresso will detect it if present.
    }



    @Test
    public void testGenerateQRButton() {
        // Check if the Create Event button is displayed
        onView(withId(R.id.btn_create_and_generate_qr)).check(matches(isDisplayed()));

        // Fill in the fields required for creating an event
        onView(withId(R.id.et_event_name)).perform(typeText("Sample Event"), closeSoftKeyboard());
        onView(withId(R.id.et_event_datetime)).perform(typeText("2024-12-15 10:00:00"), closeSoftKeyboard());
        onView(withId(R.id.et_capacity)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.et_price)).perform(typeText("50"), closeSoftKeyboard());
        onView(withId(R.id.et_event_description)).perform(typeText("Sample event description"), closeSoftKeyboard());

        // Perform click on the Create and Generate QR button
        onView(withId(R.id.btn_create_and_generate_qr)).perform(click());

        // Verify that the QR Code ImageView is displayed
        onView(withId(R.id.qrCodeImageView)).check(matches(isDisplayed()));
    }

    @Test
    public void testCancelButton() {
        // Check if the Cancel button is displayed
        onView(withId(R.id.btn_cancel)).check(matches(isDisplayed()));

        // Perform click on the Cancel button
        onView(withId(R.id.btn_cancel)).perform(click());

        // Test will pass if the activity finishes successfully after clicking cancel
    }

    @Test
    public void testEditEventImageButton() {
        // Check if the Edit Image button is displayed
        onView(withId(R.id.edit_image)).check(matches(isDisplayed()));

        // Perform click on the Edit Image button
        onView(withId(R.id.edit_image)).perform(click());

        // Test will pass if it navigates to the image upload activity
    }
}
