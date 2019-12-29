package com.pf0n1x.getmoredone.entities;

import com.google.firebase.database.IgnoreExtraProperties;

// TODO: Consider ignoring title translatable fields
@IgnoreExtraProperties
public class Achievement {

    // Data Members
    private String id;
    private int stars; // each star represents a level
    private int maxStars;
    private int currentStarProgress; // TODO: Might be better to move to a constant database value
    private int currentStarMaxProgress; // TODO: Might be better to move to a constant database value

    // Constructors
    public Achievement() {

    }

    public Achievement(String id,
                       int stars,
                       int maxStars,
                       int currentStarProgress,
                       int currentStarMaxProgress) {
        this.id = id;
        this.stars = stars;
        this.maxStars = maxStars;
        this.currentStarProgress = currentStarProgress;
        this.currentStarMaxProgress = currentStarMaxProgress;
    }

    // Getters
    public String getId() {
        return this.id;
    }

    public int getStars() {
        return this.stars;
    }

    public int getCurrentStarProgress() {
        return currentStarProgress;
    }

    public void setCurrentStarProgress(int currentStarProgress) {
        this.currentStarProgress = currentStarProgress;
    }

    public int getCurrentStarMaxProgress() {
        return currentStarMaxProgress;
    }

    // Setters
    public void setId(String id) {

        this.id = id;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getMaxStars() {
        return maxStars;
    }

    public void setMaxStars(int maxStars) {
        this.maxStars = maxStars;
    }

    public void setCurrentStarMaxProgress(int currentStarMaxProgress) {
        this.currentStarMaxProgress = currentStarMaxProgress;
    }
}
