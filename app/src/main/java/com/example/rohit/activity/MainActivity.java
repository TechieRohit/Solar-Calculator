package com.example.rohit.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.adapters.PlaceAutocompleteAdapter;
import com.example.rohit.constants.IntentKeys;
import com.example.rohit.constants.Vars;
import com.example.rohit.utils.solarcalculator.Location;
import com.example.rohit.utils.solarcalculator.SunriseSunsetCalculator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.rohit.solarcalulator.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import static com.example.rohit.constants.Vars.DEFAULT_ZOOM;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private final String TAG = "main_activity";

    private android.location.Location mLastKnownLocation;
    private android.location.Location mCurrentLocation;
    private CameraPosition mCameraPosition;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Calendar mCalendar;
    DateFormat hourFormat;
    DateFormat dateFormat;

    private TextView mSunrise,mSunset,mMoonrise,mMoonset,mDate;

    SunriseSunsetCalculator calculator;
    String sunrise,sunset;
    Calendar officialSunset;
    Calendar officialSunrise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initViews();
        initMap();

        mCalendar = Calendar.getInstance();
        hourFormat = new SimpleDateFormat(Vars.STR_HOUR_FORMAT);
        dateFormat = new SimpleDateFormat(Vars.STR_DATE_FORMAT);

        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(IntentKeys.KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(IntentKeys.KEY_CAMERA_POSITION);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude()), DEFAULT_ZOOM));

        }

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }


    private void initViews() {
        mDate = findViewById(R.id.date);
        mSunrise = findViewById(R.id.text_sunrise);
        mSunset = findViewById(R.id.text_sunset);
        mMoonrise = findViewById(R.id.text_moonrise);
        mMoonset = findViewById(R.id.text_moonset);

        com.google.android.libraries.places.api.Places.initialize(getApplicationContext(),
                "AIzaSyAxt0Mx9mDnNlC-rQ6hMieuYhFgI1Z-iuo");

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setCountry("IN");
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setTypeFilter(TypeFilter.REGIONS);

        autocompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,
                com.google.android.libraries.places.api.model.Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.
                PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                Toast.makeText(MainActivity.this,"place - " + place.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(MainActivity.this,"error - " + status, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      /*  mGoogleApiClient = new GoogleApiClient.
                Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();
        mGoogleApiClient.connect();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,mGoogleApiClient,LAT_LNG_BOUNDS,null);
        mPlaceAutoComplete.setAdapter(mPlaceAutocompleteAdapter);*/
         // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (android.location.Location) task.getResult();
                            calculateResult(mLastKnownLocation);
                        } else {
                           /* Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());*/
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void calculateResult(android.location.Location currentLocation) {
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                longitude), DEFAULT_ZOOM));

        Location location = new Location(latitude, longitude);
        calculator = new SunriseSunsetCalculator(location, "");

        updateTimings();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(IntentKeys.KEY_LOCATION, mMap.getCameraPosition());
            outState.putParcelable(IntentKeys.KEY_CAMERA_POSITION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.next_date:
                mCalendar.add(Calendar.DAY_OF_MONTH,1);
                updateTimings();
                break;
            case R.id.current_date:
                mCalendar = Calendar.getInstance();
                updateTimings();
                break;
            case R.id.previous_date:
                mCalendar.add(Calendar.DAY_OF_MONTH,-1);
                updateTimings();
                break;
        }
    }

    private void updateTimings() {
        officialSunset = calculator.getOfficialSunsetCalendarForDate(mCalendar);
        officialSunrise = calculator.getOfficialSunriseCalendarForDate(mCalendar);

        sunrise = hourFormat.format(officialSunrise.getTime());
        sunset = hourFormat.format(officialSunset.getTime());

        mSunrise.setText(sunrise + "");
        mSunset.setText(sunset + "");

        mDate.setText(String.valueOf(mCalendar.getTime()));
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
