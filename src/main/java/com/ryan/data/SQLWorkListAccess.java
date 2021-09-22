package com.ryan.data;

import com.ryan.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLWorkListAccess implements EmployeeWorkListAccess {

    @Override
    public List<Integer> getWorkList(Integer integer) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("SELECT application_id FROM employee_work_list WHERE employee_id = ?");
            statement.setInt(1, integer);
            ResultSet resultSet = statement.executeQuery();
            List<Integer> ids = new ArrayList<>();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("application_id"));
            }
            return ids;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get employee work list", e);
            return null;
        }
    }

    @Override
    public List<Integer> getEmployees(Integer integer) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("SELECT employee_id FROM employee_work_list WHERE application_id = ?");
            statement.setInt(1, integer);
            ResultSet resultSet = statement.executeQuery();
            List<Integer> ids = new ArrayList<>();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("employee_id"));
            }
            return ids;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get employees assigned to work list", e);
            return null;
        }
    }

    @Override
    public void addItemToList(Integer employee, Integer application) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("INSERT INTO employee_work_list (employee_id, application_id) VALUES (?, ?)");
            statement.setInt(1, employee);
            statement.setInt(2, application);
            statement.execute();
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to add item to employee work list", e);
        }
    }

    @Override
    public Integer getLeastAssigned() {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            ResultSet resultSet = c.createStatement().executeQuery("SELECT u.id, count(ewl.application_id) as amount FROM \"user\" u LEFT JOIN employee_work_list ewl on u.id = ewl.employee_id WHERE u.user_type = 2 group by u.id");
            int least = Integer.MAX_VALUE;
            int leastEmployee = -1;
            while (resultSet.next()) {
                int amount = resultSet.getInt("amount");
                if (amount <= least) {
                    int employee = resultSet.getInt("id");
                    if (employee != Context.getInstance().getCurrentUser().getId()) {
                        least = amount;
                        leastEmployee = employee;
                    }
                }
            }
            return leastEmployee;
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to get employee with the least assigned items", e);
            return null;
        }
    }

    @Override
    public void removeItemFromList(Integer employee, Integer application) {
        try (Connection c = Context.getInstance().getConnector().newConnection()) {
            PreparedStatement statement = c.prepareStatement("DELETE FROM employee_work_list WHERE employee_id = ? AND application_id = ?");
            statement.setInt(1, employee);
            statement.setInt(2, application);
            statement.execute();
        } catch (SQLException e) {
            Context.getInstance().getLogger().error("Failed to remove item from employee work list", e);
        }
    }

}
