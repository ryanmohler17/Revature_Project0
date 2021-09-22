package com.ryan.data;

import com.ryan.Context;
import com.ryan.models.Transaction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLTransactionAccess implements TransactionDataAccess {
    @Override
    public Integer saveItem(Transaction item) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            int id = item.getId();
            if (item.getId() == -1) {
                PreparedStatement statement = c.prepareStatement("INSERT INTO transaction (account_id, description, amount, \"where\", date) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                addParams(statement, item);
                statement.execute();
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                id = resultSet.getInt("id");
            } else {
                throw new UnsupportedOperationException("Cannot update transactions as of right now");
            }
            return id;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to save transaction", e);
            return null;
        }
    }

    private void addParams(PreparedStatement statement, Transaction item) throws SQLException {
        statement.setInt(1, item.getAccount());
        statement.setString(2, item.getDescription());
        statement.setDouble(3, item.getAmount());
        statement.setString(4, item.getWhere());
        statement.setDate(5, new Date(item.getDate().getTime()));
    }

    @Override
    public Transaction getItem(Integer item) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("SELECT id, account_id, description, amount, \"where\", date FROM transaction WHERE id = ?");
            statement.setInt(1, item);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return getTransactionFromResultSet(resultSet);
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get transaction by id", e);
            return null;
        }
    }

    private Transaction getTransactionFromResultSet(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getInt("id"));
        transaction.setAccount(resultSet.getInt("account_id"));
        transaction.setDescription(resultSet.getString("description"));
        transaction.setAmount(resultSet.getDouble("amount"));
        transaction.setWhere(resultSet.getString("where"));
        transaction.setDate(resultSet.getDate("date"));
        return transaction;
    }

    @Override
    public List<Transaction> getAllItems() {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            ResultSet resultSet =  c.createStatement().executeQuery("SELECT id, account_id, description, amount, \"where\", date FROM transaction");
            List<Transaction> transactions = new ArrayList<>();
            while (resultSet.next()) {
                transactions.add(getTransactionFromResultSet(resultSet));
            }
            return transactions;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get all transactions", e);
            return null;
        }
    }

    @Override
    public List<Transaction> getAccountTransactions(int accountId) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("SELECT id, account_id, description, amount, \"where\", date FROM transaction WHERE account_id = ?");
            statement.setInt(1, accountId);
            ResultSet resultSet = statement.executeQuery();
            List<Transaction> transactions = new ArrayList<>();
            while (resultSet.next()) {
                transactions.add(getTransactionFromResultSet(resultSet));
            }
            return transactions;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get account transactions", e);
            return null;
        }
    }
}
