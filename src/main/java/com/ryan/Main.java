package com.ryan;

import com.ryan.data.DataChecker;
import com.ryan.data.FileDataChecker;
import com.ryan.screens.MainMenu;
import com.ryan.screens.Screen;
import com.ryan.screens.client.AccountsScreen;
import com.ryan.screens.client.ApplicationsScreen;
import com.ryan.screens.client.ApplyScreen;
import com.ryan.screens.client.ClientScreen;
import com.ryan.screens.LoginScreen;
import com.ryan.screens.ScreenRegister;
import com.ryan.screens.client.CreateAccountScreen;
import com.ryan.screens.client.CreditScreen;
import com.ryan.screens.client.DepositScreen;
import com.ryan.screens.client.ManageAccountScreen;
import com.ryan.screens.client.TransactionHistoryScreen;
import com.ryan.screens.client.TransferScreen;
import com.ryan.screens.client.WithdrawScreen;
import com.ryan.screens.employee.CreateUserScreen;
import com.ryan.screens.employee.EmployeeScreen;
import com.ryan.screens.employee.ReviewApplicationScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Main {

    public static void main(String... args) {
        new Main().run();
    }

    private String version = "1.0";

    public void run() {
        startScreens();
    }

    public void startScreens() {
        Logger logger = LogManager.getLogger(Main.class);
        ScreenRegister screenRegister = new ScreenRegister();
        screenRegister.registerScreen(new ClientScreen());
        screenRegister.registerScreen(new LoginScreen());
        screenRegister.registerScreen(new EmployeeScreen());
        screenRegister.registerScreen(new MainMenu());
        screenRegister.registerScreen(new AccountsScreen());
        screenRegister.registerScreen(new DepositScreen());
        screenRegister.registerScreen(new ManageAccountScreen());
        screenRegister.registerScreen(new TransactionHistoryScreen());
        screenRegister.registerScreen(new TransferScreen());
        screenRegister.registerScreen(new WithdrawScreen());
        screenRegister.registerScreen(new CreateUserScreen());
        screenRegister.registerScreen(new ReviewApplicationScreen());
        screenRegister.registerScreen(new CreditScreen());
        screenRegister.registerScreen(new ApplyScreen());
        screenRegister.registerScreen(new ApplicationsScreen());
        screenRegister.registerScreen(new CreateAccountScreen());
        Context.createInstance(screenRegister, logger);

        handleState(screenRegister.getScreen("main").startScreen());
    }

    public void handleState(ApplicationState state) {
        Context.getInstance().changeState(state);
        String screen = state.getScreen();
        if (screen == null) {
            System.exit(0);
            return;
        }
        Screen screenObj = Context.getInstance().getScreenRegister().getScreen(screen);
        if (screenObj == null) {
            System.exit(1);
            Context.getInstance().getLogger().fatal("Encountered null screen for name " + screen);
            return;
        }
        handleState(screenObj.startScreen());
    }

}
