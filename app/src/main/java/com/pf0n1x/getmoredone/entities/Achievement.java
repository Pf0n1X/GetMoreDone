package com.pf0n1x.getmoredone.entities;

import com.google.firebase.database.IgnoreExtraProperties;

// TODO: Consider ignoring title translatable fields
@IgnoreExtraProperties
public class Achievement {

    // Data Members
    private String id;
    private int stars;

    // Constructors
    public Achievement() {

    }

    public Achievement(String id, int stars) {
        this.id = id;
        this.stars = stars;
    }

    // Getters
    public String getId() {
        return this.id;
    }

    public int getStars() {
        return this.stars;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
