package com.example.rohit.modals;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PlacesDao {

    @Insert
    void insert(Places places);

    @Update
    void update(Places places);

    @Delete
    void delete(Places places);

    @Query("DELETE from places_table")
    void deleteAllNote();

    @Query("SELECT * FROM places_table ORDER BY id DESC")
    LiveData<List<Places>> getAllPlaces();
}

