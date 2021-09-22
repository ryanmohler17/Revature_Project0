package com.ryan.screens.employee;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.console.ConsoleOption;
import com.ryan.models.Employee;
import com.ryan.models.EmployeeAccess;
import com.ryan.screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class EmployeeScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        List<ConsoleOption> options = new ArrayList<>();
        if (((Employee) Context.getInstance().getCurrentUser()).hasAccess(EmployeeAccess.CREATE_USERS) || ((Employee) Context.getInstance().getCurrentUser()).hasAccess(EmployeeAccess.CREATE_EMPLOYEES)) {
            options.add(new ConsoleOption("Create user", "create", "user"));
        }
        if (((Employee) Context.getInstance().getCurrentUser()).hasAccess(EmployeeAccess.REVIEW_CREDIT)) {
            options.add(new ConsoleOption("Review credit applications", "review", "credit"));
        }
        options.add(new ConsoleOption("Go to client menu", "client"));
        options.add(new ConsoleOption("Logout", "logout"));
        int chosen = Context.getInstance().getConsole().promptMenu("Employee Menu", options);
        ConsoleOption option = options.get(chosen);
        if (option.getAlias(0).equals("create")) {
            return ApplicationState.CREATE_USER;
        } else if (option.getAlias(0).equals("review")) {
            return ApplicationState.REVIEW_APPLICATION;
        } else if (option.getAlias(0).equals("client")) {
            return ApplicationState.CLIENT_EMPLOYEE;
        } else if (option.getAlias(0).equals("logout")) {
            return ApplicationState.MAIN_MENU;
        }
        return ApplicationState.EXIT;
    }

    @Override
    public String getName() {
        return "employee";
    }

}
