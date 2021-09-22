package com.ryan.service;

import com.ryan.Context;
import com.ryan.models.Account;
import com.ryan.models.CheckingAccount;
import com.ryan.models.Client;
import com.ryan.models.User;

public class UserService {

    /**
     * Adds a user and creates a default checking account
     *
     * @param user The user object to add
     */
    public void addUser(User user) {
        if (user instanceof Client) {
            user.setCreditScore(40);
        } else {
            user.setCreditScore(50);
        }
        Context.getInstance().getUserDataAccess().saveItem(user);
        Account account = new CheckingAccount();
        account.setUser(user.getId());
        account.setName("Checking");
        Context.getInstance().getAccountDataAccess().saveItem(account);

        Context.getInstance().getLogger().info("Created user with id of " + user.getId() + " with username of " + user.getUserName());
    }

}
