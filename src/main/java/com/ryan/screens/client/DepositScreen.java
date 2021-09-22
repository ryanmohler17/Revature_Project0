package com.ryan.screens.client;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.screens.Screen;

public class DepositScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        System.out.println("Depositing into account " + Context.getInstance().getAccount().getName());
        while (true) {
            double amount = Context.getInstance().getConsole().promptDouble("Enter amount to deposit", 2);
            if (amount > 0) {
                Context.getInstance().getAccountService().depositOrWithdraw(Context.getInstance().getAccount(), amount);
                break;
            }
            System.out.println("Please only enter positive numbers");
        }

        System.out.println("New account balance: " + Context.getInstance().getAccount().calcBalance());
        boolean depositAgain = Context.getInstance().getConsole().promptYesNo("Do you want to do another deposit into this account?");
        if (depositAgain) {
            return ApplicationState.DEPOSIT;
        } else {
            return ApplicationState.ACCOUNT;
        }
    }

    @Override
    public String getName() {
        return "deposit";
    }

}
