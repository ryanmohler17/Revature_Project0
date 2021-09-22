package com.ryan.data;

public interface DataChecker {

    boolean dataExists();
    String getCurrentVersion();
    void doUpgrade();
    void createFirst();

}
