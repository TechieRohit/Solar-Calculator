package com.example.rohit.utils.solarcalculator;

import java.math.BigDecimal;

/**
 * Defines the solar declination used in computing the sunrise/sunset.
 */
public class Zenith {

    /** Official sunrise/set is when the sun is 50' below the horizon. */
    public static final Zenith OFFICIAL = new Zenith(90.8333);

    private final BigDecimal degrees;

    public Zenith(double degrees) {
        this.degrees = BigDecimal.valueOf(degrees);
    }

    public BigDecimal degrees() {
        return degrees;
    }
}
