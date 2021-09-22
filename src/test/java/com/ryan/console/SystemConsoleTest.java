package com.ryan.console;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class SystemConsoleTest {

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream oldOut = System.out;
    InputStream oldIn = System.in;

    @Before
    public void init() {
        oldOut = System.out;
        oldIn = System.in;
        System.setOut(new PrintStream(out));
    }

    @Test(timeout = 5000)
    public void testOptionNumber() {
        Console mockedConsole = Mockito.mock(Console.class);

        String menuName = "Test Menu";
        String opt1 = "Test Option 1";
        String opt2 = "Test Option 2";
        int expectedOpt = 0;
        Mockito.when(mockedConsole.readLine("Enter Option: ")).then(invocation -> {
            System.out.print(invocation.getArgument(0).toString());
            return String.valueOf(expectedOpt + 1);
        });

        String expectedPrint = menuName + "\n" +
                "----------------\n" +
                "1. " + opt1 + "\n" +
                "2. " + opt2 + "\n" +
                "Enter Option: ";

        List<ConsoleOption> options = new ArrayList<>();
        options.add(new ConsoleOption(opt1));
        options.add(new ConsoleOption(opt2));

        JavaConsole console = new SystemConsole();
        ((SystemConsole) console).setConsole(mockedConsole);
        int chosenOpt = console.promptMenu(menuName, options);
        String printed = new String(out.toByteArray(), StandardCharsets.UTF_8);

        assertEquals("Printed does not match expected", expectedPrint, printed);
        assertEquals("Returned option does not match expected", expectedOpt, chosenOpt);
    }

    @Test(timeout = 5000)
    public void testOptionAllies() {
        Console mockedConsole = Mockito.mock(Console.class);

        String menuName = "Test Menu";
        String opt1 = "Test Option 1";
        String opt2 = "Test Option 2";
        int expectedOpt = 0;
        Mockito.when(mockedConsole.readLine("Enter Option: ")).then(invocation -> {
            System.out.print(invocation.getArgument(0).toString());
            return "test1";
        });

        String expectedPrint = menuName + "\n" +
                "----------------\n" +
                "1. " + opt1 + "\n" +
                "2. " + opt2 + "\n" +
                "Enter Option: ";

        List<ConsoleOption> options = new ArrayList<>();
        options.add(new ConsoleOption(opt1, "test1"));
        options.add(new ConsoleOption(opt2, "test2"));

        JavaConsole console = new SystemConsole();
        ((SystemConsole) console).setConsole(mockedConsole);
        int chosenOpt = console.promptMenu(menuName, options);
        String printed = new String(out.toByteArray(), StandardCharsets.UTF_8);

        assertEquals("Printed does not match expected", expectedPrint, printed);
        assertEquals("Returned option does not match expected", chosenOpt, expectedOpt);
    }

    @Test(timeout = 5000)
    public void testOptionInvalid() {
        Console mockedConsole = Mockito.mock(Console.class);

        String menuName = "Test Menu";
        String opt1 = "Test Option 1";
        String opt2 = "Test Option 2";
        int expectedOpt = 0;
        AtomicBoolean firstRun = new AtomicBoolean(true);
        Mockito.when(mockedConsole.readLine("Enter Option: ")).then(invocation -> {
            System.out.print(invocation.getArgument(0).toString());
            if (firstRun.get()) {
                firstRun.set(false);
                return "hi";
            } else {
                return String.valueOf(expectedOpt + 1);
            }
        });

        String expectedPrint = menuName + "\n" +
                "----------------\n" +
                "1. " + opt1 + "\n" +
                "2. " + opt2 + "\n" +
                "Enter Option: " +
                "Invalid Option\n" +
                "Enter Option: ";

        List<ConsoleOption> options = new ArrayList<>();
        options.add(new ConsoleOption(opt1));
        options.add(new ConsoleOption(opt2));

        JavaConsole console = new SystemConsole();
        ((SystemConsole) console).setConsole(mockedConsole);
        int chosenOpt = console.promptMenu(menuName, options);
        String printed = new String(out.toByteArray(), StandardCharsets.UTF_8);

        assertEquals("Printed does not match expected", expectedPrint, printed);
        assertEquals("Returned option does not match expected", expectedOpt, chosenOpt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionsEmptyTitleConstructor() {
        new ConsoleOption("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionsEmptyTitleSetter() {
        new ConsoleOption("Test").setFull("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPromptMenuEmptyTitle() {
        new SystemConsole().promptMenu("", new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPromptMenuNoOptions() {
        new SystemConsole().promptMenu("Test", new ArrayList<>());
    }

    @Test(timeout = 5000)
    public void testParseInt() {
        String prompt = "Enter int: ";
        Console mockedConsole = Mockito.mock(Console.class);
        int expected = 5;

        Mockito.when(mockedConsole.readLine(prompt)).then(invocation -> {
            System.out.print(invocation.getArgument(0).toString());
            return String.valueOf(expected);
        });

        JavaConsole console = new SystemConsole();
        ((SystemConsole) console).setConsole(mockedConsole);
        int chosen = console.promptInt(prompt);
        String printed = new String(out.toByteArray(), StandardCharsets.UTF_8);

        assertEquals("Printed does not match expected", prompt, printed);
        assertEquals("Converted value does not match expected", expected, chosen);
    }

    @Test(timeout = 5000)
    public void testParseIntNotInt() {
        String prompt = "Enter int: ";
        Console mockedConsole = Mockito.mock(Console.class);
        int expected = 5;

        AtomicBoolean firstRun = new AtomicBoolean(true);
        Mockito.when(mockedConsole.readLine(prompt)).then(invocation -> {
            System.out.print(invocation.getArgument(0).toString());
            if (firstRun.get()) {
                firstRun.set(false);
                return "hi";
            } else {
                return String.valueOf(expected);
            }
        });

        JavaConsole console = new SystemConsole();
        ((SystemConsole) console).setConsole(mockedConsole);
        int chosen = console.promptInt(prompt);
        String printed = new String(out.toByteArray(), StandardCharsets.UTF_8);

        assertEquals("Printed does not match expected", prompt + "Please enter a valid integer\n" + prompt, printed);
        assertEquals("Converted value does not match expected", expected, chosen);
    }

    @Test(timeout = 5000)
    public void testParseDouble() {
        String prompt = "Enter double: ";
        Console mockedConsole = Mockito.mock(Console.class);
        double expected = 5.5;

        Mockito.when(mockedConsole.readLine(prompt)).then(invocation -> {
            System.out.print(invocation.getArgument(0).toString());
            return String.valueOf(expected);
        });

        JavaConsole console = new SystemConsole();
        ((SystemConsole) console).setConsole(mockedConsole);
        double chosen = console.promptDouble(prompt, 2);
        String printed = new String(out.toByteArray(), StandardCharsets.UTF_8);

        assertEquals("Printed does not match expected", prompt, printed);
        assertEquals("Converted value does not match expected", expected, chosen, .01);
    }

    @Test(timeout = 5000)
    public void testParseDoubleNotDouble() {
        String prompt = "Enter double: ";
        Console mockedConsole = Mockito.mock(Console.class);
        double expected = 5.5;

        AtomicBoolean firstRun = new AtomicBoolean(true);
        Mockito.when(mockedConsole.readLine(prompt)).then(invocation -> {
            System.out.print(invocation.getArgument(0).toString());
            if (firstRun.get()) {
                firstRun.set(false);
                return "hi";
            } else {
                return String.valueOf(expected);
            }
        });

        JavaConsole console = new SystemConsole();
        ((SystemConsole) console).setConsole(mockedConsole);
        double chosen = console.promptDouble(prompt, 2);
        String printed = new String(out.toByteArray(), StandardCharsets.UTF_8);

        assertEquals("Printed does not match expected", prompt + "Please enter a valid decimal number with 2 decimal places or less\n" + prompt, printed);
        assertEquals("Converted value does not match expected", expected, chosen, .01);
    }

    @Test(timeout = 5000)
    public void testParseDoubleWrongPrecision() {
        String prompt = "Enter double: ";
        Console mockedConsole = Mockito.mock(Console.class);
        double expected = 5.5;

        AtomicBoolean firstRun = new AtomicBoolean(true);
        Mockito.when(mockedConsole.readLine(prompt)).then(invocation -> {
            System.out.print(invocation.getArgument(0).toString());
            if (firstRun.get()) {
                firstRun.set(false);
                return "5.555";
            } else {
                return String.valueOf(expected);
            }
        });

        JavaConsole console = new SystemConsole();
        ((SystemConsole) console).setConsole(mockedConsole);
        double chosen = console.promptDouble(prompt, 2);
        String printed = new String(out.toByteArray(), StandardCharsets.UTF_8);

        assertEquals("Printed does not match expected", prompt + "Please enter a valid decimal number with 2 decimal places or less\n" + prompt, printed);
        assertEquals("Converted value does not match expected", expected, chosen, .01);
    }

    @Test
    public void testPromptPassword() {
        String prompt = "Enter password: ";
        Console mockedConsole = Mockito.mock(Console.class);
        String expected = "1234";

        Mockito.when(mockedConsole.readPassword(prompt)).then(invocation -> {
            System.out.print(invocation.getArgument(0).toString());
            return expected.toCharArray();
        });

        JavaConsole console = new SystemConsole();
        ((SystemConsole) console).setConsole(mockedConsole);
        String chosen = console.promptPassword(prompt);
        String printed = new String(out.toByteArray(), StandardCharsets.UTF_8);

        assertEquals("Printed does not match expected", prompt, printed);
        assertEquals("Entered value does not match expected", expected, chosen);
    }

    @Test
    public void testGettersAndSetters() {
        String full = "Test";
        String full2 = "Test2";
        String alias = "test";
        ConsoleOption consoleOption = new ConsoleOption(full, alias);
        String gotFull = consoleOption.getFull();
        consoleOption.setFull(full2);
        String gotFull2 = consoleOption.getFull();
        String gotAlias = consoleOption.getAlias(0);

        assertEquals("Full values do not match", full, gotFull);
        assertEquals("Full 2 values do not match", full2, gotFull2);
        assertEquals("Aliases don't match", alias, gotAlias);
    }

    @Test
    public void testYesNo() {
        String prompt = "Does this work";
        Console mockedConsole = Mockito.mock(Console.class);
        AtomicReference<String> printed = new AtomicReference<>("");
        AtomicBoolean first = new AtomicBoolean(false);
        Mockito.when(mockedConsole.readLine(Mockito.anyString())).then(invocation -> {
            printed.set(printed.get() + invocation.getArgument(0));
            if (!first.get()) {
                first.set(true);
                return "hi";
            } else {
                return "y";
            }
        });

        JavaConsole console = new SystemConsole();
        ((SystemConsole) console).setConsole(mockedConsole);
        boolean works = console.promptYesNo(prompt);
        assertEquals("Printed does not match expected", prompt + " (y/n): " + prompt + " (y/n): ", printed.get());
        assertTrue("Boolean is not true", works);
    }

}
