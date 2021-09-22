package com.ryan.data;

public class FileDataChecker implements DataChecker {

    @Override
    public boolean dataExists() {
        return false;
    }

    @Override
    public String getCurrentVersion() {
        return null;
    }

    @Override
    public void doUpgrade() {

    }

    @Override
    public void createFirst() {

    }

}
