package com.cricket.models;

public class User {
    private String name;
    private int userId;
    private String email;
    private String password;
    private String gender;
    private int mobileNumber;
    private String role;
    private  String skills;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(int mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public User(String name, int userId, String email, String password, String gender, int mobileNumber, String role, String skills) {
        this.name = name;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.role = role;
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "UserRegistration{" +
                "name='" + name + '\'' +
                ", userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", mobileNumber=" + mobileNumber +
                ", role='" + role + '\'' +
                ", skills='" + skills + '\'' +
                '}';
    }
}
