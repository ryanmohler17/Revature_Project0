package com.ryan.data;

import com.ryan.models.CreditApplication;

import java.util.List;

public interface ApplicationDataAccess extends DataAccess<CreditApplication, Integer> {

    List<CreditApplication> getUserApplications(int userId);

}
