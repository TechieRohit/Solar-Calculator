package com.example.rohit.utils.solarcalculator;

import java.math.BigDecimal;

/**
 * Simple VO class to store latitude/longitude information.
 */
public class Location {
    private BigDecimal latitude;
    private BigDecimal longitude;


    /**
     * Creates a new instance of <code>Location</code> with the given parameters.
     * 
     * @param latitude
     *            the latitude, in degrees, of this location. North latitude is positive, south negative.
     * @param longitude
     *            the longitude, in degrees, of this location. East longitude is positive, east negative.
     */
    public Location(double latitude, double longitude) {
        this.latitude = new BigDecimal(latitude);
        this.longitude = new BigDecimal(longitude);
    }

    /**
     * @return the latitude
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public BigDecimal getLongitude() {
        return longitude;
    }



}
