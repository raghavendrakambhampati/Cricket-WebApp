package com.cricket.models;

public class Match {
    private int matchId;
    private int firstInnings;
    private int secondInnings;
    private int result;
    private int manOfTheMatch;
    private int scoreDifference;

    public Match(int firstInnings, int secondInnings, int result, int manOfTheMatch, int scoreDifference) {
        this.firstInnings = firstInnings;
        this.secondInnings = secondInnings;
        this.result = result;
        this.manOfTheMatch = manOfTheMatch;
        this.scoreDifference = scoreDifference;
    }

    public Match(int matchId, int firstInnings, int secondInnings, int result, int manOfTheMatch, int scoreDifference) {
        this.matchId = matchId;
        this.firstInnings = firstInnings;
        this.secondInnings = secondInnings;
        this.result = result;
        this.manOfTheMatch = manOfTheMatch;
        this.scoreDifference = scoreDifference;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getFirstInnings() {
        return firstInnings;
    }

    public void setFirstInnings(int firstInnings) {
        this.firstInnings = firstInnings;
    }

    public int getSecondInnings() {
        return secondInnings;
    }

    public void setSecondInnings(int secondInnings) {
        this.secondInnings = secondInnings;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getManOfTheMatch() {
        return manOfTheMatch;
    }

    public void setManOfTheMatch(int manOfTheMatch) {
        this.manOfTheMatch = manOfTheMatch;
    }

    public int getScoreDifference() {
        return scoreDifference;
    }

    public void setScoreDifference(int scoreDifference) {
        this.scoreDifference = scoreDifference;
    }
}
