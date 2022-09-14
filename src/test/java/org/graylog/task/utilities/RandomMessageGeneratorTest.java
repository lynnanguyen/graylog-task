package org.graylog.task.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.graylog.task.dto.GelfMessage;
import org.graylog.task.dto.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RandomMessageGeneratorTest {

    private RandomMessageGenerator randomMessageGenerator;

    private Message generatedMessage;

    private String data = "{\"ClientDeviceType\":\"desktop\",\"ClientIP\":\"192.168.87.52\",\"ClientIPClass\":\"noRecord\",\"ClientStatus\":403,\"ClientRequestBytes\":889,\"ClientRequestReferer\":\"graylog.org\",\"ClientRequestURI\":\"/search\",\"ClientRequestUserAgent\":\"Mozilla/5.0\",\"ClientSrcPort\":122,\"EdgeServerIP\":\"10.0.151.71\",\"EdgeStartTimestamp\":1576929197,\"DestinationIP\":\"172.16.153.30\",\"OriginResponseBytes\":821,\"OriginResponseTime\":337000000}";

    @BeforeEach
    public void setup() throws JsonProcessingException {
        randomMessageGenerator = new RandomMessageGenerator();
        ObjectMapper mapper = new ObjectMapper();
        generatedMessage = mapper.readValue(data, Message.class);
    }

    @Test
    void generateGelfMessage() {
        GelfMessage gelfMessage = randomMessageGenerator.generateGelfMessage();

        assertTrue(gelfMessage.host == "example.com" || gelfMessage.host == "graylog.org" || gelfMessage.host == "torch.sh");
        assertTrue(gelfMessage.facility == "desktop" || gelfMessage.facility == "mobile");
        assertTrue(gelfMessage.sourceIP.contains("192.168."));
        assertTrue(gelfMessage.targetIP.contains("172.16."));
        assertTrue(gelfMessage.serverIP.contains("10.0."));
        assertTrue(gelfMessage.requestUri == "/search");
        assertTrue(gelfMessage.device == "desktop" || gelfMessage.device == "mobile");
    }

    @Test
    void generateGelfMessages() {
        ArrayList<GelfMessage> gelfMessages = randomMessageGenerator.generateGelfMessages(3);

        assertEquals(gelfMessages.size(), 3);
        assertEquals(gelfMessages.get(0).getClass(), GelfMessage.class);

    }

    @Test
    public void generateMessage() {
        List<Integer> expectedStatus = Arrays.asList(200, 300, 302, 400, 401, 403, 404, 501, 503, 550);
        Message message = randomMessageGenerator.generateMessage();

        assertTrue(message.clientDeviceType == "desktop" || message.clientDeviceType == "mobile");
        assertTrue(message.clientIP.contains("192.168."));
        assertEquals(message.clientIPClass, "noRecord");
        assertTrue(expectedStatus.contains(message.clientStatus));
        assertTrue(message.clientRequestBytes < 1000);
        assertTrue(message.clientRequestReferer == "example.com" ||
                message.clientRequestReferer == "graylog.org" || message.clientRequestReferer == "torch.sh");
        assertEquals(message.clientRequestUri, "/search");
        assertTrue(message.clientSrcPort < 1000);
        assertTrue(message.edgeServerIP.contains("10.0."));
        assertEquals(message.edgeStartTimestamp, (Integer) 1576929197);
        assertTrue(message.destinationIP.contains("172.16."));
        assertTrue(message.originResponseBytes < 1000);
    }

    @Test
    public void generateMessages() {
        ArrayList<Message> messages = randomMessageGenerator.generateMessages(3);

        assertEquals(messages.size(), 3);
        assertEquals(messages.get(0).getClass(), Message.class);
    }
}