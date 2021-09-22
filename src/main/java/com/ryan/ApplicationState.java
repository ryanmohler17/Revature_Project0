package com.ryan;

public enum ApplicationState {

    EXIT(null),
    MAIN_MENU("main"),
    LOGIN("login"),
    EMPLOYEE("employee"),
    CLIENT("user"),
    CLIENT_EMPLOYEE("user"),
    MANAGE_ACCOUNTS("account"),
    TRANSFER("transfer"),
    CREDIT("credit"),
    ACCOUNT("manage account"),
    CREATE_ACCOUNT("create account"),
    TRANSACTION("history"),
    DEPOSIT("deposit"),
    WITHDRAW("withdraw"),
    APPLY("apply"),
    APPLICATIONS("application"),
    CREATE_USER("create user"),
    REVIEW_APPLICATION("review");

    private final String screen;

    ApplicationState(String screen) {
        this.screen = screen;
    }

    public String getScreen() {
        return screen;
    }
}
