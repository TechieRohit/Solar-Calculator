package com.example.rohit.modals;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import java.util.List;

public class PlaceRepository {

    private PlacesDao placesDao;
    private LiveData<List<Places>> places;

    public PlaceRepository(Application application) {
        PlacesDatabase database = PlacesDatabase.getInstance(application);
        placesDao = database.placeDao();
        places = placesDao.getAllPlaces();
    }


    public void insertNote(Places place){
        new InsertNote(placesDao).execute(place);
    }

    public void deleteNote(Places places) {
        new DeleteNote(placesDao).execute(places);
    }

    public void deleteAllNote() {
        new DeleteAll(placesDao).execute();
    }

    public LiveData<List<Places>> getAllPlaces() {
        return places;
    }

    private class InsertNote extends AsyncTask<Places,Void,Void> {
       private PlacesDao placesDao;

        private InsertNote(PlacesDao placesDao) {
            this.placesDao = placesDao;
        }

        @Override
        protected Void doInBackground(Places... places) {
            placesDao.insert(places[0]);
            return null;
        }
    }


    private class DeleteNote extends AsyncTask<Places,Void,Void> {
        private PlacesDao placesDao;

        private DeleteNote(PlacesDao placesDao) {
            this.placesDao = placesDao;
        }

        @Override
        protected Void doInBackground(Places... places) {
            placesDao.delete(places[0]);
            return null;
        }
    }

    private class DeleteAll extends AsyncTask<Void,Void,Void> {
        private PlacesDao placesDao;

        private DeleteAll(PlacesDao placesDao) {
            this.placesDao = placesDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            placesDao.deleteAllNote();
            return null;
        }
    }
}
