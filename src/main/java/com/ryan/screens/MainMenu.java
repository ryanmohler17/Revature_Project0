package com.ryan.screens;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.console.ConsoleOption;

import java.util.ArrayList;
import java.util.List;

public class MainMenu implements Screen {

    @Override
    public ApplicationState startScreen() {
        List<ConsoleOption> options = new ArrayList<>();
        options.add(new ConsoleOption("Login to account", "login"));
        options.add(new ConsoleOption("Exit", "exit"));
        int choice = Context.getInstance().getConsole().promptMenu("Main Menu", options);
        if (choice == 0) {
            return ApplicationState.LOGIN;
        } else {
            return ApplicationState.EXIT;
        }
    }

    @Override
    public String getName() {
        return "main";
    }

}
