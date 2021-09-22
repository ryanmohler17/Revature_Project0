package com.ryan.screens.client;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.console.ConsoleOption;
import com.ryan.screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class ClientScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        List<ConsoleOption> options = new ArrayList<>();
        options.add(new ConsoleOption("View and maintain accounts", "accounts", "account", "view"));
        options.add(new ConsoleOption("Transfer money", "transfer"));
        options.add(new ConsoleOption("Apply for credit", "credit", "apply"));
        if (Context.getInstance().getState().equals(ApplicationState.CLIENT)) {
            options.add(new ConsoleOption("Logout", "logout"));
        } else {
            options.add(new ConsoleOption("Return to employee menu", "return"));
        }
        int option = Context.getInstance().getConsole().promptMenu("Client Menu", options);
        switch (option) {
            case 0:
                return ApplicationState.MANAGE_ACCOUNTS;
            case 1:
                return ApplicationState.TRANSFER;
            case 2:
                return ApplicationState.CREDIT;
            case 3:
                if (Context.getInstance().getState().equals(ApplicationState.CLIENT)) {
                    Context.getInstance().setCurrentUser(null);
                    return ApplicationState.MAIN_MENU;
                } else {
                    return ApplicationState.EMPLOYEE;
                }
        }
        return ApplicationState.EXIT;
    }

    @Override
    public String getName() {
        return "user";
    }

}
