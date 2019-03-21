package com.example.rohit.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.rohit.solarcalulator.R;

import java.util.Arrays;

public class AutoComplete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete);

        com.google.android.libraries.places.api.Places.initialize(getApplicationContext(),
                "AIzaSyAxt0Mx9mDnNlC-rQ6hMieuYhFgI1Z-iuo");

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
         autocompleteFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
             @Override
             public void onPlaceSelected(@NonNull Place place) {
                 Toast.makeText(AutoComplete.this,"place - " + place.getName(), Toast.LENGTH_LONG).show();
             }

             @Override
             public void onError(@NonNull Status status) {
                 Log.i("error", "An error occurred: " + status);
                 Toast.makeText(AutoComplete.this,"error - " + status, Toast.LENGTH_LONG).show();
             }
         });
    }
}
