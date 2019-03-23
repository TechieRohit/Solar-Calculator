package com.example.rohit.views.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.rohit.adapters.SavedPlacesAdapter;
import com.example.rohit.modals.Places;
import com.example.rohit.viewmodal.PlaceViewModal;
import com.rohit.solarcalulator.R;

import java.util.ArrayList;
import java.util.List;

public class SavedPinsActivity extends AppCompatActivity implements View.OnClickListener {

    private PlaceViewModal mPlaceViewModal;
    private RecyclerView mRecyclerView;
    private SavedPlacesAdapter mSavedPlacesAdapter;
    private List<Places> mPlaces = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.support.design.R.anim.abc_slide_in_bottom,
                android.support.design.R.anim.abc_slide_out_bottom);
        setContentView(R.layout.activity_saved_pins);

        mPlaceViewModal = ViewModelProviders.of(this).get(PlaceViewModal.class);
        mRecyclerView = findViewById(R.id.recyclerview_saved_locations);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSavedPlacesAdapter = new SavedPlacesAdapter(this,mPlaces);
        mRecyclerView.setAdapter(mSavedPlacesAdapter);

        mPlaceViewModal.getAllPlaces().observe(this, new Observer<List<Places>>() {
            @Override
            public void onChanged(@Nullable List<Places> places) {
                if (!places.isEmpty()) {
                    findViewById(R.id.textView3).setVisibility(View.GONE);
                }
                mSavedPlacesAdapter.setNotes(places);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        openActivityfromBottom();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityfromBottom();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close) {
            finish();
            openActivityfromBottom();
        }
    }

    protected void openActivityfromBottom() {
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }

}
