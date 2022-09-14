package org.graylog.task.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graylog.task.dto.GelfMessage;
import org.graylog.task.dto.Message;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Component
public class RandomMessageGenerator {

    private final Logger logger = LogManager.getLogger(RandomMessageGenerator.class.getName());

    public RandomMessageGenerator() {}

    public GelfMessage generateGelfMessage() {
        logger.info("Generating random GELF message.");
        Message message = generateMessage();
        return new GelfMessage(message);
    }

    public ArrayList<GelfMessage> generateGelfMessages(int n) {
        logger.info(String.format("Generating %d random GELF messages.", n));
        ArrayList<GelfMessage> gelfMessages = new ArrayList<GelfMessage>();
        for (int i = 0; i < n; i++) {
            Message message = generateMessage();
            gelfMessages.add(new GelfMessage(message));
        }

        return gelfMessages;
    }

    public Message generateMessage() {
        logger.info("Generating random message.");
        Message message = new Message();
        Random r = new Random();
        message.clientDeviceType = Math.random() > 0.5 ? "desktop" : "mobile";
        message.clientIP = generateRandomIP(192, 168);
        message.clientIPClass = "noRecord";
        message.clientStatus = (Integer) randomResponsesMap.get("clientStatus")[r.nextInt(8)];
        message.clientRequestBytes = r.nextInt(999);
        message.clientRequestReferer = (String) randomResponsesMap.get("clientRequestReferer")[r.nextInt(3)];
        message.clientRequestUri = "/search";
        message.clientRequestUserAgent = (String) randomResponsesMap.get("clientRequestUserAgent")[r.nextInt(4)];
        message.clientSrcPort = r.nextInt(999);
        message.edgeServerIP = generateRandomIP(10, 0);
        message.edgeStartTimestamp = (int) java.time.Instant.now().getEpochSecond();
        message.destinationIP = generateRandomIP(172, 16);
        message.originResponseBytes = r.nextInt(999);
        message.originResponseTime = r.nextInt(999) * 1000000;

        return message;
    }

    public ArrayList<Message> generateMessages(int n) {
        logger.info(String.format("Generating %d random messages.", n));
        ArrayList<Message> messages = new ArrayList<Message>() ;
        for (int i = 0; i < n; i++) {
            messages.add(generateMessage());
        }

        return messages;
    }

    private String generateRandomIP(Integer networkPart1, Integer networkPart2) {
        Random r = new Random();
        return String.format("%d.%d.%d.%d", networkPart1, networkPart2, r.nextInt(256), r.nextInt(256));
    }

    private HashMap<String, Object[]> randomResponsesMap = new HashMap<String, Object[]>() {{
        put("clientDeviceType", new String[] {"desktop", "mobile"});
        put("clientStatus", new Integer[] {200, 300, 302, 400, 401, 403, 404, 501, 503, 550});
        put("clientRequestReferer", new String[] {"example.com", "graylog.org", "torch.sh"});
        put("clientRequestUserAgent", new String[] {
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/600.7.12 (KHTML, like Gecko) Version/8.0.7 Safari/600.7.12",
                "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                "Mozilla/5.0 (iPad; CPU OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13G36 Safari/601.1",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0"
        });
    }};
}
