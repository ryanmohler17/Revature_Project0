package com.ryan.console;

import java.io.Console;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SystemConsole implements JavaConsole {

    private Console console = System.console();
    private PrintStream out = System.out;

    private List<Character> terminatingChars = new ArrayList<>();

    public SystemConsole() {
        terminatingChars.add('.');
        terminatingChars.add('?');
        terminatingChars.add(':');
        terminatingChars.add(';');
        terminatingChars.add('!');
    }

    @Override
    public int promptMenu(String menuName, List<ConsoleOption> options) {
        if (menuName.isEmpty()) {
            throw new IllegalArgumentException("Menu name can't be empty");
        }
        if (options.isEmpty()) {
            throw new IllegalArgumentException("Options can't be empty");
        }
        int start = (options.size() / 10) + 1;
        int len = 0;
        for (ConsoleOption option : options) {
            if (option.getFull().length() > len) {
                len = option.getFull().length();
            }
        }
        len += start + 1;
        StringBuilder sep = new StringBuilder();
        for (int i = 0; i <= len; i++) {
            sep.append("-");
        }
        out.println(menuName);
        out.println(sep);
        for (int i = 0; i < options.size(); i++) {
            out.printf("%" + start + "d. %s%n", (i + 1), options.get(i).getFull());
        }
        while (true) {
            String option = console.readLine("Enter Option: ");
            try {
                int opt = Integer.parseInt(option) - 1;
                if (options.size() > opt && opt >= 0) {
                    return opt;
                }
            } catch (NumberFormatException e) {
                Optional<ConsoleOption> opt = options.stream().filter(consoleOption -> consoleOption.getAliases().contains(option)).findFirst();
                if (opt.isPresent()) {
                    return options.indexOf(opt.get());
                }
            }
            out.println("Invalid Option");
        }
    }

    public void setConsole(Console console) {
        this.console = console;
    }

    private String addTermination(String prompt) {
        prompt = prompt.trim();
        char[] promptChars = prompt.toCharArray();
        if (terminatingChars.contains(promptChars[promptChars.length - 1])) {
            return prompt + " ";
        } else {
            return prompt + ": ";
        }
    }

    @Override
    public String promptPassword(String prompt) {
        return new String(console.readPassword(addTermination(prompt)));
    }

    @Override
    public String promptString(String prompt) {
        return console.readLine(addTermination(prompt));
    }

    @Override
    public int promptInt(String prompt) {
        return promptInt(prompt, new char[0]);
    }

    @Override
    public Integer promptInt(String prompt, char[] allowQuit) {
        while (true) {
            String line = console.readLine(addTermination(prompt));
            for (char ch : allowQuit) {
                if (line.equalsIgnoreCase(String.valueOf(ch))) {
                    return null;
                }
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                out.println("Please enter a valid integer");
            }
        }
    }

    @Override
    public double promptDouble(String prompt, int precision) {
        return promptDouble(prompt, precision, new char[0]);
    }

    @Override
    public Double promptDouble(String prompt, int precision, char[] allowQuit) {
        while (true) {
            String line = console.readLine(addTermination(prompt));
            for (char ch : allowQuit) {
                if (line.equalsIgnoreCase(String.valueOf(ch))) {
                    return null;
                }
            }
            try {
                double value = Double.parseDouble(line);
                String[] split = line.split("\\.");
                if (split.length > 1) {
                    if (split[1].length() > precision) {
                        out.println("Please enter a valid decimal number with " + precision + " decimal places or less");
                        continue;
                    }
                }
                return value;
            } catch (NumberFormatException e) {
                out.println("Please enter a valid decimal number with " + precision + " decimal places or less");
            }
        }
    }

    @Override
    public boolean promptYesNo(String prompt) {
        return promptYesNo(prompt, new char[0]);
    }

    @Override
    public Boolean promptYesNo(String prompt, char[] allowQuit) {
        while (true) {
            String line = console.readLine(prompt + " (y/n): ");
            for (char ch : allowQuit) {
                if (line.equalsIgnoreCase(String.valueOf(ch))) {
                    return null;
                }
            }
            if (line.equalsIgnoreCase("y")) {
                return true;
            } else if (line.equalsIgnoreCase("n")) {
                return false;
            }
            out.println("Please only enter y or n");
        }
    }

}
