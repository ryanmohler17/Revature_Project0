package com.ryan.screens.client;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.console.ConsoleOption;
import com.ryan.models.Account;
import com.ryan.models.Employee;
import com.ryan.screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class AccountsScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        List<Account> accounts = Context.getInstance().getAccountDataAccess().getAccountsForUser(Context.getInstance().getCurrentUser().getId());
        List<ConsoleOption> options = new ArrayList<>();
        for (Account account : accounts) {
            options.add(new ConsoleOption(account.getName() + " (" + account.getId() + ")", account.getName()));
        }
        options.add(new ConsoleOption("Create account", "create"));
        options.add(new ConsoleOption("Go back", "back"));
        int chosen = Context.getInstance().getConsole().promptMenu("Select account", options);
        if (chosen < accounts.size()) {
            Account account = accounts.get(chosen);
            Context.getInstance().setAccount(account);
            return ApplicationState.ACCOUNT;
        } else  if (chosen == accounts.size()) {
            return ApplicationState.CREATE_ACCOUNT;
        } else {
            return Context.getInstance().getCurrentUser() instanceof Employee ? ApplicationState.CLIENT_EMPLOYEE : ApplicationState.CLIENT;
        }
    }

    @Override
    public String getName() {
        return "account";
    }

}
