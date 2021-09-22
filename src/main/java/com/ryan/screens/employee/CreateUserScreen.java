package com.ryan.screens.employee;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.console.ConsoleOption;
import com.ryan.models.Client;
import com.ryan.models.Employee;
import com.ryan.models.EmployeeAccess;
import com.ryan.models.User;
import com.ryan.screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class CreateUserScreen implements Screen {
    @Override
    public ApplicationState startScreen() {
        System.out.println("Enter the information for the user you want to create, or q at any time to exit");
        String first = Context.getInstance().getConsole().promptString("First Name");
        if (first.equalsIgnoreCase("q")) {
            return ApplicationState.EMPLOYEE;
        }
        String last = Context.getInstance().getConsole().promptString("Last Name");
        if (last.equalsIgnoreCase("q")) {
            return ApplicationState.EMPLOYEE;
        }
        String email = Context.getInstance().getConsole().promptString("Email");
        if (email.equalsIgnoreCase("q")) {
            return ApplicationState.EMPLOYEE;
        }
        String userName = Context.getInstance().getConsole().promptString("User name");
        if (userName.equalsIgnoreCase("q")) {
            return ApplicationState.EMPLOYEE;
        }
        String password;
        while (true) {
            password = Context.getInstance().getConsole().promptPassword("Password");
            if (password.equalsIgnoreCase("q")) {
                return ApplicationState.EMPLOYEE;
            }
            String repeat = Context.getInstance().getConsole().promptPassword("Repeat password");
            if (repeat.equalsIgnoreCase("q")) {
                return ApplicationState.EMPLOYEE;
            }
            if (password.equals(repeat)) {
                break;
            } else {
                System.out.println("Passwords don't match");
            }
        }
        List<ConsoleOption> options = new ArrayList<>();
        if (((Employee) Context.getInstance().getCurrentUser()).hasAccess(EmployeeAccess.CREATE_EMPLOYEES)) {
            options.add(new ConsoleOption("This user is an employee", "employee"));
        }
        if (((Employee) Context.getInstance().getCurrentUser()).hasAccess(EmployeeAccess.CREATE_USERS)) {
            options.add(new ConsoleOption("This user is a client", "client"));
        }
        options.add(new ConsoleOption("Cancel user creation", "cancel", "q", "quit"));
        int chosen = Context.getInstance().getConsole().promptMenu("What type of user is this", options);
        ConsoleOption option = options.get(chosen);
        User user = null;
        if (option.getAlias(0).equals("cancel")) {
            return ApplicationState.EMPLOYEE;
        }
        if (option.getAlias(0).equals("employee")) {
            user = new Employee();
            for (EmployeeAccess access : EmployeeAccess.values()) {
                Boolean can = Context.getInstance().getConsole().promptYesNo("Can this employee " + access.name(), new char[] {'q'});
                if (can == null) {
                    return ApplicationState.EMPLOYEE;
                }
                if (can) {
                    ((Employee) user).permitAccess(access);
                }
            }
        } else if (option.getAlias(0).equals("client")) {
            user = new Client();
        }
        if (user == null) {
            Context.getInstance().getLogger().warn("A null user was created, this shouldn't be possible. Option alias: " + option.getAlias(0));
            System.out.println("An issue was encountered when creating the user. Please report this");
            return ApplicationState.EMPLOYEE;
        }
        user.setFirstName(first);
        user.setLastName(last);
        user.setEmailAddress(email);
        user.setUserName(userName);
        user.setPassword(password);

        Context.getInstance().getUserService().addUser(user);
        System.out.println("User:");
        System.out.println("\tFirst Name: " + first);
        System.out.println("\tLast Name: " + last);
        System.out.println("\tEmail: " + email);
        System.out.println("\tUser Name: " + userName);
        boolean shouldContinue = Context.getInstance().getConsole().promptYesNo("Confirm create user");
        if (!shouldContinue) {
            return ApplicationState.CREATE_USER;
        }
        System.out.println("Successfully added user");
        boolean again = Context.getInstance().getConsole().promptYesNo("Do you want to create another user?");
        if (again) {
            return ApplicationState.CREATE_USER;
        } else {
            return ApplicationState.EMPLOYEE;
        }
    }

    @Override
    public String getName() {
        return "create user";
    }
}
