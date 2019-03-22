package com.example.rohit.modals;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * This is the database we are creating
 */

@Database(entities = Places.class, version = 1)
public abstract class PlacesDatabase extends RoomDatabase {

    public static PlacesDatabase instance;

    public abstract PlacesDao placeDao();

    public static synchronized PlacesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PlacesDatabase.class,"places_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(databaseCallback)
                    .build();
        }
        return instance;
    }

    private static Callback databaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDatabaseWithDefaultNotes(instance).execute();
        }
    };

    private static class PopulateDatabaseWithDefaultNotes extends AsyncTask<Void,Void,Void> {
        PlacesDao placesDao;

        public PopulateDatabaseWithDefaultNotes(PlacesDatabase instance) {
            placesDao = instance.placeDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //placesDao.insert(new Places("Ghaziabad",55.22,56.64));
            return null;
        }
    }

}
