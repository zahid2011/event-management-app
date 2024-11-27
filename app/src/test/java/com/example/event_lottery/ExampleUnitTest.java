package com.example.event_lottery;

import org.testng.annotations.Test;


import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import android.util.Log;

public class ExampleUnitTest {


    //Tests for signup activity

    @Test
    public void testAreFieldsFilled_AllFieldsFilled() {
        boolean result = ValidationUtils.areFieldsFilled("email@example.com", "password123", "username", "FirstName", "LastName", "Entrant");
        assertTrue("All fields are filled, should return true", result);
    }

    @Test
    public void testAreFieldsFilled_MissingField() {
        boolean result = ValidationUtils.areFieldsFilled("ema12313213il@example.com", "", "username", "FirstName", "LastName", "Entrant");
        assertFalse("One field is empty, should return false", result);
    }

    @Test
    public void testIsEmailValid_ValidEmail() {
        boolean result = ValidationUtils.isEmailValid("emai2e212313l@example.com");
        assertTrue("Valid email should return true", result);
    }

    @Test
    public void testIsEmailValid_InvalidEmail() {
        boolean result = ValidationUtils.isEmailValid("invalid-email");
        assertFalse("Invalid email should return false", result);
    }

    @Test
    public void testIsPasswordValid_ValidPassword() {
        boolean result = ValidationUtils.isPasswordValid("password123");
        assertTrue("Password with 9 characters should return true", result);
    }

    @Test
    public void testIsPasswordValid_ShortPassword() {
        boolean result = ValidationUtils.isPasswordValid("123");
        assertFalse("Password with less than 6 characters should return false", result);
    }

    @Test
    public void testIsPasswordValid_NullPassword() {
        boolean result = ValidationUtils.isPasswordValid(null);
        assertFalse("Null password should return false", result);
    }

    @Test
    public void testAreFieldsFilled_NullField() {
        boolean result = ValidationUtils.areFieldsFilled("email@example.com", null, "username", "FirstName", "LastName", "Entrant");
        assertFalse("Null field should return false", result);
    }





};
