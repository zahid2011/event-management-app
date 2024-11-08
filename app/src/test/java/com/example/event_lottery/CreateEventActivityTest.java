package com.example.event_lottery;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.firebase.firestore.FirebaseFirestore;

import android.os.SystemClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.shadows.ShadowToast;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Config(sdk = 29)
@LooperMode(LooperMode.Mode.LEGACY)
public class CreateEventActivityTest {

    private CreateEventActivity activity;
    private FirebaseFirestore mockFirestore;

    @BeforeEach
    void setUp() {
        // Mock FirebaseFirestore
        mockFirestore = Mockito.mock(FirebaseFirestore.class);

        // Initialize activity with Robolectric
        activity = Robolectric.buildActivity(CreateEventActivity.class)
                .create()
                .start()
                .resume()
                .get();

        // Inject mock FirebaseFirestore (requires adding a setter in CreateEventActivity)
        activity.setFirestore(mockFirestore); // Assuming a setFirestore method
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
    }

    @Test
    void testUIElementsExist() {
        try (MockedStatic<SystemClock> mockedClock = Mockito.mockStatic(SystemClock.class)) {
            mockedClock.when(SystemClock::uptimeMillis).thenReturn(123456789L);

            // Verify UI elements
            EditText eventName = activity.findViewById(R.id.et_event_name);
            EditText eventDateTime = activity.findViewById(R.id.et_event_datetime);
            EditText capacity = activity.findViewById(R.id.et_capacity);
            EditText price = activity.findViewById(R.id.et_price);
            EditText description = activity.findViewById(R.id.et_event_description);
            Button btnCreateEvent = activity.findViewById(R.id.btn_create_event);
            Button btnGenerateQr = activity.findViewById(R.id.btn_generate_qr);
            ImageView qrCodeImageView = activity.findViewById(R.id.qrCodeImageView);
            Switch switchGeolocation = activity.findViewById(R.id.switch_geolocation);

            assertNotNull(eventName);
            assertNotNull(eventDateTime);
            assertNotNull(capacity);
            assertNotNull(price);
            assertNotNull(description);
            assertNotNull(btnCreateEvent);
            assertNotNull(btnGenerateQr);
            assertNotNull(qrCodeImageView);
            assertNotNull(switchGeolocation);
        }
    }

    @Test
    void testCreateEventButton_ShowsErrorForEmptyFields() {
        try (MockedStatic<SystemClock> mockedClock = Mockito.mockStatic(SystemClock.class)) {
            mockedClock.when(SystemClock::uptimeMillis).thenReturn(123456789L);

            // Get the Create Event button
            Button btnCreateEvent = activity.findViewById(R.id.btn_create_event);

            // Simulate button click with empty fields
            btnCreateEvent.performClick();

            // Verify Toast message for empty fields
            String toastMessage = ShadowToast.getTextOfLatestToast();
            assertEquals("Please fill in all fields", toastMessage);
        }
    }

    @Test
    void testCreateEventButton_SuccessfulEventCreation() {
        try (MockedStatic<SystemClock> mockedClock = Mockito.mockStatic(SystemClock.class)) {
            mockedClock.when(SystemClock::uptimeMillis).thenReturn(123456789L);

            // Set valid data in fields
            EditText eventName = activity.findViewById(R.id.et_event_name);
            EditText eventDateTime = activity.findViewById(R.id.et_event_datetime);
            EditText capacity = activity.findViewById(R.id.et_capacity);
            EditText price = activity.findViewById(R.id.et_price);
            EditText description = activity.findViewById(R.id.et_event_description);
            Switch switchGeolocation = activity.findViewById(R.id.switch_geolocation);
            Button btnCreateEvent = activity.findViewById(R.id.btn_create_event);

            eventName.setText("Sample Event");
            eventDateTime.setText("2023-01-01 10:00:00");
            capacity.setText("100");
            price.setText("20");
            description.setText("This is a sample event description.");
            switchGeolocation.setChecked(true);

            // Simulate button click
            btnCreateEvent.performClick();

            // Verify success Toast message
            String toastMessage = ShadowToast.getTextOfLatestToast();
            assertEquals("Event Created Successfully", toastMessage);

            // Verify Firestore data saving (mock)
            verify(mockFirestore).collection("events").document("Sample Event");
        }
    }

    @Test
    void testGenerateQRCodeButton() {
        try (MockedStatic<SystemClock> mockedClock = Mockito.mockStatic(SystemClock.class)) {
            mockedClock.when(SystemClock::uptimeMillis).thenReturn(123456789L);

            // Set up the event name for QR code generation
            EditText eventName = activity.findViewById(R.id.et_event_name);
            eventName.setText("Sample Event");

            // Click the Generate QR button
            Button btnGenerateQr = activity.findViewById(R.id.btn_generate_qr);
            btnGenerateQr.performClick();

            // Verify that QR code is generated and displayed in ImageView
            ImageView qrCodeImageView = activity.findViewById(R.id.qrCodeImageView);
            assertNotNull(qrCodeImageView.getDrawable(), "QR code should be generated and displayed in ImageView.");
        }
    }

    @Test
    void testGeolocationToggle() {
        try (MockedStatic<SystemClock> mockedClock = Mockito.mockStatic(SystemClock.class)) {
            mockedClock.when(SystemClock::uptimeMillis).thenReturn(123456789L);

            Switch switchGeolocation = activity.findViewById(R.id.switch_geolocation);

            // Check that the geolocation switch is initially off
            assertFalse(switchGeolocation.isChecked());

            // Toggle the geolocation switch on
            switchGeolocation.setChecked(true);
            assertTrue(switchGeolocation.isChecked());

            // Toggle the geolocation switch off
            switchGeolocation.setChecked(false);
            assertFalse(switchGeolocation.isChecked());
        }
    }
}
