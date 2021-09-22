package com.ryan.screens.client;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.models.CreditApplication;
import com.ryan.screens.Screen;

import java.text.SimpleDateFormat;

public class ApplicationsScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        int i = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        for (CreditApplication creditApplication : Context.getInstance().getApplicationDataAccess().getUserApplications(Context.getInstance().getCurrentUser().getId())) {
            System.out.println(i + ": " + "$" + creditApplication.getAmount() + " Submitted on " + dateFormat.format(creditApplication.getSubmitted()) + " Is currently " + creditApplication.getStatus().name());
            i++;
        }
        Context.getInstance().getConsole().promptString("Press enter to continue");
        return ApplicationState.CREDIT;
    }

    @Override
    public String getName() {
        return "application";
    }

}
