package com.ryan.data;

import com.ryan.Context;
import com.ryan.models.Account;
import com.ryan.models.CheckingAccount;
import com.ryan.models.SavingsAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLAccountAccess implements AccountDataAccess {
    @Override
    public List<Account> getAccountsForUser(int userId) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("SELECT id, user_id, name, account_type FROM user_account WHERE user_id = ?");
            statement.setInt(1, userId);
            List<Account> accounts = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accounts.add(getAccountFromResultSet(resultSet));
            }
            return accounts;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get user accounts from sql", e);
            return null;
        }
    }

    private Account getAccountFromResultSet(ResultSet resultSet) throws SQLException {
        Account account;
        if (resultSet.getInt("account_type") == 1) {
            account = new CheckingAccount();
        } else {
            account = new SavingsAccount();
        }
        account.setId(resultSet.getInt("id"));
        account.setUser(resultSet.getInt("user_id"));
        account.setName(resultSet.getString("name"));
        return account;
    }

    @Override
    public Integer saveItem(Account item) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            int id = item.getId();
            if (item.getId() == -1) {
                PreparedStatement statement = c.prepareStatement("INSERT INTO user_account (user_id, name, account_type) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, item.getUser());
                statement.setString(2, item.getName());
                statement.setInt(3, item instanceof CheckingAccount ? 1 : 2);
                statement.execute();
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                id = resultSet.getInt("id");
                item.setId(id);
            } else {
                PreparedStatement statement = c.prepareStatement("UPDATE user_account SET user_id = ?, name = ?, account_type = ? WHERE id = ?");
                statement.setInt(1, item.getUser());
                statement.setString(2, item.getName());
                statement.setInt(3, item instanceof CheckingAccount ? 1 : 2);
                statement.setInt(4, item.getId());
                statement.execute();
            }
            return id;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to add or update account to database", e);
            return null;
        }
    }

    @Override
    public Account getItem(Integer item) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("SELECT id, user_id, name, account_type FROM user_account WHERE id = ?");
            statement.setInt(1, item);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return getAccountFromResultSet(resultSet);
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get account from id", e);
            return null;
        }
    }

    @Override
    public List<Account> getAllItems() {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            ResultSet resultSet =  c.createStatement().executeQuery("SELECT id, user_id, name, account_type FROM user_account ");
            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                accounts.add(getAccountFromResultSet(resultSet));
            }
            return accounts;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get all accounts", e);
            return null;
        }
    }
}
