package com.example.rohit.modals;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.math.BigDecimal;

/**
 * This is basically a particular table(Entity) we want to create
 * in Database
 */

@Entity(tableName = "places_table")
public class Places {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String placeName;
    private double latitude;
    private double longitude;
    private String sunrise;
    private String sunset;
    private String date;

    public Places(String placeName, double latitude, double longitude,String sunrise,String sunset,String date) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
