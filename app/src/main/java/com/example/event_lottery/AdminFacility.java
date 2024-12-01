package com.example.event_lottery;

public class AdminFacility {
    private String facilityId; // Firestore document ID)
    private String facilityName;
    private String facilityDescription;
    private String facilityAddress;
    private String phone;
    private boolean geolocationEnabled; // Boolean field for geolocation

    // Empty constructor for Firebase
    public AdminFacility() {}

    // Constructor
    public AdminFacility(String facilityId, String facilityName, String facilityDescription,
                         String facilityAddress, String phone, boolean geolocationEnabled) {
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.facilityDescription = facilityDescription;
        this.facilityAddress = facilityAddress;
        this.phone = phone;
        this.geolocationEnabled = geolocationEnabled;
    }

    // Getter and Setter for facilityId
    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    // Getter and Setter for facilityName
    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    // Getter and Setter for facilityDescription
    public String getFacilityDescription() {
        return facilityDescription;
    }

    public void setFacilityDescription(String facilityDescription) {
        this.facilityDescription = facilityDescription;
    }

    // Getter and Setter for facilityAddress
    public String getFacilityAddress() {
        return facilityAddress;
    }

    public void setFacilityAddress(String facilityAddress) {
        this.facilityAddress = facilityAddress;
    }

    // Getter and Setter for phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter and Setter for geolocationEnabled
    public boolean isGeolocationEnabled() {
        return geolocationEnabled; // Getter method returns the boolean value
    }

    public void setGeolocationEnabled(boolean geolocationEnabled) {
        this.geolocationEnabled = geolocationEnabled;
    }
}