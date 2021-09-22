package com.ryan.screens;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.models.Employee;
import com.ryan.models.User;

public class LoginScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        System.out.println("Login");
        String username = Context.getInstance().getConsole().promptString("Username: ");
        String password = Context.getInstance().getConsole().promptPassword("Password: ");

        User user = Context.getInstance().getUserDataAccess().getUserByUserName(username);
        if (user != null && user.checkPassword(password)) {
            Context.getInstance().setCurrentUser(user);
            if (user instanceof Employee) {
                return ApplicationState.EMPLOYEE;
            } else {
                return ApplicationState.CLIENT;
            }
        } else {
            System.out.println("Invalid username/password");
            return startScreen();
        }
    }

    @Override
    public String getName() {
        return "login";
    }

}
