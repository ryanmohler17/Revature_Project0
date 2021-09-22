package com.ryan;

import com.ryan.console.JavaConsole;
import com.ryan.console.SystemConsole;
import com.ryan.data.AccountDataAccess;
import com.ryan.data.ApplicationDataAccess;
import com.ryan.data.DBConnector;
import com.ryan.data.EmployeeWorkListAccess;
import com.ryan.data.FileAccountAccess;
import com.ryan.data.FileApplicationAccess;
import com.ryan.data.FileTransactionAccess;
import com.ryan.data.FileUserAccess;
import com.ryan.data.SQLAccountAccess;
import com.ryan.data.SQLApplicationAccess;
import com.ryan.data.SQLTransactionAccess;
import com.ryan.data.SQLUserAccess;
import com.ryan.data.SQLWorkListAccess;
import com.ryan.data.TransactionDataAccess;
import com.ryan.data.UserDataAccess;
import com.ryan.models.Account;
import com.ryan.models.CreditRank;
import com.ryan.models.User;
import com.ryan.screens.ScreenRegister;
import com.ryan.service.AccountService;
import com.ryan.service.ApplicationService;
import com.ryan.service.UserService;
import org.apache.logging.log4j.Logger;

public class Context {

    private static Context INS = null;

    private JavaConsole console;
    private UserDataAccess userDataAccess;
    private TransactionDataAccess transactionDataAccess;
    private AccountDataAccess accountDataAccess;
    private ApplicationDataAccess applicationDataAccess;
    private EmployeeWorkListAccess workListAccess;
    private ScreenRegister screenRegister;
    private ApplicationState state = ApplicationState.MAIN_MENU;
    private Logger logger;
    private User currentUser;
    private Account account;
    private AccountService accountService;
    private ApplicationService applicationService;
    private UserService userService;
    private CreditRank creditRank;
    private DBConnector connector;

    private Context(ScreenRegister screenRegister, Logger logger) {
        this.connector = new DBConnector();
        console = new SystemConsole();
        userDataAccess = new SQLUserAccess();
        userDataAccess.init();
        transactionDataAccess = new SQLTransactionAccess();
        transactionDataAccess.init();
        accountDataAccess = new SQLAccountAccess();
        accountDataAccess.init();
        applicationDataAccess = new SQLApplicationAccess();
        applicationDataAccess.init();
        workListAccess = new SQLWorkListAccess();
        accountService = new AccountService();
        accountService.setContext(this);
        applicationService = new ApplicationService();
        userService = new UserService();
        this.screenRegister = screenRegister;
        this.logger = logger;
    }

    public JavaConsole getConsole() {
        return console;
    }

    public UserDataAccess getUserDataAccess() {
        return userDataAccess;
    }

    public TransactionDataAccess getTransactionDataAccess() {
        return transactionDataAccess;
    }

    public AccountDataAccess getAccountDataAccess() {
        return accountDataAccess;
    }

    public ApplicationDataAccess getApplicationDataAccess() {
        return applicationDataAccess;
    }

    public EmployeeWorkListAccess getWorkListAccess() {
        return workListAccess;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public ApplicationService getApplicationService() {
        return applicationService;
    }

    public UserService getUserService() {
        return userService;
    }

    public ScreenRegister getScreenRegister() {
        return screenRegister;
    }

    public ApplicationState getState() {
        return state;
    }

    public Logger getLogger() {
        return logger;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public CreditRank getCreditRank() {
        return creditRank;
    }

    public void setCreditRank(CreditRank creditRank) {
        this.creditRank = creditRank;
    }

    public void changeState(ApplicationState newState) {
        state = newState;
        logger.info("Changing to application state " + newState.name());
    }

    public DBConnector getConnector() {
        return connector;
    }

    public static void createInstance(ScreenRegister screenRegister, Logger logger) {
        INS = new Context(screenRegister, logger);
    }

    public static Context getInstance() {
        return INS;
    }

}
