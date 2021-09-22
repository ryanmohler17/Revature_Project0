package com.ryan.screens.client;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.console.ConsoleOption;
import com.ryan.models.CheckingAccount;
import com.ryan.screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class ManageAccountScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        List<ConsoleOption> options = new ArrayList<>();
        options.add(new ConsoleOption("View transaction history", "view"));
        if (Context.getInstance().getAccount() instanceof CheckingAccount) {
            options.add(new ConsoleOption("Deposit into account", "deposit"));
            options.add(new ConsoleOption("Withdraw from account", "withdraw"));
        }
        options.add(new ConsoleOption("Go back", "back"));
        int chosen = Context.getInstance().getConsole().promptMenu("Account " +
                Context.getInstance().getAccount().getName() + ". $" +
                Context.getInstance().getAccount().calcBalance(), options);

        if (chosen == 0) {
            return ApplicationState.TRANSACTION;
        }

        if (Context.getInstance().getAccount() instanceof CheckingAccount) {
            if (chosen == 1) {
                return ApplicationState.DEPOSIT;
            } else if (chosen == 2) {
                return ApplicationState.WITHDRAW;
            } else {
                return ApplicationState.MANAGE_ACCOUNTS;
            }
        } else {
            return ApplicationState.MANAGE_ACCOUNTS;
        }
    }

    @Override
    public String getName() {
        return "manage account";
    }

}
