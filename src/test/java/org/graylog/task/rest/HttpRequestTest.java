package org.graylog.task.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HttpRequestTest {

    private PrintStream defaultOutStream;

    private ByteArrayOutputStream outStream;

    @BeforeEach
    public void setup() {
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
    public void testSettingUrl() {
        HttpRequest httpRequest = new HttpRequest("http://localhost:9000");

        assertEquals(httpRequest.requestUrl, "http://localhost:9000");
    }

    @Test
    public void testSettingUrlAndMethod() {
        HttpRequest httpRequest = new HttpRequest("http://localhost:9000", "POST");

        assertEquals(httpRequest.requestUrl, "http://localhost:9000");
        assertEquals(httpRequest.requestMethod, "POST");
    }

    @Test
    public void testOpenConnection() {
        HttpRequest httpRequest = new HttpRequest("http://localhost:9000");
        HttpURLConnection con = httpRequest.openConnection();

        assertEquals(con.getURL().toString(), "http://localhost:9000");
        assertEquals(con.getContentType(), "application/json");
        assertEquals(con.getRequestProperty("Accept"), "application/json");
        assertEquals(con.getRequestMethod(), "POST");
    }

    @Test
    public void testMalformedUrl() {
        HttpRequest httpRequest = new HttpRequest("INVALID_URL");
        HttpURLConnection con = httpRequest.openConnection();

        assertNull(con);
        assertTrue(getConsoleOutput().contains("Malformed URL exception: INVALID_URL"));
    }

    @Test
    public void testInvalidRequestMethod() {
        HttpRequest httpRequest = new HttpRequest("http://localhost:9000", "INVALID_METHOD");
        HttpURLConnection con = httpRequest.openConnection();

        assertNull(con);
        assertTrue(getConsoleOutput().contains("Error setting INVALID_METHOD request method for URL: http://localhost:9000"));
    }

    @Test
    public void testSendGetRequest() {
        HttpRequest httpRequest = new HttpRequest();

        assertThrows(UnsupportedOperationException.class, () -> {
            httpRequest.sendGetRequest();
        });
    }

    @Test
    public void testSendSuccessfulPostRequest() throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        HttpURLConnection con = httpRequest.openConnection();
        HttpURLConnection spyCon = spy(con);
        doReturn(HttpURLConnection.HTTP_OK).when(spyCon).getResponseCode();

        boolean isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertTrue(isSuccessful);
    }

    @Test
    public void testSendOtherSuccessfulPostRequest() throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        HttpURLConnection con = httpRequest.openConnection();
        HttpURLConnection spyCon = spy(con);
        doReturn(HttpURLConnection.HTTP_OK).when(spyCon).getResponseCode();

        boolean isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertTrue(isSuccessful);

        doReturn(HttpURLConnection.HTTP_CREATED).when(spyCon).getResponseCode();
        isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertTrue(isSuccessful);

        doReturn(HttpURLConnection.HTTP_ACCEPTED).when(spyCon).getResponseCode();
        isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertTrue(isSuccessful);

        doReturn(HttpURLConnection.HTTP_NO_CONTENT).when(spyCon).getResponseCode();
        isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertTrue(isSuccessful);

        doReturn(HttpURLConnection.HTTP_RESET).when(spyCon).getResponseCode();
        isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertTrue(isSuccessful);

        doReturn(HttpURLConnection.HTTP_PARTIAL).when(spyCon).getResponseCode();
        isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertTrue(isSuccessful);
    }

    @Test
    public void testSendUnsuccessfulPostRequest() throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        HttpURLConnection con = httpRequest.openConnection();
        HttpURLConnection spyCon = spy(con);
        doReturn(HttpURLConnection.HTTP_BAD_REQUEST).when(spyCon).getResponseCode();

        boolean isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertFalse(isSuccessful);

        doReturn(HttpURLConnection.HTTP_NOT_FOUND).when(spyCon).getResponseCode();
        isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertFalse(isSuccessful);

        doReturn(HttpURLConnection.HTTP_ENTITY_TOO_LARGE).when(spyCon).getResponseCode();
        isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertFalse(isSuccessful);

        doReturn(HttpURLConnection.HTTP_INTERNAL_ERROR).when(spyCon).getResponseCode();
        isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertFalse(isSuccessful);

        doReturn(HttpURLConnection.HTTP_BAD_REQUEST).when(spyCon).getResponseCode();
        isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertFalse(isSuccessful);

        doReturn(HttpURLConnection.HTTP_BAD_METHOD).when(spyCon).getResponseCode();
        isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertFalse(isSuccessful);
    }

    @Test
    public void testSendPostRequestIOException1() throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        HttpURLConnection mockCon = mock(HttpURLConnection.class);
        HttpURLConnection spyCon = spy(mockCon);
        doThrow(IOException.class).when(spyCon).getOutputStream();

        boolean isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertFalse(isSuccessful);
        assertTrue(getConsoleOutput().contains("IO Exception running POST request"));
    }

    @Test
    public void testSendPostRequestIOException2() throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        HttpURLConnection con = httpRequest.openConnection();
        HttpURLConnection spyCon = spy(con);
        doThrow(IOException.class).when(spyCon).getResponseCode();

        boolean isSuccessful = httpRequest.sendPostRequest(spyCon, "");
        assertFalse(isSuccessful);
        assertTrue(getConsoleOutput().contains("IO Exception running POST request"));
    }
}