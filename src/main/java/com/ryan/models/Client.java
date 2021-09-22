package com.ryan.models;

public class Client extends User {

    @Override
    void changePassword(String old, String password) {
        if (checkPassword(old)) {
            this.setPassword(password);
        } else {
            throw new UnsupportedOperationException("Cannot set password without providing valid old password");
        }
    }

    @Override
    public CreditRank getCreditRank() {
        double score = getCreditScore();
        CreditRank currentRank = CreditRank.GREAT;
        for (CreditRank rank : CreditRank.values()) {
            if (score >= rank.getAmount()) {
                currentRank = rank;
            }
        }
        return currentRank;
    }
}
