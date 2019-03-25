package com.example.rohit.views.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.adapters.PlaceArrayAdapter;
import com.example.rohit.constants.IntentKeys;
import com.example.rohit.constants.Vars;

import com.example.rohit.modals.PlaceAutoComplete;
import com.example.rohit.modals.Places;
import com.example.rohit.utils.solarcalculator.Location;
import com.example.rohit.utils.solarcalculator.SunriseSunsetCalculator;
import com.example.rohit.viewmodal.PlaceViewModal;
import com.example.rohit.views.customview.CustomDialogs;
import com.example.rohit.views.customview.DelayAutoCompleteTextView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.rohit.solarcalulator.BuildConfig;
import com.rohit.solarcalulator.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.location.GpsStatus.GPS_EVENT_STARTED;
import static android.location.GpsStatus.GPS_EVENT_STOPPED;
import static com.example.rohit.constants.Vars.DEFAULT_ZOOM;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private final String TAG = "main_activity";

    private GoogleMap mMap;

    private Calendar mCalendar;
    DateFormat hourFormat;
    SimpleDateFormat dateFormat;

    private TextView mSunrise, mSunset, mDate;

    SunriseSunsetCalculator calculator;
    String sunrise, sunset;
    Calendar officialSunset;
    Calendar officialSunrise;

    DelayAutoCompleteTextView searchPlaces;
    PlacesClient placesClient;
    Location location;
    PlaceArrayAdapter mPlaceArrayAdapter;
    String autoCompletePlaceName;
    double autoCompletePlaceLat;
    double autoCompletePlaceLang;
    private PlaceViewModal mPlaceViewModal;
    MarkerOptions markerOptions;
    String date;
    private boolean myLocationPressed = false;

    private double mCurrentPlaceLat;
    private double mCurrentPlaceLong;
    int count = 0;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentPlaceLat = getIntent().getDoubleExtra(IntentKeys.CURRENT_PLACE_LATITUDE,0.00);
        mCurrentPlaceLong = getIntent().getDoubleExtra(IntentKeys.CURRENT_PLACE_LONGITUDE,0.00);
        handler = new Handler();
        initViews();
        initMap();

        mCalendar = Calendar.getInstance();
        try {
            hourFormat = new SimpleDateFormat(Vars.STR_HOUR_FORMAT);
            dateFormat = new SimpleDateFormat(Vars.STR_DATE_FORMAT);
        }catch (Exception e) {
            e.printStackTrace();
        }


        mPlaceViewModal = ViewModelProviders.of(this).get(PlaceViewModal.class);
    }

    private void initViews() {
        com.google.android.libraries.places.api.Places.initialize(getApplicationContext(),
                BuildConfig.places_api_key);
        placesClient = com.google.android.libraries.places.api.Places.createClient(this);
        mDate = findViewById(R.id.date);
        mSunrise = findViewById(R.id.text_sunrise);
        mSunset = findViewById(R.id.text_sunset);

        searchPlaces = (DelayAutoCompleteTextView) findViewById(R.id.search_places_edit);
        searchPlaces.setThreshold(3);
        searchPlaces.setLoadingIndicator((android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator));

        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596));

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, R.layout.simple, bounds, Vars.Country.INDIA);

        searchPlaces.setAdapter(mPlaceArrayAdapter);

        searchPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myLocationPressed = false;
                final PlaceAutoComplete item = mPlaceArrayAdapter.getItem(position);
                final String placeId = (String) item.getPlaceId();
                placeDetails(placeId);
            }
        });
    }


    /**
     * Initiating the Map fragment
     */
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

        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }



    /**
     * Setting MyLocation button as enabled
     * Adding a callback to MyLocation button
     */
    private void updateLocationUI() {
        if (mMap == null) {
            Log.e(TAG, "mMap == null");
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                myLocationPressed = true;
                CustomDialogs.hideKeyboard(MainActivity.this);
                calculateResult(mCurrentPlaceLat,mCurrentPlaceLong);
                return false;
            }
        });
    }

    /**
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    private void getDeviceLocation() {
       calculateResult(mCurrentPlaceLat,mCurrentPlaceLong);
    }

    private void calculateResult(double latitude,double longitude) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                longitude), DEFAULT_ZOOM));

        location = new Location(latitude, longitude);
        updateTimings(location);
    }

    /**
     * This updates the sunrise and sunset time corresponding to a particular area
     * and date
     * @param location
     */
    private void updateTimings(Location location) {
        calculator = new SunriseSunsetCalculator(location, "");
        officialSunset = calculator.getOfficialSunsetCalendarForDate(mCalendar);
        officialSunrise = calculator.getOfficialSunriseCalendarForDate(mCalendar);

        sunrise = hourFormat.format(officialSunrise.getTime());
        sunset = hourFormat.format(officialSunset.getTime());
        mSunrise.setText(sunrise + "");
        mSunset.setText(sunset + "");
        if (dateFormat != null){
            date = dateFormat.format(mCalendar.getTime());
            mDate.setText("" + date);
        }else {
            mDate.setText(String.valueOf(mCalendar.getTime()));
        }
    }

    /**
     * Fetches details of a place corresponding to its placeId
     * @param placeId
     */
    private void placeDetails(String placeId) {
        List<com.google.android.libraries.places.api.model.Place.Field> placeFields =
                Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,
                        com.google.android.libraries.places.api.model.Place.Field.NAME,
                        Place.Field.LAT_LNG);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                CustomDialogs.hideKeyboard(MainActivity.this);
                com.google.android.libraries.places.api.model.Place place = fetchPlaceResponse.getPlace();
                Log.i(TAG, "Place found: " + place.getName());
                Log.i(TAG, "geoCor found: " + place.getLatLng());
                //Toast.makeText(MainActivity.this,"place name - " + place.getName() + place.getLatLng(),Toast.LENGTH_LONG).show();
                LatLng latLng = place.getLatLng();
                autoCompletePlaceName = place.getName();
                if (latLng != null) {
                    autoCompletePlaceLat = latLng.latitude;
                    autoCompletePlaceLang = latLng.longitude;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                    location = new Location(autoCompletePlaceLat,autoCompletePlaceLang);
                    markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    mMap.addMarker(markerOptions);
                    updateTimings(location);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                    Toast.makeText(MainActivity.this,"exception - " + exception.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void viewLocations() {
        startActivity(new Intent(MainActivity.this,SavedPinsActivity.class));
    }

    /**
     * Saving the location to local database
     */
    private void saveLocation() {
        if (autoCompletePlaceName == null || autoCompletePlaceName.isEmpty() || myLocationPressed) {
            Toast.makeText(this,"Please select your place first from the Autosuggestion box !",Toast.LENGTH_LONG).show();
        }else {
            mPlaceViewModal.insert(new Places(autoCompletePlaceName,autoCompletePlaceLat
                    ,autoCompletePlaceLang,sunrise,sunset,date));
            Toast.makeText(this,"Location Saved !", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.next_date:
                mCalendar.add(Calendar.DAY_OF_MONTH,1);
                updateTimings(location);
                break;
            case R.id.current_date:
                mCalendar = Calendar.getInstance();
                updateTimings(location);
                break;
            case R.id.previous_date:
                mCalendar.add(Calendar.DAY_OF_MONTH,-1);
                updateTimings(location);
                break;
            case R.id.save_location:
                saveLocation();
                break;
            case R.id.view_locations:
                viewLocations();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        count++;
        if (count > 1) {
            finish();
        }else {
            Toast.makeText(this,"Press twice to exit",Toast.LENGTH_LONG).show();
        }
        resetCount();
    }

    private void resetCount() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                count = 0;
            }
        },1500);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
