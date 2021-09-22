package com.ryan.models;

public abstract class User {

    private int id = -1;
    private String firstName;
    private String lastName;
    private String userName;
    private String emailAddress;
    private String password;
    private double creditScore;
    private boolean determinedScore = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    abstract void changePassword(String old, String password);

    public double getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(double creditScore) {
        this.creditScore = creditScore;
    }

    public boolean isDeterminedScore() {
        return determinedScore;
    }

    public void setDeterminedScore(boolean determinedScore) {
        this.determinedScore = determinedScore;
    }

    public abstract CreditRank getCreditRank();
}
