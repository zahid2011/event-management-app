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
public class AdminDashboardUITest {
    @Rule
    public ActivityScenarioRule<AdminDashboardActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AdminDashboardActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testBackButton() {
        // testing if back button navigates to MainActivity
        onView(withId(R.id.backButton)).perform(click());
        intended(hasComponent(MainActivity.class.getName())); // Validate the intent
    }

    @Test
    public void testEventManagementButton() {
        // checking if the "Event Management" card is displayed
        onView(withId(R.id.event_management_button)).check(matches(isDisplayed()));

        // performing click and verify navigation
        onView(withId(R.id.event_management_button)).perform(click());
        intended(hasComponent(EventManagementActivity.class.getName()));
    }

    @Test
    public void testProfileManagementButton() {
        // checking if the "Profile Management" card is displayed
        onView(withId(R.id.profile_management_button)).check(matches(isDisplayed()));

        // performing click and verify navigation
        onView(withId(R.id.profile_management_button)).perform(click());
        intended(hasComponent(AdminProfileManagementActivity.class.getName()));
    }

    @Test
    public void testImageManagementButton() {
        // checking if the "Image Management" card is displayed
        onView(withId(R.id.image_management_button)).check(matches(isDisplayed()));
        onView(withId(R.id.image_management_button)).perform(click());
        intended(hasComponent(AdminImageManagementActivity.class.getName()));
    }

    @Test
    public void testQRCodeDataButton() {
        // checking if the "QR Code Data" card is displayed
        onView(withId(R.id.qr_code_data_button)).check(matches(isDisplayed()));
        onView(withId(R.id.qr_code_data_button)).perform(click());
        intended(hasComponent(QRCodeManagementActivity.class.getName()));
    }

    @Test
    public void testFacilitiesButton() {
        // checking if the "Facilities" card is displayed
        onView(withId(R.id.facilities_button)).check(matches(isDisplayed()));
        onView(withId(R.id.facilities_button)).perform(click());
        intended(hasComponent(AdminListFacilityAcitivity.class.getName()));
    }
}

