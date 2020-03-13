package com.pf0n1x.getmoredone.entities;

import androidx.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Task implements Comparable<Task> {

    // Data Members;
    private String id;
    private String uid;
    private String title;
    private String description;
    private long creation_date;
    private String date;
    private boolean is_done;

    // Constructors

    /*
        This empty constructor is needed for read operations from firebase.
     */
    public Task() {

    }

    public Task(@NonNull String title,
                @NonNull String uid,
                String description,
                long creation_date,
                String date,
                boolean is_done) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.creation_date = creation_date;
        this.date = date;
        this.is_done = is_done;
    }

    public Task(String id,
                @NonNull String title,
                @NonNull String uid,
                String description,
                long creation_date,
                String date,
                boolean is_done) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.creation_date = creation_date;
        this.date = date;
        this.is_done = is_done;
    }

    // Getters
    public String getId() {
        return this.id;
    }

    public String getUid() {
        return this.uid;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public long getCreation_date() {
        return this.creation_date;
    }

    public String getDate() {
        return this.date;
    }

    public boolean getIs_done() {
        return this.is_done;
    }

    // Setters
    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIs_done(boolean is_done) {
        this.is_done = is_done;
    }

    @Override
    public int compareTo(Task o) {
        if ((o.getIs_done() && this.getIs_done()) ||
                (!o.getIs_done() && !this.getIs_done())) {
            return 0;
        } else if (!this.getIs_done() && o.getIs_done()) {
            return -1;
        } else {
            return 1;
        }
    }
}
