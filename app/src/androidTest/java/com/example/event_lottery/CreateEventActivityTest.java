package com.example.event_lottery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.event_lottery.CreateEventActivity;
import com.example.event_lottery.R;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateEventActivityTest {

    @Test
    public void testInitializeEventFields() {
        try (ActivityScenario<CreateEventActivity> scenario = ActivityScenario.launch(CreateEventActivity.class)) {
            scenario.onActivity(activity -> {
                assertNotNull(activity.findViewById(R.id.et_event_name));
                assertNotNull(activity.findViewById(R.id.et_event_datetime));
                assertNotNull(activity.findViewById(R.id.et_capacity));
                assertNotNull(activity.findViewById(R.id.et_price));
                assertNotNull(activity.findViewById(R.id.et_event_description));
                assertNotNull(activity.findViewById(R.id.switch_geolocation));
            });
        }
    }

    @Test
    public void testSaveEventProfileSuccess() {
        try (ActivityScenario<CreateEventActivity> scenario = ActivityScenario.launch(CreateEventActivity.class)) {
            scenario.onActivity(activity -> {
                // Simulate user input
                activity.etEventName.setText("Sample Event");
                activity.etEventDateTime.setText("2024-12-15 18:30:00");
                activity.etCapacity.setText("150");
                activity.etPrice.setText("50.00");
                activity.etEventDescription.setText("A sample event description");

                activity.btnCreateEvent.performClick();

                assertEquals("Event Created Successfully", activity.getLastToastMessage());
            });
        }
    }

    @Test
    public void testGenerateQRCode() {
        try (ActivityScenario<CreateEventActivity> scenario = ActivityScenario.launch(CreateEventActivity.class)) {
            scenario.onActivity(activity -> {
                activity.etEventName.setText("QRCodeEvent");
                activity.btnGenerateQr.performClick();

                assertNotNull(activity.qrCodeImageView.getDrawable());
                assertEquals("Event and QR hash stored successfully", activity.getLastToastMessage());
            });
        }
    }
}
