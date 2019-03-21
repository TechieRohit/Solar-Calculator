package com.example.rohit.modals;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * This is basically a particular table(Entity) we want to create
 * in Database
 */

@Entity(tableName = "places_table")
public class Places {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private int priority;

    public Places(String title, int priority) {
        this.title = title;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }


    public int getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
