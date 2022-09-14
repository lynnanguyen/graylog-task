package org.graylog.task.utilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class HelpMenuTest {

    private final PrintStream standardOut = System.out;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void testDisplayHelpMenu() {
        HelpMenu helpMenu = new HelpMenu();
        helpMenu.displayHelpMenu();
        assertTrue(outputStreamCaptor.toString().contains("Graylog Task \n"));
        assertTrue(outputStreamCaptor.toString().contains("Author: Lynna Nguyen - 2022 \n"));
        assertTrue(outputStreamCaptor.toString().contains("Version 1.0.0 \n"));
        assertTrue(outputStreamCaptor.toString().contains("Usage: java -jar graylog-task-lynna-nguyen.jar [arguments] \n"));
        assertTrue(outputStreamCaptor.toString().contains("-h  or  --help"));
        assertTrue(outputStreamCaptor.toString().contains("-f  or  --file"));
        assertTrue(outputStreamCaptor.toString().contains("-u  or  --url"));
        assertTrue(outputStreamCaptor.toString().contains("-r"));
        assertTrue(outputStreamCaptor.toString().contains("-n"));
        assertTrue(outputStreamCaptor.toString().contains("-b  or  --bulk"));
        assertTrue(outputStreamCaptor.toString().contains("NOTE:"));
    }
}