package com.ryan.service;

import com.ryan.Context;
import com.ryan.Main;
import com.ryan.data.UserDataAccess;
import com.ryan.models.Account;
import com.ryan.models.Transaction;
import com.ryan.models.User;

import java.util.Date;

public class AccountService {

    private Context context;

    /**
     * This is only used for testing and can be safely ignored
     *
     * @param context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Deposits or withdraws money from the users account.
     * If the amount is < 0 then it withdraws, otherwise it deposits.
     * This creates a new transaction in order to handle the money.
     * This also affects the users credit score.
     * If the transaction was a success it adds the amount / 100 to the users credit score.
     * If it fails then it takes the amount it failed by and divides that by 100 and removes that amount from the credit score
     *
     * @param account The account to use.
     * @param amount The amount to use.
     * @return If the transaction was successful
     */
    public boolean depositOrWithdraw(Account account, double amount) {
        if (amount < 0) {
            if (Math.abs(amount) > account.calcBalance()) {
                double diff = Math.abs(amount) - account.calcBalance();
                double affect = diff / 1000d;
                User user = context.getUserDataAccess().getItem(account.getUser());
                user.setCreditScore(user.getCreditScore() - affect);
                context.getUserDataAccess().saveItem(user);
                return false;
            }
        }
        Transaction transaction = new Transaction();
        transaction.setAccount(account.getId());
        transaction.setAmount(amount);
        transaction.setDate(new Date());
        transaction.setWhere("Ryan Bank Application");
        if (amount > 0) {
            transaction.setDescription("Deposit into account");
            context.getLogger().info("$" + amount + " was deposited into account of id " + account.getId());
        } else {
            transaction.setDescription("Withdraw from account");
            context.getLogger().info("$" + amount + " was withdrawn from account of id " + account.getId());
        }

        double affect = Math.abs(amount) / 1000d;
        User user = context.getUserDataAccess().getItem(account.getUser());
        user.setCreditScore(user.getCreditScore() + affect);
        context.getUserDataAccess().saveItem(user);
        context.getTransactionDataAccess().saveItem(transaction);
        return true;
    }

    /**
     * Transfers money from one account to the other.
     * It does this by creating a transaction for both accounts.
     *
     * @param from The account to transfer out of
     * @param to The account to transfer into
     * @param amount The amount to transfer
     * @return If the transaction was successful
     */
    public boolean transfer(Account from, Account to, double amount) {
        if (amount > from.calcBalance()) {
            return false;
        }
        Transaction out = new Transaction();
        out.setDescription("Transfer to account " + to.getId());
        out.setWhere("Account " + to.getId());
        out.setAmount(-amount);
        out.setAccount(from.getId());
        out.setDate(new Date());
        Context.getInstance().getTransactionDataAccess().saveItem(out);

        Transaction in = new Transaction();
        in.setDescription("Transfer from " + from.getId());
        in.setWhere("Account " + from.getId());
        in.setAmount(amount);
        in.setAccount(to.getId());
        in.setDate(new Date());
        Context.getInstance().getTransactionDataAccess().saveItem(in);

        Context.getInstance().getLogger().info("$" + amount + " was transferred from account " + from.getId() + " into account " + to.getId());

        return true;
    }

}
