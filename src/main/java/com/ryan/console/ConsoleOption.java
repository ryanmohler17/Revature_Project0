package com.ryan.console;

import java.util.Arrays;
import java.util.List;

public class ConsoleOption {

    private String full;
    private final List<String> aliases;

    public ConsoleOption( String full, String... aliases) {
        if (full.isEmpty()) {
            throw new IllegalArgumentException("Option must not be empty");
        }
        this.full = full;
        this.aliases = Arrays.asList(aliases);
    }

    public String getAlias(int pos) {
        return aliases.get(pos);
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        if (full.isEmpty()) {
            throw new IllegalArgumentException("Option must not be empty");
        }
        this.full = full;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
