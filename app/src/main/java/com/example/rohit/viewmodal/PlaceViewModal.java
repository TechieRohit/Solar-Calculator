package com.example.rohit.viewmodal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.rohit.modals.PlaceRepository;
import com.example.rohit.modals.Places;

import java.util.List;

/**
 * ViewModel contains a reference to the repository
 * to perform crucial task
 */
public class PlaceViewModal extends AndroidViewModel {

    private PlaceRepository placeRepository;
    private LiveData<List<Places>> allPlaces;

    public PlaceViewModal(@NonNull Application application) {
        super(application);
        placeRepository = new PlaceRepository(application);
        allPlaces = placeRepository.getAllPlaces();
    }

    public void insert(Places place) {
        placeRepository.insertNote(place);
    }

    public void delete(Places place) {
        placeRepository.deleteNote(place);
    }

    public void deleteAllNotes() {
        placeRepository.deleteAllNote();
    }

    public LiveData<List<Places>> getAllPlaces() {
        return allPlaces;
    }
}
