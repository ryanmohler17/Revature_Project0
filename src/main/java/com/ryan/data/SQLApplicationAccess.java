package com.ryan.data;

import com.ryan.Context;
import com.ryan.models.ApplicationStatus;
import com.ryan.models.CreditApplication;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLApplicationAccess implements ApplicationDataAccess {
    @Override
    public List<CreditApplication> getUserApplications(int userId) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("SELECT id, user_id, account_id, amount, interest, score, status, submitted FROM credit_application WHERE user_id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            List<CreditApplication> applications = new ArrayList<>();
            while (resultSet.next()) {
                applications.add(getApplicationFromResultSet(resultSet));
            }
            return applications;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get user applications", e);
            return null;
        }
    }

    private CreditApplication getApplicationFromResultSet(ResultSet resultSet) throws SQLException {
        CreditApplication creditApplication = new CreditApplication();
        creditApplication.setId(resultSet.getInt("id"));
        creditApplication.setUserId(resultSet.getInt("user_id"));
        creditApplication.setAccountId(resultSet.getInt("account_id"));
        creditApplication.setAmount(resultSet.getDouble("amount"));
        creditApplication.setInterest(resultSet.getDouble("interest"));
        creditApplication.setScore(resultSet.getDouble("score"));
        creditApplication.setStatus(ApplicationStatus.values()[resultSet.getInt("status")]);
        creditApplication.setSubmitted(resultSet.getDate("submitted"));
        return creditApplication;
    }

    @Override
    public Integer saveItem(CreditApplication item) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            int id = item.getId();
            if (item.getId() == -1) {
                PreparedStatement statement = c.prepareStatement("INSERT INTO credit_application (user_id, account_id, amount, interest, score, status, submitted) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                addParams(statement, item);
                statement.execute();
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                id = resultSet.getInt("id");
                item.setId(id);
            } else {
                PreparedStatement statement = c.prepareStatement("UPDATE credit_application SET user_id = ?, account_id = ?, amount = ?, interest = ?, score = ?, status = ?, submitted = ? WHERE id = ?");
                addParams(statement, item);
                statement.setInt(8, item.getId());
                statement.execute();
            }
            return id;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to save application", e);
            return null;
        }
    }

    private void addParams(PreparedStatement statement, CreditApplication item) throws SQLException {
        statement.setInt(1, item.getUserId());
        statement.setInt(2, item.getAccountId());
        statement.setDouble(3, item.getAmount());
        statement.setDouble(4, item.getInterest());
        statement.setDouble(5, item.getScore());
        statement.setInt(6, item.getStatus().ordinal());
        statement.setDate(7, new Date(item.getSubmitted().getTime()));
    }

    @Override
    public CreditApplication getItem(Integer item) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("SELECT id, user_id, account_id, amount, interest, score, status, submitted FROM credit_application WHERE id = ?");
            statement.setInt(1, item);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return getApplicationFromResultSet(resultSet);
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get application", e);
            return null;
        }
    }

    @Override
    public List<CreditApplication> getAllItems() {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            ResultSet resultSet = c.createStatement().executeQuery("SELECT id, user_id, account_id, amount, interest, score, status, submitted FROM credit_application");
            List<CreditApplication> applications = new ArrayList<>();
            while (resultSet.next()) {
                applications.add(getApplicationFromResultSet(resultSet));
            }
            return applications;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get all applications", e);
            return null;
        }
    }
}
