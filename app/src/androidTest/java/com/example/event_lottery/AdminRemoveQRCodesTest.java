package com.example.event_lottery;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class AdminRemoveQRCodesTest {
    private AdminTestHelper testHelper;

    @Before
    public void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    public void testDeleteQRCodeByEventId_Success() {
        boolean isDeleted = testHelper.deleteQRCodeByEventId("1");
        assertTrue("QR Code should be successfully deleted", isDeleted);

        String deletedQRCode = testHelper.getQRCodeByEventId("1");
        assertNull("Deleted QR Code should not exist in the mock data", deletedQRCode);
    }

    @Test
    public void testDeleteQRCodeByEventId_Failure() {
        boolean isDeleted = testHelper.deleteQRCodeByEventId("nonExistentId");
        assertFalse("Non-existent QR Code should not be deleted", isDeleted);
    }

    @Test
    public void testAddQRCode() {
        boolean isAdded = testHelper.addQRCode("3", "QRContentC");
        assertTrue("New QR Code should be added successfully", isAdded);

        String addedQRCode = testHelper.getQRCodeByEventId("3");
        assertNotNull("Added QR Code should exist in the mock data", addedQRCode);
    }

    @Test
    public void testQRCodeCount() {
        int count = testHelper.getQRCodeCount();
        assertEquals("QR Code count should be 2 initially", 2, count);

        testHelper.deleteQRCodeByEventId("1");
        count = testHelper.getQRCodeCount();
        assertEquals("QR Code count should decrease by 1 after deletion", 1, count);
    }
}
