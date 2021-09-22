package com.ryan.data;

import com.ryan.models.Account;

import java.util.List;

public interface AccountDataAccess extends DataAccess<Account, Integer> {

    List<Account> getAccountsForUser(int userId);

}
