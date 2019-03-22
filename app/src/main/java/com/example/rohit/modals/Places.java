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

    public Places(String placeName, double latitude, double longitude) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
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
