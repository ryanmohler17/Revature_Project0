package com.ryan.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Employee extends User {

    private Set<EmployeeAccess> accessList = new HashSet<>();

    @Override
    void changePassword(String old, String password) {
        throw new UnsupportedOperationException("Cannot change password as an employee");
    }

    public boolean hasAccess(EmployeeAccess access) {
        return accessList.contains(access);
    }

    public void permitAccess(EmployeeAccess access) {
        accessList.add(access);
    }

    public void denyAccess(EmployeeAccess access) {
        accessList.remove(access);
    }

    public Set<EmployeeAccess> getAccessList() {
        return accessList;
    }

    @Override
    public CreditRank getCreditRank() {
        double score = getCreditScore() + 10;
        CreditRank currentRank = CreditRank.GREAT;
        for (CreditRank rank : CreditRank.values()) {
            if (score >= rank.getAmount()) {
                currentRank = rank;
            }
        }
        if (currentRank.equals(CreditRank.POUR)) {
            currentRank = CreditRank.BAD;
        }
        return currentRank;
    }

}
