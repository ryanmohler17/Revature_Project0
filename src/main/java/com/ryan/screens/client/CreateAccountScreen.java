package com.ryan.screens.client;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.console.ConsoleOption;
import com.ryan.models.Account;
import com.ryan.models.CheckingAccount;
import com.ryan.models.Employee;
import com.ryan.models.SavingsAccount;
import com.ryan.screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class CreateAccountScreen implements Screen {
    @Override
    public ApplicationState startScreen() {
        System.out.println("Enter the information for the account you want to make, or q at any time to quit");
        String name = Context.getInstance().getConsole().promptString("Enter account name");
        if (name.equalsIgnoreCase("q")) {
            return Context.getInstance().getCurrentUser() instanceof Employee ? ApplicationState.CLIENT_EMPLOYEE : ApplicationState.CLIENT;
        }
        List<ConsoleOption> options = new ArrayList<>();
        options.add(new ConsoleOption("This is a checking account", "checking"));
        options.add(new ConsoleOption("This is a savings account", "savings"));
        options.add(new ConsoleOption("Cancel", "cancel", "quit", "q"));
        int chosen = Context.getInstance().getConsole().promptMenu("Enter account type", options);
        Account account;
        if (chosen == 0) {
            account = new SavingsAccount();
        } else if (chosen == 1) {
            account = new CheckingAccount();
        } else {
            return Context.getInstance().getCurrentUser() instanceof Employee ? ApplicationState.CLIENT_EMPLOYEE : ApplicationState.CLIENT;
        }
        account.setName(name);
        account.setUser(Context.getInstance().getCurrentUser().getId());
        Context.getInstance().getAccountDataAccess().saveItem(account);
        System.out.println("Successfully created account");
        return Context.getInstance().getCurrentUser() instanceof Employee ? ApplicationState.CLIENT_EMPLOYEE : ApplicationState.CLIENT;
    }

    @Override
    public String getName() {
        return "create account";
    }
}
