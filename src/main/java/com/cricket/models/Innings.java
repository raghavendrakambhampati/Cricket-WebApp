package com.cricket.models;

public class Innings {
    private int id;
    private int teamId;
    private int score;
    private int wickets;

    public Innings(int teamId, int score, int wickets) {
        this.teamId = teamId;
        this.score = score;
        this.wickets = wickets;
    }

    public Innings(int id, int teamId, int score, int wickets) {
        this.id = id;
        this.teamId = teamId;
        this.score = score;
        this.wickets = wickets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }
}
