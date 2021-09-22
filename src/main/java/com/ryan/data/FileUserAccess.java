package com.ryan.data;

import com.ryan.models.Client;
import com.ryan.models.Employee;
import com.ryan.models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUserAccess implements UserDataAccess {

    List<User> userCache = new ArrayList<>();
    private final String usersFile = "data/users.csv";
    private final String employeesFile = "data/employees.csv";
    private final String clientsFile = "data/clients.csv";
    private Map<String, BufferedReader> readers = new HashMap<>();
    private Map<String, BufferedWriter> writers = new HashMap<>();

    public FileUserAccess() {
        try {
            readers.put(usersFile, new BufferedReader(new FileReader(usersFile)));
        } catch (IOException e) {

        }
        try {
            writers.put(usersFile, new BufferedWriter(new FileWriter(usersFile, true)));
        } catch (IOException e) {

        }
    }

    public String getUsersFile() {
        return usersFile;
    }

    public String getEmployeesFile() {
        return employeesFile;
    }

    public String getClientsFile() {
        return clientsFile;
    }

    public void setWriter(String file, BufferedWriter writer) {
        writers.put(file, writer);
    }

    public void setReader(String file, BufferedReader reader) {
        readers.put(file, reader);
    }

    public String formatUser(User user, int type) {
        return user.getId() + "," + removeCommas(user.getUserName()) +
                removeCommas(user.getPassword()) + removeCommas(user.getEmailAddress()) + "," +
                removeCommas(user.getFirstName()) + "," + removeCommas(user.getLastName()) + "," + type + "\n";
    }

    @Override
    public void init() {
        try {
            BufferedReader bufferedReader = readers.get(usersFile);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(",");
                int type = Integer.parseInt(split[6]);
                User user;
                if (type == 1) {
                    user = new Employee();
                } else if (type == 2) {
                    user = new Client();
                } else {
                    continue;
                }
                user.setId(Integer.parseInt(split[0]));
                user.setUserName(split[1]);
                user.setPassword(split[2]);
                user.setEmailAddress(split[3]);
                user.setFirstName(split[4]);
                user.setLastName(split[5]);

                if (user instanceof Employee) {
                    readEmployee((Employee) user, Arrays.copyOfRange(split, 7, split.length - 1));
                } else if (user instanceof Client) {
                    readClient((Client) user, Arrays.copyOfRange(split, 7, split.length - 1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readEmployee(Employee employee, String[] items) {

    }

    private void readClient(Client client, String[] items) {

    }

    private void saveEmployee(Employee employee) {

    }

    private void saveClient(Client client) {

    }

    private String removeCommas(String item) {
        return item.replaceAll(",", "");
    }

    @Override
    public Integer saveItem(User item) {
        int id = userCache.size();
        item.setId(id);
        int type = -1;
        if (item instanceof Employee) {
            type = 1;
        } else if (item instanceof Client) {
            type = 2;
        }
        String formatted = formatUser(item, type);

        try {
            BufferedWriter writer = writers.get(usersFile);
            writer.append(formatted);
            writer.flush();

            userCache.add(item);

            if (item instanceof Employee) {
                saveEmployee((Employee) item);
            } else if (item instanceof Client) {
                saveClient((Client) item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    public User getItem(Integer item) {
        return userCache.get(item);
    }

    @Override
    public List<User> getAllItems() {
        return userCache;
    }

    @Override
    public User getUserByUserName(String name) {
        return userCache.stream().filter(user -> user.getUserName().equals(name)).findFirst().orElse(null);
    }

}
