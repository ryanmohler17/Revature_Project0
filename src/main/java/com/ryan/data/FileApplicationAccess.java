package com.ryan.data;

import com.ryan.models.ApplicationStatus;
import com.ryan.models.CreditApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

public class FileApplicationAccess implements ApplicationDataAccess {

    private List<CreditApplication> applicationCache = new ArrayList<>();
    private String applicationFile = "data/creditApplications.csv";
    private SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    public void init() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(applicationFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                CreditApplication application = new CreditApplication();
                application.setId(Integer.parseInt(split[0]));
                application.setUserId(Integer.parseInt(split[1]));
                application.setAccountId(Integer.parseInt(split[2]));
                application.setAmount(Double.parseDouble(split[3]));
                application.setScore(Double.parseDouble(split[4]));
                application.setStatus(ApplicationStatus.valueOf(split[5]));
                application.setSubmitted(simpleFormatter.parse(split[6]));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private String formatApplication(CreditApplication application) {
        return application.getId() + "," + application.getUserId() + "," + application.getAccountId() + "," +
                application.getAmount() + "," + application.getScore() + "," + application.getStatus().name() + "," +
                simpleFormatter.format(application.getSubmitted()) + "\n";
    }

    @Override
    public Integer saveItem(CreditApplication item) {
        int id = applicationCache.size();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(applicationFile, true));
            writer.append(formatApplication(item));
            writer.flush();

            applicationCache.add(item);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    public CreditApplication getItem(Integer item) {
        return applicationCache.get(item);
    }

    @Override
    public List<CreditApplication> getUserApplications(int userId) {
        return applicationCache.stream().filter(application -> application.getUserId() == userId).collect(Collectors.toList());
    }

    @Override
    public List<CreditApplication> getAllItems() {
        return applicationCache;
    }
}
