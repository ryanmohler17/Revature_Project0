package com.ryan.data;

import com.ryan.models.Transaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FileTransactionAccess implements TransactionDataAccess {

    private List<Transaction> transactionCache = new ArrayList<>();
    private String transactionFile = "data/transactions.csv";
    private SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    public void init() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(transactionFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");

                Transaction transaction = new Transaction();
                transaction.setId(Integer.parseInt(split[0]));
                transaction.setAccount(Integer.parseInt(split[1]));
                transaction.setDescription(split[2]);
                transaction.setAmount(Double.parseDouble(split[3]));
                transaction.setWhere(split[4]);
                transaction.setDate(simpleFormatter.parse(split[5]));

                transactionCache.add(transaction);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String formatTransaction(Transaction transaction) {
        return transaction.getId() + "," + transaction.getAccount() + "," +
                removeCommas(transaction.getDescription()) + "," + transaction.getAmount() +  "," +
                removeCommas(transaction.getWhere()) + "," + simpleFormatter.format(transaction.getDate()) + "\n";
    }

    @Override
    public Integer saveItem(Transaction item) {
        int id = transactionCache.size();

        item.setId(id);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(transactionFile, true));
            writer.append(formatTransaction(item));
            writer.flush();
            transactionCache.add(item);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public Transaction getItem(Integer item) {
        return transactionCache.get(item);
    }

    @Override
    public List<Transaction> getAllItems() {
        return transactionCache;
    }

    @Override
    public List<Transaction> getAccountTransactions(int accountId) {
        return transactionCache.stream().filter(transaction -> transaction.getAccount() == accountId).collect(Collectors.toList());
    }

    private String removeCommas(String item) {
        return item.replaceAll(",", "");
    }

}
