package com.ryan.screens.client;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.models.Transaction;
import com.ryan.screens.Screen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionHistoryScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        List<Transaction> transactions = Context.getInstance().getTransactionDataAccess().getAccountTransactions(Context.getInstance().getAccount().getId());
        Collections.reverse(transactions);
        System.out.println("Account: " + Context.getInstance().getAccount().getName());
        System.out.println("Current balance: " + Context.getInstance().getAccount().calcBalance());
        System.out.println("-----------------------------------------------------");
        System.out.println();
        int current = 0;
        for (int i = 0; i < 5 && i < transactions.size(); i++) {
            Transaction transaction = transactions.get(current);
            printTransaction(transaction);
            current++;
        }

        while (current < transactions.size()) {
            String option = Context.getInstance().getConsole().promptString("Press enter to load more, or press q followed by enter to quit back to account menu");
            if (option.equalsIgnoreCase("q")) {
                break;
            }
            Transaction transaction = transactions.get(current);
            printTransaction(transaction);
            current++;
        }

        Context.getInstance().getConsole().promptString("Press enter to continue");

        System.out.println();

        return ApplicationState.ACCOUNT;
    }

    public void printTransaction(Transaction transaction) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println("Transaction " + transaction.getId() + " done on " + dateFormat.format(transaction.getDate()));
        System.out.println("---------------------------------------------");
        if (transaction.getAmount() > 0) {
            System.out.print("From: " + transaction.getWhere());
            System.out.print("\u001B[32m");
        } else {
            System.out.print("To: " + transaction.getWhere());
            System.out.print("\u001B[31m");
        }
        System.out.println(" Amount: " + Math.abs(transaction.getAmount()));
        System.out.print("\u001B[0m");
        System.out.println("Description: " + transaction.getDescription());
        System.out.println();
    }

    @Override
    public String getName() {
        return "history";
    }

}
