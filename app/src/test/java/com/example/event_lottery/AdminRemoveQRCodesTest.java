package com.example.event_lottery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminRemoveQRCodesTest {
    private AdminTestHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    void testDeleteQRCodeByEventId_Success() {
        boolean isDeleted = testHelper.deleteQRCodeByEventId("1");
        assertTrue(isDeleted, "QR Code should be successfully deleted");

        String deletedQRCode = testHelper.getQRCodeByEventId("1");
        assertNull(deletedQRCode, "Deleted QR Code should not exist in the mock data");
    }

    @Test
    void testDeleteQRCodeByEventId_Failure() {
        boolean isDeleted = testHelper.deleteQRCodeByEventId("nonExistentId");
        assertFalse(isDeleted, "Non-existent QR Code should not be deleted");
    }

    @Test
    void testAddQRCode() {
        boolean isAdded = testHelper.addQRCode("3", "QRContentC");
        assertTrue(isAdded, "New QR Code should be added successfully");

        String addedQRCode = testHelper.getQRCodeByEventId("3");
        assertNotNull(addedQRCode, "Added QR Code should exist in the mock data");
    }

    @Test
    void testQRCodeCount() {
        int count = testHelper.getQRCodeCount();
        assertEquals(2, count, "QR Code count should be 2 initially");

        testHelper.deleteQRCodeByEventId("1");
        count = testHelper.getQRCodeCount();
        assertEquals(1, count, "QR Code count should decrease by 1 after deletion");
    }
}
