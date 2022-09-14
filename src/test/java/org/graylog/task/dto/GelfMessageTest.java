package org.graylog.task.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GelfMessageTest {

    private Message message;

    private String data = "{\"ClientDeviceType\":\"desktop\",\"ClientIP\":\"192.168.87.52\",\"ClientIPClass\":\"noRecord\",\"ClientStatus\":403,\"ClientRequestBytes\":889,\"ClientRequestReferer\":\"graylog.org\",\"ClientRequestURI\":\"/search\",\"ClientRequestUserAgent\":\"Mozilla/5.0\",\"ClientSrcPort\":122,\"EdgeServerIP\":\"10.0.151.71\",\"EdgeStartTimestamp\":1576929197,\"DestinationIP\":\"172.16.153.30\",\"OriginResponseBytes\":821,\"OriginResponseTime\":337000000}";

    @BeforeEach
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        message = mapper.readValue(data, Message.class);
    }

    @Test
    public void testGelfMessage() {
        String expectedShortMessage = String.format("Accessing %s from IP: %s and received a %d.",
                message.clientRequestReferer, message.clientIP, message.clientStatus);

        GelfMessage gelfMessage = new GelfMessage(message);

        assertEquals(gelfMessage.host, message.clientRequestReferer);
        assertEquals(gelfMessage.facility, message.clientDeviceType);
        assertEquals(gelfMessage.shortMessage, expectedShortMessage);
        assertEquals(gelfMessage.fullMessage, message.toString());
        assertEquals(gelfMessage.status, message.clientStatus);
        assertEquals(gelfMessage.sourceIP, message.clientIP);
        assertEquals(gelfMessage.targetIP, message.destinationIP);
        assertEquals(gelfMessage.serverIP, message.edgeServerIP);
        assertEquals(gelfMessage.requestUri, message.clientRequestUri);
        assertEquals(gelfMessage.device, message.clientDeviceType);
    }

    @Test
    public void testToString() {
        GelfMessage gelfMessage = new GelfMessage(message);
        String data = gelfMessage.toString();
        assertEquals(data.contains("\"host\":\"graylog.org\""), true);
        assertEquals(data.contains("facility"), true);
        assertEquals(data.contains("short_message"), true);
        assertEquals(data.contains("full_message"), true);
        assertEquals(data.contains("\"_status\":403"), true);
        assertEquals(data.contains("_source_ip"), true);
        assertEquals(data.contains("_destination_ip"), true);
        assertEquals(data.contains("_server_ip"), true);
        assertEquals(data.contains("_request_uri"), true);
        assertEquals(data.contains("_device"), true);
    }
}