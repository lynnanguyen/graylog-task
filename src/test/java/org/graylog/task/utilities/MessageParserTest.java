package org.graylog.task.utilities;

import org.graylog.task.dto.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageParserTest {

    private MessageParser messageParser;

    private PrintStream defaultOutStream;

    private ByteArrayOutputStream outStream;

    @BeforeEach
    public void setup() throws IOException {
        messageParser = new MessageParser();
        outStream = new ByteArrayOutputStream();
        defaultOutStream = System.out;
        System.setOut(new PrintStream(outStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(defaultOutStream);
    }

    private String getConsoleOutput() {
        return outStream.toString();
    }

    @Test
    public void fromFile() {
        String path = "src/test/resources/good-sample-messages.txt";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        ArrayList<Message> messages = messageParser.fromFile(absolutePath);

        assertEquals(messages.size(), 5);
        assertEquals(messages.get(0).getClass(), Message.class);
        assertEquals(messages.get(0).clientIP, "192.168.87.52");
    }

    @Test
    public void testFileNotFound() {
        messageParser.fromFile("");

        assertTrue(getConsoleOutput().contains("File not found."));
    }
}