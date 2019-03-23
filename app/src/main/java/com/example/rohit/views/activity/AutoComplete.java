package com.example.rohit.views.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.rohit.solarcalulator.R;

import java.util.Arrays;

public class AutoComplete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
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
