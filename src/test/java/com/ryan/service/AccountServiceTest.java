package com.ryan.service;

import com.ryan.Context;
import com.ryan.data.TransactionDataAccess;
import com.ryan.data.UserDataAccess;
import com.ryan.models.Account;
import com.ryan.models.CheckingAccount;
import com.ryan.models.Client;
import com.ryan.models.Transaction;
import com.ryan.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.atomic.AtomicReference;
import static org.junit.Assert.*;

public class AccountServiceTest {

    @Test
    public void testDeposit() {
        AccountService accountService = new AccountService();
        Context context = Mockito.mock(Context.class);
        Logger logger = LogManager.getLogger(AccountServiceTest.class);
        Mockito.when(context.getLogger()).then(invocation -> logger);
        User user = new Client();
        user.setId(1);
        user.setCreditScore(20);
        UserDataAccess userDataAccess = Mockito.mock(UserDataAccess.class);
        Mockito.when(userDataAccess.getItem(1)).then(invocation -> user);
        Mockito.when(userDataAccess.saveItem(Mockito.any())).then(invocation -> 1);
        TransactionDataAccess dataAccess = Mockito.mock(TransactionDataAccess.class);
        AtomicReference<Transaction> created = new AtomicReference<>();
        Mockito.when(dataAccess.saveItem(Mockito.any())).then(invocation -> {
            created.set(invocation.getArgument(0));
            return 1;
        });

        Mockito.when(context.getUserDataAccess()).then(invocation -> userDataAccess);
        Mockito.when(context.getTransactionDataAccess()).then(invocation -> dataAccess);

        accountService.setContext(context);

        Account account = new CheckingAccount();
        account.setUser(1);
        account.setId(1);

        double amount = 1000;

        accountService.depositOrWithdraw(account, amount);

        assertEquals("Deposited amount does not match what we entered", amount, created.get().getAmount(), 2);

    }

}
