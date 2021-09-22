package com.ryan.screens.client;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.console.ConsoleOption;
import com.ryan.models.Account;
import com.ryan.models.Employee;
import com.ryan.models.Transaction;
import com.ryan.screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class TransferScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        List<Account> accounts = Context.getInstance().getAccountDataAccess().getAccountsForUser(Context.getInstance().getCurrentUser().getId());
        List<ConsoleOption> options = new ArrayList<>();
        for (Account account : accounts) {
            options.add(new ConsoleOption(account.getName() + " (" + account.getId() + ")", account.getName()));
        }
        options.add(new ConsoleOption("Go back", "back"));
        int chosen = Context.getInstance().getConsole().promptMenu("Chose account to transfer out of", options);
        if (chosen < accounts.size()) {
            Account account = accounts.get(chosen);
            while (true) {
                Integer into = Context.getInstance().getConsole().promptInt("Enter account id you would like to send money to or q to quit", new char[]{'q'});
                if (into == null) {
                    return ApplicationState.TRANSFER;
                }
                Account intoAccount = Context.getInstance().getAccountDataAccess().getItem(into);
                if (intoAccount != null) {
                    while (true) {
                        Double amount = Context.getInstance().getConsole().promptDouble("Enter amount you would like to transfer or q to quit", 2, new char[]{'q'});
                        if (amount == null) {
                            return ApplicationState.TRANSFER;
                        }
                        if (Context.getInstance().getAccountService().transfer(account, intoAccount, amount)) {
                            System.out.println("Successfully transferred $" + amount + " to account " + intoAccount.getId());
                            System.out.println("You now have $" + account.calcBalance() + " in account " + account.getName());
                            boolean transferAgain = Context.getInstance().getConsole().promptYesNo("Would you like to transfer more money");
                            if (transferAgain) {
                                return ApplicationState.TRANSFER;
                            } else {
                                return Context.getInstance().getCurrentUser() instanceof Employee ? ApplicationState.CLIENT_EMPLOYEE : ApplicationState.CLIENT;
                            }
                        } else {
                            System.out.println("You cannot transfer more then you have in this account");
                            return ApplicationState.TRANSFER;
                        }
                    }
                } else {
                    System.out.println("Couldn't find that account");
                }
            }
        } else {
            return Context.getInstance().getCurrentUser() instanceof Employee ? ApplicationState.CLIENT_EMPLOYEE : ApplicationState.CLIENT;
        }
    }

    @Override
    public String getName() {
        return "transfer";
    }

}
