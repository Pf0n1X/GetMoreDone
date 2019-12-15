package com.pf0n1x.getmoredone.entities;

public class Account {

    // Data Members
    private String uid;
    private String name;
    private int weeklyExperience; // represents weekly experience
    private String email;
    private int league; // TODO: Move this to an enum representing a league/tier
    private int weeklyGroup;
    private int streak;
    private int money;

    // Constructors

    /*
       This empty constructor is needed for read operations from firebase.
    */
    public Account() {

    }

    public Account(String uid, String name, int weeklyExperience, String email, int league, int weeklyGroup, int streak, int money) {
        this.uid = uid;
        this.name = name;
        this.weeklyExperience = weeklyExperience;
        this.email = email;
        this.league = league;
        this.weeklyGroup = weeklyGroup;
        this.streak = streak;
        this.money = money;
    }

    // Getters
    public String getUid() {
        return this.uid;
    }

    public String getName() {
        return name;
    }

    public int getWeeklyExperience() {

        return weeklyExperience;
    }

    public int getLeague() {

        return league;
    }

    public int getWeeklyGroup() {
        return weeklyGroup;
    }

    public int getStreak() {
        return streak;
    }

    public String getEmail() {
        return email;
    }

    public int getMoney() {
        return this.money;
    }

    // Setters
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setWeeklyExperience(int weeklyExperience) {
        this.weeklyExperience = weeklyExperience;
    }

    public void setLeague(int level) {

        this.league = league;
    }

    public void setWeeklyGroup(int weeklyGroup) {
        this.weeklyGroup = weeklyGroup;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
