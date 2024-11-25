package com.example.event_lottery;

public class ValidationUtils {

    public static boolean areFieldsFilled(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmailValid(String email) {

        //checking if email has these characters
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email != null && email.matches(emailRegex);
    }

    public static boolean isPasswordValid(String password) {
        // Password should be at least 6 characters
        return password != null && password.length() >= 6;
    }
}
