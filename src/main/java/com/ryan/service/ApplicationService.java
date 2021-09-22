package com.ryan.service;

import com.ryan.Context;
import com.ryan.models.Account;
import com.ryan.models.ApplicationStatus;
import com.ryan.models.CreditApplication;
import com.ryan.models.CreditRank;
import com.ryan.models.Transaction;
import com.ryan.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationService {

    /**
     * Submits an application, and then approves it, disapproves it, or submits it for review depending on the amount selected and the users credit score.
     *
     * @param application The application to submit.
     */
    public void submitApplication(CreditApplication application) {
        User user = Context.getInstance().getUserDataAccess().getItem(application.getUserId());
        CreditRank rank = user.getCreditRank();
        boolean assign = false;
        if (application.getAmount() > rank.getRequestable()) {
            application.setStatus(ApplicationStatus.DENIED);
        } else {
            if (rank.equals(CreditRank.GREAT) || rank.equals(CreditRank.GOOD)) {
                handleApprovedApplication(application);
            } else if (rank.equals(CreditRank.POUR)) {
                application.setStatus(ApplicationStatus.DENIED);
            } else {
                assign = true;
            }
        }
        Context.getInstance().getApplicationDataAccess().saveItem(application);
        if (assign) {
            int least = Context.getInstance().getWorkListAccess().getLeastAssigned();
            Context.getInstance().getWorkListAccess().addItemToList(least, application.getId());
        }
        Context.getInstance().getLogger().info("User with id of " + user.getId() + " submitted an application for " + application.getAmount() + ". This application was set to state of " + application.getStatus().name() + ". The id is " + application.getId());
    }

    /**
     * Approves an application, and deposits the money into the users account.
     * Also removes the application from any employee work lists
     *
     * @param application The application to remove
     */
    public void handleApprovedApplication(CreditApplication application) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        User user = Context.getInstance().getUserDataAccess().getItem(application.getUserId());
        CreditRank rank = user.getCreditRank();
        application.setStatus(ApplicationStatus.APPROVED);
        application.setInterest(rank.getInterest());
        Context.getInstance().getApplicationDataAccess().saveItem(application);
        Transaction transaction = new Transaction();
        transaction.setAccount(application.getAccountId());
        transaction.setAmount(application.getAmount());
        transaction.setDescription("Credit approved on " + dateFormat.format(new Date()));
        transaction.setWhere("Ryan Bank - Credit");
        transaction.setDate(new Date());
        Context.getInstance().getTransactionDataAccess().saveItem(transaction);

        Context.getInstance().getLogger().info("Application with id of " + application.getId() + " was approved with an amount of " + application.getAmount());

        for (Integer emp : Context.getInstance().getWorkListAccess().getEmployees(application.getId())) {
            Context.getInstance().getWorkListAccess().removeItemFromList(emp, application.getId());
        }
    }

    /**
     * Denies an application
     * Also removes the application from any employee work lists
     *
     * @param application The application to deny
     */
    public void denyApplication(CreditApplication application) {
        application.setStatus(ApplicationStatus.DENIED);
        Context.getInstance().getApplicationDataAccess().saveItem(application);

        Context.getInstance().getLogger().info("Application with id of " + application.getId() + " was denied");

        for (Integer emp : Context.getInstance().getWorkListAccess().getEmployees(application.getId())) {
            Context.getInstance().getWorkListAccess().removeItemFromList(emp, application.getId());
        }
    }

}
