package com.ryan.models;

public enum CreditRank {

    POUR(0.0, 0, .10),
    BAD(25.0, 10000, .07),
    GOOD(50.0, 50000, .05),
    GREAT(75.0, 100000, .03);

    private final double amount;
    private final int requestable;
    private final double interest;

    CreditRank(double amount, int requestable, double interest) {
        this.amount = amount;
        this.requestable = requestable;
        this.interest = interest;
    }

    public int getRequestable() {
        return requestable;
    }

    public double getAmount() {
        return amount;
    }

    public double getInterest() {
        return interest;
    }

}
