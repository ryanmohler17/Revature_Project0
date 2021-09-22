package com.ryan.screens.client;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.screens.Screen;

public class WithdrawScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        System.out.println("Withdrawing from account " + Context.getInstance().getAccount().getName());
        while (true) {
            double amount = Context.getInstance().getConsole().promptDouble("Enter amount to withdraw", 2);
            if (amount > 0) {
                if (Context.getInstance().getAccountService().depositOrWithdraw(Context.getInstance().getAccount(), -amount)) {
                    break;
                } else {
                    System.out.println("You cannot withdraw more then you have in this account");
                }
            }
            System.out.println("Please only enter positive numbers");
        }

        System.out.println("New account balance: " + Context.getInstance().getAccount().calcBalance());
        boolean depositAgain = Context.getInstance().getConsole().promptYesNo("Do you want to do another withdraw from this account?");
        if (depositAgain) {
            return ApplicationState.WITHDRAW;
        } else {
            return ApplicationState.ACCOUNT;
        }
    }

    @Override
    public String getName() {
        return "withdraw";
    }

}
