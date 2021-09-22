package com.ryan.models;

import com.ryan.Context;

public class SavingsAccount extends Account {

    public double calcInterest(double percent) {
        double bal = calcBalance();
        double added = bal * percent;
        return added;
    }

    @Override
    public double calcBalance() {
        double bal = 0;
        for (Transaction transaction : Context.getInstance().getTransactionDataAccess().getAccountTransactions(getId())) {
            bal += transaction.getAmount();
        }
        return bal;
    }
}
