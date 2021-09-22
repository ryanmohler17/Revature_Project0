package com.ryan.data;

import java.util.List;

public interface EmployeeWorkListAccess {

    List<Integer> getWorkList(Integer integer);
    List<Integer> getEmployees(Integer integer);
    void addItemToList(Integer employee, Integer application);
    Integer getLeastAssigned();
    void removeItemFromList(Integer employee, Integer application);

}
