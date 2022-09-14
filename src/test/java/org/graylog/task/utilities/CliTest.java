package org.graylog.task.utilities;

import org.graylog.task.dto.GelfMessage;
import org.graylog.task.dto.Message;
import org.graylog.task.rest.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CliTest {

    private Cli cli;

    private HttpRequest mockHttpRequest;

    private MessageParser mockMessageParser;

    private RandomMessageGenerator mockRandomMessageGenerator;

    @BeforeEach
    public void setup() {
        HttpURLConnection mockHttpUrlConnection = mock(HttpURLConnection.class);
        mockHttpRequest = mock(HttpRequest.class);
        when(mockHttpRequest.openConnection()).thenReturn(mockHttpUrlConnection);
        mockMessageParser = mock(MessageParser.class);
        mockRandomMessageGenerator = mock(RandomMessageGenerator.class);
        cli = new Cli(mockHttpRequest, mockMessageParser, mockRandomMessageGenerator);
    }

    @Test
    public void testSetArguments() {
        cli.setArguments("/file-path/sample-messages.txt", "http://localhost:9000", true, 2, true);

        assertEquals(ReflectionTestUtils.getField(cli, "filePath"), "/file-path/sample-messages.txt");
        assertEquals(ReflectionTestUtils.getField(cli, "apiUrl"), "http://localhost:9000");
        assertTrue((boolean) ReflectionTestUtils.getField(cli, "generateRandomMessage"));
        assertEquals(ReflectionTestUtils.getField(cli, "numRandomMessages"), 2);
        assertTrue((boolean) ReflectionTestUtils.getField(cli, "sendInBulk"));
    }

    @Test
    public void testNoFilePath() {
        ReflectionTestUtils.setField(cli, "filePath", null);
        cli.handleCommands();

        assertTrue((boolean) ReflectionTestUtils.getField(cli, "generateRandomMessage"));

        ReflectionTestUtils.setField(cli, "filePath", "");
        cli.handleCommands();

        assertTrue((boolean) ReflectionTestUtils.getField(cli, "generateRandomMessage"));
    }

    @Test
    public void testNoApiUrl() {
        ReflectionTestUtils.setField(cli, "apiUrl", null);
        cli.handleCommands();

        assertEquals(ReflectionTestUtils.getField(cli, "apiUrl"), HttpRequest.GELF_URL);

        ReflectionTestUtils.setField(cli, "apiUrl", "");
        cli.handleCommands();

        assertEquals(ReflectionTestUtils.getField(cli, "apiUrl"), HttpRequest.GELF_URL);
    }

    @Test
    public void testNoNumRandomMessages() {
        ReflectionTestUtils.setField(cli, "numRandomMessages", null);
        cli.handleCommands();

        assertEquals(ReflectionTestUtils.getField(cli, "numRandomMessages"), 1);
    }

    @Test
    public void testNegativeNumRandomMessages() {
        ReflectionTestUtils.setField(cli, "numRandomMessages", -1);
        cli.handleCommands();

        verify(mockRandomMessageGenerator, times(0)).generateMessages(anyInt());
    }

    @Test
    public void testFilePath() {
        String path = "src/test/resources/good-sample-messages.txt";
        File file = new File(path);
        ReflectionTestUtils.setField(cli, "filePath", file.getAbsolutePath());
        ReflectionTestUtils.setField(cli, "generateRandomMessage", true);
        when(mockMessageParser.fromFile(file.getAbsolutePath())).thenReturn(new ArrayList<Message>());
        cli.handleCommands();

        verify(mockMessageParser, times(1)).fromFile(anyString());
    }

    @Test
    public void testUnspecifiedFilePath() {
        ReflectionTestUtils.setField(cli, "filePath", null);
        ReflectionTestUtils.setField(cli, "generateRandomMessage", false);
        when(mockRandomMessageGenerator.generateGelfMessages(anyInt())).thenReturn(new ArrayList<GelfMessage>());
        cli.handleCommands();

        assertTrue((boolean) ReflectionTestUtils.getField(cli, "generateRandomMessage"));
    }

    @Test
    public void testGenerateRandomMessages() {
        ReflectionTestUtils.setField(cli, "generateRandomMessage", true);
        ReflectionTestUtils.setField(cli, "numRandomMessages", 2);
        cli.handleCommands();

        verify(mockRandomMessageGenerator, times(1)).generateGelfMessages(2);
    }

    @Test
    public void testNoFileGelfMessages() {
        String path = "src/test/resources/good-sample-messages.txt";
        File file = new File(path);
        ReflectionTestUtils.setField(cli, "filePath", file.getAbsolutePath());
        ReflectionTestUtils.setField(cli, "generateRandomMessage", true);
        when(mockMessageParser.fromFile(file.getAbsolutePath())).thenReturn(new ArrayList<Message>());
        cli.handleCommands();

        verify(mockHttpRequest, times(0)).openConnection();
    }

    @Test
    public void testNoRandomGelfMessages() {
        ReflectionTestUtils.setField(cli, "generateRandomMessage", true);
        ReflectionTestUtils.setField(cli, "numRandomMessages", 2);
        when(mockRandomMessageGenerator.generateGelfMessages(anyInt())).thenReturn(new ArrayList<GelfMessage>());
        cli.handleCommands();

        verify(mockHttpRequest, times(0)).openConnection();
    }

    @Test
    public void testSendFileGelfMessages() {
        String path = "src/test/resources/good-sample-messages.txt";
        File file = new File(path);
        ReflectionTestUtils.setField(cli, "filePath", file.getAbsolutePath());
        ReflectionTestUtils.setField(cli, "generateRandomMessage", false);
        ReflectionTestUtils.setField(cli, "sendInBulk", false);
        MessageParser realMessageParser = new MessageParser();
        ArrayList<Message> realMessages = realMessageParser.fromFile(file.getAbsolutePath());
        when(mockMessageParser.fromFile(file.getAbsolutePath())).thenReturn(realMessages);
        when(mockHttpRequest.sendPostRequest(any(), anyString())).thenReturn(true);
        cli.handleCommands();

        verify(mockHttpRequest, times(realMessages.size())).openConnection();
    }

    @Test
    public void testSendFileGelfMessagesInBulk() {
        String path = "src/test/resources/good-sample-messages.txt";
        File file = new File(path);
        MessageParser realMessageParser = new MessageParser();
        ArrayList<Message> realMessages = realMessageParser.fromFile(file.getAbsolutePath());

        ReflectionTestUtils.setField(cli, "filePath", file.getAbsolutePath());
        ReflectionTestUtils.setField(cli, "generateRandomMessage", false);
        ReflectionTestUtils.setField(cli, "sendInBulk", true);
        when(mockMessageParser.fromFile(file.getAbsolutePath())).thenReturn(realMessages);
        when(mockHttpRequest.sendPostRequest(any(), anyString())).thenReturn(true);
        cli.handleCommands();

        verify(mockHttpRequest, times(1)).openConnection();
    }

    @Test
    public void testSendRandomGelfMessages() {
        RandomMessageGenerator realRandomMessageGenerator = new RandomMessageGenerator();
        ArrayList<GelfMessage> realGelfMessages = realRandomMessageGenerator.generateGelfMessages(3);

        ReflectionTestUtils.setField(cli, "filePath", null);
        ReflectionTestUtils.setField(cli, "generateRandomMessage", true);
        ReflectionTestUtils.setField(cli, "numRandomMessages", 3);
        ReflectionTestUtils.setField(cli, "sendInBulk", false);
        when(mockRandomMessageGenerator.generateGelfMessages(3)).thenReturn(realGelfMessages);
        when(mockHttpRequest.sendPostRequest(any(), anyString())).thenReturn(true);
        cli.handleCommands();

        verify(mockHttpRequest, times(realGelfMessages.size())).openConnection();
    }

    @Test
    public void testSendRandomGelfMessagesInBulk() {
        RandomMessageGenerator realRandomMessageGenerator = new RandomMessageGenerator();
        ArrayList<GelfMessage> realGelfMessages = realRandomMessageGenerator.generateGelfMessages(3);

        ReflectionTestUtils.setField(cli, "filePath", null);
        ReflectionTestUtils.setField(cli, "generateRandomMessage", true);
        ReflectionTestUtils.setField(cli, "numRandomMessages", 3);
        ReflectionTestUtils.setField(cli, "sendInBulk", true);
        when(mockRandomMessageGenerator.generateGelfMessages(3)).thenReturn(realGelfMessages);
        when(mockHttpRequest.sendPostRequest(any(), anyString())).thenReturn(true);
        cli.handleCommands();

        verify(mockHttpRequest, times(1)).openConnection();
    }
}