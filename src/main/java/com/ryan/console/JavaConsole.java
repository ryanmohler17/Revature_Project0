package com.ryan.console;

import java.util.List;

public interface JavaConsole {

    int promptMenu(String menuName, List<ConsoleOption> options);
    String promptPassword(String prompt);
    int promptInt(String prompt);
    Integer promptInt(String prompt, char[] allowQuit);
    double promptDouble(String prompt, int precision);
    Double promptDouble(String prompt, int precision, char[] allowQuit);
    String promptString(String prompt);
    boolean promptYesNo(String prompt);
    Boolean promptYesNo(String prompt, char[] allowQuit);

}
