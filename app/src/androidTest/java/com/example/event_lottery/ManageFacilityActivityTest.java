package com.example.event_lottery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.event_lottery.ManageFacilityActivity;
import com.example.event_lottery.R;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ManageFacilityActivityTest {

    @Test
    public void testInitializeFacilityFields() {
        try (ActivityScenario<ManageFacilityActivity> scenario = ActivityScenario.launch(ManageFacilityActivity.class)) {
            scenario.onActivity(activity -> {
                assertNotNull(activity.findViewById(R.id.et_facility_name));
                assertNotNull(activity.findViewById(R.id.et_facility_address));
                assertNotNull(activity.findViewById(R.id.et_phone));
                assertNotNull(activity.findViewById(R.id.et_email));
            });
        }
    }

    @Test
    public void testSaveFacilityProfileSuccess() {
        try (ActivityScenario<ManageFacilityActivity> scenario = ActivityScenario.launch(ManageFacilityActivity.class)) {
            scenario.onActivity(activity -> {
                // Set up fields and simulate user input
                activity.etFacilityName.setText("Sample Facility");
                activity.etFacilityAddress.setText("123 Main St");
                activity.etPhone.setText("1234567890");
                activity.etEmail.setText("contact@facility.com");
                activity.etFacilityDescription.setText("A sample facility");

                activity.btnSaveChanges.performClick();

                assertEquals("Facility profile saved successfully", activity.getLastToastMessage());
            });
        }
    }

    @Test
    public void testFetchFacilityData() {
        try (ActivityScenario<ManageFacilityActivity> scenario = ActivityScenario.launch(ManageFacilityActivity.class)) {
            scenario.onActivity(activity -> {
                activity.etFacilityName.setText("Sample Facility");
                activity.btnGetFacility.performClick();

                assertEquals("Facility data loaded", activity.getLastToastMessage());
                assertEquals("123 Main St", activity.etFacilityAddress.getText().toString());
            });
        }
    }
}
