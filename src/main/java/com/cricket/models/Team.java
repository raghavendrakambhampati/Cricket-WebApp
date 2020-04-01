package com.cricket.models;

public class Team {
    private String teamName;
    private int userId;
    private String city;

    public Team(){

    }
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Team(String teamName, int userId, String city) {
        this.teamName = teamName;
        this.userId = userId;
        this.city = city;
    }

    @Override
    public String toString() {
        return "TeamRegistration{" +
                "teamName='" + teamName + '\'' +
                ", userId=" + userId +
                ", city='" + city + '\'' +
                '}';
    }
}
