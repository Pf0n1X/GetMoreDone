package com.pf0n1x.getmoredone.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.pf0n1x.getmoredone.util.DateConverter;

// TODO: Implement account DAO
@Entity(tableName = "account_table")
@TypeConverters(DateConverter.class)
public class Account {

    // Data Members
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String user_name;

    @ColumnInfo(name = "first_name")
    private String first_name;

    @ColumnInfo(name = "last_name")
    private String last_name;

    @ColumnInfo(name = "experience")
    private int experience;

    @ColumnInfo(name = "level")
    private int level;

    // Constructors
    public Account(@NonNull String user_name,
                   String first_name,
                   String last_name,
                   int experience,
                   int level) {
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.experience = experience;
        this.level = level;
    }

    // Getters
    public String getUser_name() {
        return user_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    // Setters
    public void setUser_name(@NonNull String user_name) {
        this.user_name = user_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
