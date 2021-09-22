package com.ryan.data;

import com.ryan.models.Transaction;

import java.util.List;

public interface TransactionDataAccess extends DataAccess<Transaction, Integer> {

    List<Transaction> getAccountTransactions(int accountId);

}
