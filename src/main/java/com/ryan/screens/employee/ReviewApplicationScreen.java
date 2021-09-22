package com.ryan.screens.employee;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.console.ConsoleOption;
import com.ryan.models.Account;
import com.ryan.models.CreditApplication;
import com.ryan.models.Employee;
import com.ryan.models.Transaction;
import com.ryan.models.User;
import com.ryan.screens.Screen;
import com.ryan.screens.client.TransactionHistoryScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewApplicationScreen implements Screen {
    @Override
    public ApplicationState startScreen() {
        List<CreditApplication> applications = Context.getInstance().getWorkListAccess().getWorkList(Context.getInstance().getCurrentUser().getId())
                .stream().map(i -> Context.getInstance().getApplicationDataAccess().getItem(i)).collect(Collectors.toList());

        List<ConsoleOption> options = new ArrayList<>();
        for (CreditApplication application : applications) {
            User user = Context.getInstance().getUserDataAccess().getItem(application.getUserId());
            options.add(new ConsoleOption("Application " + application.getId() + " for $" + application.getAmount() +
                    " submitted by " + user.getFirstName() + " " + user.getLastName()));
        }
        options.add(new ConsoleOption("Go back", "back"));
        int chosen = Context.getInstance().getConsole().promptMenu("Chose application to review", options);
        if (chosen < applications.size()) {
            CreditApplication application = applications.get(chosen);
            User user = Context.getInstance().getUserDataAccess().getItem(application.getUserId());
            while (true) {
                System.out.println("Credit application id: " + application.getId());
                System.out.println("Applied for $" + application.getAmount() + " into account id " + application.getAccountId());
                System.out.println("Credit score at time of submission: " + application.getScore() + "  Credit score now: " + user.getCreditScore());
                System.out.println("The user currently has " + user.getCreditRank().name() + " credit score");
                List<ConsoleOption> newOptions = new ArrayList<>();
                newOptions.add(new ConsoleOption("View recent transaction history", "history", "view"));
                newOptions.add(new ConsoleOption("Approve request", "approve"));
                newOptions.add(new ConsoleOption("Deny request", "deny"));
                newOptions.add(new ConsoleOption("Go back", "back"));
                int newChosen = Context.getInstance().getConsole().promptMenu("Chose option", newOptions);
                if (newChosen == 0) {
                    while (true) {
                        List<Account> accounts = Context.getInstance().getAccountDataAccess().getAccountsForUser(user.getId());
                        List<ConsoleOption> accountOptions = new ArrayList<>();
                        for (Account account : accounts) {
                            accountOptions.add(new ConsoleOption("Account " + account.getName() + " (" + account.getId() + ")"));
                        }
                        accountOptions.add(new ConsoleOption("Go back", "back"));
                        int accountChosen = Context.getInstance().getConsole().promptMenu("Select account to view transaction history for", accountOptions);
                        if (accountChosen < accounts.size()) {
                            System.out.println();
                            Account account = accounts.get(accountChosen);
                            List<Transaction> transactions = Context.getInstance().getTransactionDataAccess().getAccountTransactions(account.getId());
                            Collections.reverse(transactions);
                            int i = 0;
                            for (Transaction transaction : transactions) {
                                ((TransactionHistoryScreen)Context.getInstance().getScreenRegister().getScreen("history")).printTransaction(transaction);
                                if (i >= 4) {
                                    break;
                                }
                                i++;
                            }
                        } else {
                            break;
                        }
                        Context.getInstance().getConsole().promptString("Press enter to continue");
                    }
                } else if (newChosen == 1) {
                    boolean approve = Context.getInstance().getConsole().promptYesNo("Are you sure you want to approve this application");
                    if (approve) {
                        Context.getInstance().getApplicationService().handleApprovedApplication(application);
                        return ApplicationState.REVIEW_APPLICATION;
                    }
                } else if (newChosen == 2) {
                    boolean deny = Context.getInstance().getConsole().promptYesNo("Are you sure you want to deny this application");
                    if (deny) {
                        Context.getInstance().getApplicationService().denyApplication(application);
                        return ApplicationState.REVIEW_APPLICATION;
                    }
                } else {
                    return ApplicationState.REVIEW_APPLICATION;
                }
            }
        } else {
            return ApplicationState.EMPLOYEE;
        }
    }

    @Override
    public String getName() {
        return "review";
    }
}
