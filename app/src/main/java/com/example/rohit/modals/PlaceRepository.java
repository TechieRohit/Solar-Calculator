package com.example.rohit.modals;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import java.util.List;

public class PlaceRepository {

    private PlacesDao mplacesDao;
    private List<Places> mplaces;
    private LiveData<List<Places>> places;

    public PlaceRepository(Application application) {
        PlacesDatabase database = PlacesDatabase.getInstance(application);
        mplacesDao = database.placeDao();
        places = mplacesDao.getAllPlaces();
        new GetAllNotes(mplacesDao).execute();
    }


    public void insertNote(Places place){
        new InsertNote(mplacesDao).execute(place);
    }

    public void deleteNote(Places places) {
        new DeleteNote(mplacesDao).execute(places);
    }

    public void deleteAllNote() {
        new DeleteAll(mplacesDao).execute();
    }

    public List<Places> getMplaces() {
        return mplaces;
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

    private class GetAllNotes extends AsyncTask<Void,Void,List<Places>> {
        private PlacesDao placesDao;

        private GetAllNotes(PlacesDao placesDao) {
            this.placesDao = placesDao;
        }

        @Override
        protected List<Places> doInBackground(Void... voids) {
           return placesDao.getAllNotes();
        }

        @Override
        protected void onPostExecute(List<Places> places) {
            mplaces = places;
            super.onPostExecute(places);
        }
    }
}
