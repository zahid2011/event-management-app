package com.example.event_lottery;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class OrganizerStoreQRCodeTest {

    private OrganizerEventTestHelper eventHelper;
    private FirebaseFirestore mockFirestore;
    private CollectionReference mockCollection;
    private DocumentReference mockDocument;
    private Task<Void> mockTask;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        eventHelper = new OrganizerEventTestHelper();
        mockFirestore = mock(FirebaseFirestore.class);
        mockCollection = mock(CollectionReference.class);
        mockDocument = mock(DocumentReference.class);
        mockTask = mock(Task.class);

        // Mock Firestore chain
        when(mockFirestore.collection("events")).thenReturn(mockCollection);
        when(mockCollection.document(anyString())).thenReturn(mockDocument);
        when(mockDocument.set(anyMap(), eq(SetOptions.merge()))).thenReturn(mockTask);
    }

    @Test
    void testStoreQRCode() {
        // Arrange
        String eventName = "Tech Expo 2024";
        String qrContent = "myapp://event/" + eventName;

        // Prepare QR code data
        Map<String, Object> qrData = new HashMap<>();
        qrData.put("qrContent", qrContent);
        qrData.put("qrhash", "dummyHash");

        // Act
        Task<Void> returnedTask = mockFirestore.collection("events")
                .document(eventName)
                .set(qrData, SetOptions.merge());

        // Assert
        verify(mockFirestore).collection("events");
        verify(mockCollection).document(eventName);
        verify(mockDocument).set(qrData, SetOptions.merge());
        assertNotNull(returnedTask, "Returned Task should not be null.");
        assertNotNull(qrData.get("qrContent"), "QR Content should not be null.");
    }
}
