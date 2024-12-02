package com.example.event_lottery;

public class AdminFacility {
    private String facilityId; // Firestore document ID
    private String facilityName;
    private String facilityDescription;
    private String facilityAddress;
    private String phone;
    private boolean geolocationEnabled; // Boolean field for geolocation

    /**
     * Empty constructor for Firebase.
     */
    public AdminFacility() {}

    /**
     * Constructor to initialize facility details.
     *
     * @param facilityId The unique ID of the facility.
     * @param facilityName The name of the facility.
     * @param facilityDescription A description of the facility.
     * @param facilityAddress The address of the facility.
     * @param phone The contact phone number for the facility.
     * @param geolocationEnabled Whether geolocation is enabled for the facility.
     */
    public AdminFacility(String facilityId, String facilityName, String facilityDescription,
                         String facilityAddress, String phone, boolean geolocationEnabled) {
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.facilityDescription = facilityDescription;
        this.facilityAddress = facilityAddress;
        this.phone = phone;
        this.geolocationEnabled = geolocationEnabled;
    }

    /**
     * Gets the facility ID.
     *
     * @return The facility ID.
     */
    public String getFacilityId() {
        return facilityId;
    }

    /**
     * Sets the facility ID.
     *
     * @param facilityId The facility ID to set.
     */
    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    /**
     * Gets the facility name.
     *
     * @return The facility name.
     */
    public String getFacilityName() {
        return facilityName;
    }

    /**
     * Sets the facility name.
     *
     * @param facilityName The facility name to set.
     */
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    /**
     * Gets the facility description.
     *
     * @return The facility description.
     */
    public String getFacilityDescription() {
        return facilityDescription;
    }

    /**
     * Sets the facility description.
     *
     * @param facilityDescription The facility description to set.
     */
    public void setFacilityDescription(String facilityDescription) {
        this.facilityDescription = facilityDescription;
    }

    /**
     * Gets the facility address.
     *
     * @return The facility address.
     */
    public String getFacilityAddress() {
        return facilityAddress;
    }

    /**
     * Sets the facility address.
     *
     * @param facilityAddress The facility address to set.
     */
    public void setFacilityAddress(String facilityAddress) {
        this.facilityAddress = facilityAddress;
    }

    /**
     * Gets the phone number.
     *
     * @return The phone number of the facility.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number.
     *
     * @param phone The phone number to set.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Checks if geolocation is enabled for the facility.
     *
     * @return true if geolocation is enabled, false otherwise.
     */
    public boolean isGeolocationEnabled() {
        return geolocationEnabled;
    }

    /**
     * Sets the geolocation status for the facility.
     *
     * @param geolocationEnabled The geolocation status to set.
     */
    public void setGeolocationEnabled(boolean geolocationEnabled) {
        this.geolocationEnabled = geolocationEnabled;
    }
}