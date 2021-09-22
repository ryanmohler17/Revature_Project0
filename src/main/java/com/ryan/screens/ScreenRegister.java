package com.ryan.screens;

import java.util.HashMap;
import java.util.Map;

public class ScreenRegister {

    private Map<String, Screen> registeredScreens = new HashMap<>();

    public Screen getScreen(String name) {
        return registeredScreens.get(name);
    }

    public void registerScreen(Screen screen) {
        registeredScreens.put(screen.getName(), screen);
    }

}
