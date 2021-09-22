package com.ryan.data;

import com.ryan.models.Account;
import com.ryan.models.CheckingAccount;
import com.ryan.models.SavingsAccount;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileAccountAccess implements AccountDataAccess {

    List<Account> accountCache = new ArrayList<>();
    private String accountsFile = "data/accounts.csv";

    @Override
    public void init() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(accountsFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                int type = Integer.parseInt(split[3]);
                Account account;
                if (type == 1) {
                    account = new CheckingAccount();
                } else {
                    account = new SavingsAccount();
                }
                account.setId(Integer.parseInt(split[0]));
                account.setUser(Integer.parseInt(split[1]));
                account.setName(split[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String formatAccount(Account account, int type) {
        return account.getId() + "," + account.getUser() + "," + removeCommas(account.getName()) + "," + type + "\n";
    }

    @Override
    public Integer saveItem(Account item) {
        int id = accountCache.size();
        item.setId(id);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFile, true));
            int type = item instanceof CheckingAccount ? 1: 2;
            writer.append(formatAccount(item, type));
            writer.flush();
            accountCache.add(item);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    public Account getItem(Integer item) {
        return accountCache.get(item);
    }

    @Override
    public List<Account> getAccountsForUser(int userId) {
        return accountCache.stream().filter(account -> account.getUser() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Account> getAllItems() {
        return accountCache;
    }

    private String removeCommas(String item) {
        return item.replaceAll(",", "");
    }

}
