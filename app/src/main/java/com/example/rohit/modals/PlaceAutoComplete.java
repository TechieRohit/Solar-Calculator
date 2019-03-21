package com.example.rohit.modals;

public class PlaceAutoComplete {

    public CharSequence placeId;
    public CharSequence description;


    public PlaceAutoComplete(CharSequence placeId, CharSequence description) {
        this.placeId = placeId;
        this.description = description;
    }

    @Override
    public String toString() {
        return description.toString();
    }

    public CharSequence getPlaceId() {
        return placeId;
    }

    public CharSequence getDescription() {
        return description;
    }
}
