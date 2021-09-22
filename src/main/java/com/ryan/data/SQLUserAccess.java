package com.ryan.data;

import com.ryan.Context;
import com.ryan.models.Client;
import com.ryan.models.Employee;
import com.ryan.models.EmployeeAccess;
import com.ryan.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLUserAccess implements UserDataAccess {
    @Override
    public Integer saveItem(User item) {
        try(Connection c = Context.getInstance().getConnector().newConnection()) {
            int id = item.getId();
            if (item.getId() == -1) {
                PreparedStatement statement = c.prepareStatement("INSERT INTO \"user\" (first_name, last_name, username, email_address, password, credit_score, user_type) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                addParams(statement, item);
                statement.execute();
                ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                id = keys.getInt("id");
                item.setId(id);
                if (item instanceof Employee) {
                    saveEmployee(c, (Employee) item);
                }
            } else {
                PreparedStatement statement = c.prepareStatement("UPDATE \"user\" SET first_name = ?, last_name = ?, username = ?, email_address = ?, password = ?, credit_score = ?, user_type = ? WHERE id = ?");
                addParams(statement, item);
                statement.setInt(8, item.getId());
                statement.execute();
                if (item instanceof Employee) {
                    updateEmployee(c, (Employee) item);
                }
            }
            return id;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to save user", e);
            return -1;
        }
    }

    private void saveEmployee(Connection c, Employee item) throws SQLException {
        for (EmployeeAccess access : item.getAccessList()) {
            PreparedStatement statement = c.prepareStatement("INSERT INTO employee_access (employee_id, access_type) VALUES (?, ?)");
            statement.setInt(1, item.getId());
            statement.setInt(2, access.ordinal());
            statement.execute();
        }
    }

    private void updateEmployee(Connection c, Employee item) throws SQLException {
        PreparedStatement statement = c.prepareStatement("DELETE FROM employee_access WHERE employee_id = ?");
        statement.setInt(1, item.getId());
        statement.execute();
        saveEmployee(c, item);
    }

    private void addParams(PreparedStatement statement, User item) throws SQLException {
        statement.setString(1, item.getFirstName());
        statement.setString(2, item.getLastName());
        statement.setString(3, item.getUserName());
        statement.setString(4, item.getEmailAddress());
        statement.setString(5, item.getPassword());
        statement.setDouble(6, item.getCreditScore());
        statement.setInt(7, item instanceof Client ? 1 : 2);
    }

    @Override
    public User getItem(Integer item) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("SELECT id, first_name, last_name, username, email_address, password, credit_score, user_type FROM \"user\" WHERE id = ?");
            statement.setInt(1, item);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return createUserFromResultSet(resultSet, c);
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get user by id", e);
            return null;
        }
    }

    private User createUserFromResultSet(ResultSet resultSet, Connection c) throws SQLException {
        User user;
        if (resultSet.getInt("user_type") == 1) {
            user = new Client();
        } else {
            user = new Employee();
            PreparedStatement s = c.prepareStatement("SELECT access_type FROM employee_access WHERE employee_id = ?");
            s.setInt(1, resultSet.getInt("id"));
            ResultSet accessSet = s.executeQuery();
            while (accessSet.next()) {
                ((Employee) user).permitAccess(EmployeeAccess.values()[accessSet.getInt("access_type")]);
            }
        }
        user.setId(resultSet.getInt("id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setUserName(resultSet.getString("username"));
        user.setEmailAddress(resultSet.getString("email_address"));
        user.setPassword(resultSet.getString("password"));
        user.setCreditScore(resultSet.getDouble("credit_score"));
        return user;
    }

    @Override
    public List<User> getAllItems() {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            List<User> users = new ArrayList<>();
            ResultSet resultSet = c.createStatement().executeQuery("SELECT id, first_name, last_name, username, email_address, password, credit_score, user_type FROM \"user\"");
            while (resultSet.next()) {
                users.add(createUserFromResultSet(resultSet, c));
            }
            return users;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get all users", e);
            return null;
        }
    }

    @Override
    public User getUserByUserName(String name) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("SELECT id, first_name, last_name, username, email_address, password, credit_score, user_type FROM \"user\" WHERE username = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createUserFromResultSet(resultSet, c);
            } else {
                return null;
            }
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get user by username", e);
            return null;
        }
    }
}
