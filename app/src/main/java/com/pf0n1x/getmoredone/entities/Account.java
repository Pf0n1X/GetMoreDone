package com.pf0n1x.getmoredone.entities;

public class Account implements Comparable<Account> {

    // Data Members
    private String uid;
    private String name;
    private int weeklyExperience; // represents weekly experience
    private String email;
    private int league; // TODO: Move this to an enum representing a league/tier
    private int weeklyGroup;
    private int streak;
    private int money;
    private long lastActiveDate;
    private boolean hasStreakFreeze;
    private boolean hasWager;
    private int wagerStreak;

    // Constructors

    /*
       This empty constructor is needed for read operations from firebase.
    */
    public Account() {

    }

    public Account(String uid,
                   String name,
                   int weeklyExperience,
                   String email,
                   int league,
                   int weeklyGroup,
                   int streak,
                   int money,
                   long lastActiveDate,
                   boolean hasStreakFreeze,
                   boolean hasWager,
                   int wagerStreak) {
        this.uid = uid;
        this.name = name;
        this.weeklyExperience = weeklyExperience;
        this.email = email;
        this.league = league;
        this.weeklyGroup = weeklyGroup;
        this.streak = streak;
        this.money = money;
        this.lastActiveDate = lastActiveDate;
        this.hasStreakFreeze = hasStreakFreeze;
        this.hasWager = hasWager;
        this.wagerStreak = wagerStreak;
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

    public long getLastActiveDate() {
        return this.lastActiveDate;
    }

    public boolean getHasStreakFreeze() {
        return this.hasStreakFreeze;
    }

    public boolean getHasWager() {
        return this.hasWager;
    }

    public int getWagerStreak() {
        return this.wagerStreak;
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

    public void setLastActiveDate(long lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public void setHasStreakFreeze(boolean hasStreakFreeze) {
        this.hasStreakFreeze = hasStreakFreeze;
    }

    public void setHasWager(boolean hasWager) {
        this.hasWager = hasWager;
    }

    public void setWagerStreak(int wagerStreak) {
        this.wagerStreak = wagerStreak;
    }

    @Override
    public int compareTo(Account o) {
        if (this.getWeeklyExperience() > o.getWeeklyExperience()) {
            return -1;
        }
        if (this.getWeeklyExperience() == o.getWeeklyExperience()) {
            return 0;
        } else {
            return 1;
        }
    }
}
