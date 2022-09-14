package org.graylog.task.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    private String data = "{\"ClientDeviceType\":\"desktop\",\"ClientIP\":\"192.168.87.52\",\"ClientIPClass\":\"noRecord\",\"ClientStatus\":403,\"ClientRequestBytes\":889,\"ClientRequestReferer\":\"graylog.org\",\"ClientRequestURI\":\"/search\",\"ClientRequestUserAgent\":\"Mozilla/5.0\",\"ClientSrcPort\":122,\"EdgeServerIP\":\"10.0.151.71\",\"EdgeStartTimestamp\":1576929197,\"DestinationIP\":\"172.16.153.30\",\"OriginResponseBytes\":821,\"OriginResponseTime\":337000000}";

    private Message message;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        message = mapper.readValue(data, Message.class);
    }

    @Test
    public void testCreatingMessage() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(data, Message.class);
        } catch (Exception e) {

        }

        assertEquals(message.clientDeviceType, "desktop");
        assertEquals(message.clientIP, "192.168.87.52");
        assertEquals(message.clientIPClass, "noRecord");
        assertEquals(message.clientStatus, 403);
        assertEquals(message.clientRequestBytes, 889);
        assertEquals(message.clientRequestReferer, "graylog.org");
        assertEquals(message.clientRequestUri, "/search");
        assertEquals(message.clientRequestUserAgent, "Mozilla/5.0");
        assertEquals(message.clientSrcPort, 122);
        assertEquals(message.edgeServerIP, "10.0.151.71");
        assertEquals(message.edgeStartTimestamp, 1576929197);
        assertEquals(message.destinationIP, "172.16.153.30");
        assertEquals(message.originResponseBytes, 821);
        assertEquals(message.originResponseTime, 337000000);
    }

    @Test
    void testToString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        message = mapper.readValue(data, Message.class);

        String messageStr = message.toString();
        assertEquals(messageStr, data);
    }
}