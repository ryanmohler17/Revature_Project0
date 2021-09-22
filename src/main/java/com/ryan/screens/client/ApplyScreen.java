package com.ryan.screens.client;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.console.ConsoleOption;
import com.ryan.models.Account;
import com.ryan.models.ApplicationStatus;
import com.ryan.models.CreditApplication;
import com.ryan.screens.Screen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApplyScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        while (true) {
            List<ConsoleOption> options = new ArrayList<>();
            List<Account> accounts = Context.getInstance().getAccountDataAccess().getAccountsForUser(Context.getInstance().getCurrentUser().getId());
            for (Account account : accounts) {
                options.add(new ConsoleOption(account.getName() + " (" + account.getId() + ")", account.getName()));
            }
            options.add(new ConsoleOption("Go back", "back"));
            int chosen = Context.getInstance().getConsole().promptMenu("Select account to use", options);
            if (chosen >= accounts.size()) {
                return ApplicationState.CREDIT;
            }

            Account account = accounts.get(chosen);

            Double amount = Context.getInstance().getConsole().promptDouble("Please enter the amount you would like to apply for or q to quit", 2, new char[]{'q'});
            if (amount == null) {
                return ApplicationState.CREDIT;
            }
            if (amount < 1) {
                System.out.println("You need to apply for a positive amount");
                continue;
            }
            CreditApplication creditApplication = new CreditApplication();
            creditApplication.setSubmitted(new Date());
            creditApplication.setAmount(amount);
            creditApplication.setScore(Context.getInstance().getCurrentUser().getCreditScore());
            creditApplication.setStatus(ApplicationStatus.PENDING);
            creditApplication.setAccountId(account.getId());
            creditApplication.setUserId(Context.getInstance().getCurrentUser().getId());

            Context.getInstance().getApplicationService().submitApplication(creditApplication);

            boolean again = Context.getInstance().getConsole().promptYesNo("Would you like to submit another application?");
            if (again) {
                return ApplicationState.APPLY;
            } else {
                return ApplicationState.CREDIT;
            }
        }
    }

    @Override
    public String getName() {
        return "apply";
    }

}
