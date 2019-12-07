package com.pf0n1x.getmoredone.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.pf0n1x.getmoredone.util.DateConverter;
import com.pf0n1x.getmoredone.util.TimeConverter;
import java.sql.Time;
import java.util.Date;

// TODO: Implement mission DAO
@Entity(tableName = "task_table")
public class Task {

    // Data Members;
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "creation_date")
    private Date creation_date;

    @ColumnInfo(name = "start_date")
    private Date start_date;

    @ColumnInfo(name = "end_date")
    private Date end_date;

    @ColumnInfo(name = "start_time")
    private Time start_time;

    @ColumnInfo(name = "end_time")
    private Time end_time;

    @ColumnInfo (name = "is_done")
    private boolean is_done;

    public Task(@NonNull String title,
                String description,
                Date creation_date,
                Date start_date,
                Date end_date,
                Time start_time,
                Time end_time,
                boolean is_done) {
        this.title = title;
        this.description = description;
        this.creation_date = creation_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.is_done = is_done;
    }

    @Ignore
    public Task(int id,
                @NonNull String title,
                String description,
                Date creation_date,
                Date start_date,
                Date end_date,
                Time start_time,
                Time end_time,
                boolean is_done) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creation_date = creation_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.is_done = is_done;
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Date getCreation_date() {
        return this.creation_date;
    }

    public Date getStart_date() { return this.start_date; }

    public Date getEnd_date() { return this.end_date; }

    public Time getStart_time() {
        return this.start_time;
    }

    public Time getEnd_time() {
        return this.end_time;
    }
    public boolean getIs_done() { return this.is_done; }

    // Setters
    public void setId(@NonNull int id) { this.id = id; }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }
    public void setIs_done(boolean is_done) { this.is_done = is_done; }
}
