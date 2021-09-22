package com.ryan.screens.client;

import com.ryan.ApplicationState;
import com.ryan.Context;
import com.ryan.console.ConsoleOption;
import com.ryan.models.CreditRank;
import com.ryan.models.Employee;
import com.ryan.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class CreditScreen implements Screen {

    @Override
    public ApplicationState startScreen() {
        Timer timer = new Timer();
        AtomicInteger pos = new AtomicInteger(0);
        if (!Context.getInstance().getCurrentUser().isDeterminedScore()) {
            System.out.print("\rHang on while we determine your credit score.....");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    StringBuilder progressBuilder = new StringBuilder();
                    int display = pos.incrementAndGet();
                    for (int i = 0; i < display - 1; i++) {
                        progressBuilder.append('.');
                    }
                    progressBuilder.append('*');
                    for (int i = display + 1; i <= 5; i++) {
                        progressBuilder.append('.');
                    }
                    if (display == 5) {
                        pos.set(0);
                    }
                    System.out.print("\rHang on while we determine your credit score" + progressBuilder);
                }
            }, 1000, 1000);

            CreditRank rank = Context.getInstance().getCurrentUser().getCreditRank();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timer.cancel();
            StringBuilder spaceBuilder = new StringBuilder();
            for (int i = 0 ; i < 50; i++) {
                spaceBuilder.append(' ');
            }
            System.out.println("\rYour rank is " + rank.name().toLowerCase(Locale.ROOT) + " with a score of " + Context.getInstance().getCurrentUser().getCreditScore() + spaceBuilder);
            Context.getInstance().getCurrentUser().setDeterminedScore(true);
            Context.getInstance().setCreditRank(rank);
        }
        List<ConsoleOption> options = new ArrayList<>();
        options.add(new ConsoleOption("View Applications", "view"));
        options.add(new ConsoleOption("Apply for credit", "apply"));
        options.add(new ConsoleOption("Go back", "back"));
        int chosen = Context.getInstance().getConsole().promptMenu("Credit menu", options);
        if (chosen == 0) {
            return ApplicationState.APPLICATIONS;
        } else if (chosen == 1) {
            return ApplicationState.APPLY;
        } else {
            return Context.getInstance().getCurrentUser() instanceof Employee ? ApplicationState.CLIENT_EMPLOYEE : ApplicationState.CLIENT;
        }
    }

    @Override
    public String getName() {
        return "credit";
    }

}
