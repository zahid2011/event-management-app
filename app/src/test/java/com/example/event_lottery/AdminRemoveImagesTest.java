package com.example.event_lottery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdminRemoveImagesTest {
    private AdminTestHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    void testDeleteImage_Success() {
        boolean isDeleted = testHelper.deleteImage("image1");
        assertTrue(isDeleted, "Image should be successfully deleted");

        List<String> images = testHelper.getAllImages();
        assertFalse(images.contains("image1"), "Deleted image should not exist in the mock data");
    }

    @Test
    void testDeleteImage_Failure() {
        boolean isDeleted = testHelper.deleteImage("nonExistentImage");
        assertFalse(isDeleted, "Non-existent image should not be deleted");
    }

    @Test
    void testGetAllImages() {
        List<String> images = testHelper.getAllImages();
        assertNotNull(images, "Image list should not be null");
        assertEquals(2, images.size(), "Image list should contain 2 images initially");
        assertTrue(images.contains("image1"), "Image list should contain 'image1'");
        assertTrue(images.contains("image2"), "Image list should contain 'image2'");
    }

    @Test
    void testAddImage() {
        boolean isAdded = testHelper.addImage("image3");
        assertTrue(isAdded, "New image should be added successfully");

        List<String> images = testHelper.getAllImages();
        assertEquals(3, images.size(), "Image list should contain 3 images after addition");
        assertTrue(images.contains("image3"), "Image list should contain 'image3'");
    }

    @Test
    void testImageCount() {
        List<String> images = testHelper.getAllImages();
        assertEquals(2, images.size(), "Image count should be 2 initially");

        testHelper.deleteImage("image1");
        images = testHelper.getAllImages();
        assertEquals(1, images.size(), "Image count should be 1 after deletion");
    }

    @Test
    void testDeleteAllImages() {
        List<String> images = testHelper.getAllImages();

        for (String imageId : new ArrayList<>(images)) {
            testHelper.deleteImage(imageId);
        }

        images = testHelper.getAllImages();
        assertEquals(0, images.size(), "Image list should be empty after deleting all images");
    }
}
