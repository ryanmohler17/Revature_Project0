package com.ryan.models;

import com.ryan.Context;

public class CheckingAccount extends Account {

    @Override
    public double calcBalance() {
        double bal = 0;
        for (Transaction transaction : Context.getInstance().getTransactionDataAccess().getAccountTransactions(getId())) {
            bal += transaction.getAmount();
        }
        return bal;
    }

}
