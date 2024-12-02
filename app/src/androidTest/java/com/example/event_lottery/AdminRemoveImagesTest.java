package com.example.event_lottery;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
public class AdminRemoveImagesTest {
    private AdminTestHelper testHelper;

    @Before
    public void setUp() {
        testHelper = new AdminTestHelper();
    }

    @Test
    public void testDeleteImage_Success() {
        boolean isDeleted = testHelper.deleteImage("image1");
        assertTrue("Image should be successfully deleted", isDeleted);

        List<String> images = testHelper.getAllImages();
        assertFalse("Deleted image should not exist in the mock data", images.contains("image1"));
    }

    @Test
    public void testDeleteImage_Failure() {
        boolean isDeleted = testHelper.deleteImage("nonExistentImage");
        assertFalse("Non-existent image should not be deleted", isDeleted);
    }

    @Test
    public void testGetAllImages() {
        List<String> images = testHelper.getAllImages();
        assertNotNull("Image list should not be null", images);
        assertEquals("Image list should contain 2 images initially", 2, images.size());
        assertTrue("Image list should contain 'image1'", images.contains("image1"));
        assertTrue("Image list should contain 'image2'", images.contains("image2"));
    }

    @Test
    public void testAddImage() {
        boolean isAdded = testHelper.addImage("image3");
        assertTrue("New image should be added successfully", isAdded);

        List<String> images = testHelper.getAllImages();
        assertEquals("Image list should contain 3 images after addition", 3, images.size());
        assertTrue("Image list should contain 'image3'", images.contains("image3"));
    }

    @Test
    public void testImageCount() {
        List<String> images = testHelper.getAllImages();
        assertEquals("Image count should be 2 initially", 2, images.size());

        testHelper.deleteImage("image1");
        images = testHelper.getAllImages();
        assertEquals("Image count should be 1 after deletion", 1, images.size());
    }

    @Test
    public void testDeleteAllImages() {
        List<String> images = testHelper.getAllImages();

        for (String imageId : new ArrayList<>(images)) {
            testHelper.deleteImage(imageId);
        }

        images = testHelper.getAllImages();
        assertEquals("Image list should be empty after deleting all images", 0, images.size());
    }
}
