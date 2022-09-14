package org.graylog.task.utilities;

import org.graylog.task.dto.GelfMessage;
import org.graylog.task.dto.Message;
import org.graylog.task.rest.HttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.util.ArrayList;

@Component
public class Cli {

    private HttpRequest httpRequest;

    private MessageParser messageParser;

    private RandomMessageGenerator randomMessageGenerator;

    private String filePath;

    private String apiUrl;

    private Boolean generateRandomMessage;

    private Integer numRandomMessages;

    private Boolean sendInBulk;

    @Autowired
    public Cli(HttpRequest httpRequest, MessageParser messageParser, RandomMessageGenerator randomMessageGenerator) {
        this.httpRequest = httpRequest;
        this.messageParser = messageParser;
        this.randomMessageGenerator = randomMessageGenerator;
    }

    private final Logger logger = LogManager.getLogger(Cli.class.getName());

    public void setArguments(String filePath, String apiUrl, Boolean generateRandomMessage, Integer numRandomMessages, Boolean sendInBulk) {
        this.filePath = filePath;
        this.apiUrl = apiUrl;
        this.generateRandomMessage = generateRandomMessage == null ? false : generateRandomMessage;
        this.numRandomMessages = numRandomMessages;
        this.sendInBulk = sendInBulk == null ? false : sendInBulk;
        this.httpRequest.requestUrl = apiUrl;
        this.httpRequest.requestMethod = "POST";
    }

    public void handleCommands() {
        // If no file path is specified, randomly generate one message and send it to the Graylog server
        if (filePath == null || filePath.isEmpty()) {
            generateRandomMessage = true;
            logger.info("No file path for messages was specified.");
        } else {
            // File path is specified
            logger.info("Sending messages specified in user file to Graylog server.");
        }

        // Set default Graylog API url to 'http://127.0.0.1:12201/gelf' if none is specified
        if (apiUrl == null || apiUrl.isEmpty()) {
            apiUrl = HttpRequest.GELF_URL;
            httpRequest.requestUrl = apiUrl;
            logger.info("Graylog API endpoint to send GELF message not specified, setting to default 'http://127.0.0.1:12201/gelf'.");
        } else {
            logger.info("Graylog API endpoint to send GELF message set to: " + apiUrl);
        }

        if (generateRandomMessage) {
            // Set default number of random messages to generate to 1 if none is specified
            if (numRandomMessages == null) {
                numRandomMessages = 1;
            } else if (numRandomMessages <= 0) {
                logger.error(String.format("Number of random messages to be generated must be > 0, user entered: %d.", numRandomMessages));
                return;
            }

            logger.info(String.format("Flags specified generating %d random messages to be sent to the Graylog server.", numRandomMessages));
        }

        handleMessages();
    }

    private void handleMessages() {
        if (filePath != null && !filePath.isEmpty()) {
            // Parse file to an array of messages
            ArrayList<Message> fileMessages = messageParser.fromFile(filePath);
            // Convert messages to an array of GELF messages
            ArrayList<GelfMessage> fileGelfMessages = new ArrayList<GelfMessage>();
            for (Message message : fileMessages) {
                fileGelfMessages.add(new GelfMessage(message));
            }

            if (fileGelfMessages.size() == 0) {
                logger.info("No GELF messages in file to post to the Graylog server.");
                return;
            }

            if (sendInBulk) {
                postGelfMessagesInBulk(fileGelfMessages, true);
            } else {
                postGelfMessages(fileGelfMessages, true);
            }
        }

        if (generateRandomMessage) {
            // Randomly generate GELF messages
            ArrayList<GelfMessage> randomGelfMessages = randomMessageGenerator.generateGelfMessages(numRandomMessages);

            if (randomGelfMessages.size() == 0) {
                logger.info("No randomly generated GELF messages to post to the Graylog server.");
                return;
            }

            if (sendInBulk) {
                postGelfMessagesInBulk(randomGelfMessages, false);
            } else {
                postGelfMessages(randomGelfMessages, false);
            }
        }
    }

    private void postGelfMessages(ArrayList<GelfMessage> gelfMessages, boolean fromFile) {
        // Count of all unsuccessful messages posted to the Graylog server GELF API endpoint
        int countSuccessful = 0;


        // For each GELF message make a POST request to the Graylog server GELF API endpoint
        for (int i = 0; i < gelfMessages.size(); i++) {
            HttpURLConnection con = httpRequest.openConnection();
            boolean isSuccessful = httpRequest.sendPostRequest(con, gelfMessages.get(i).toString());
            con.disconnect();

            if (isSuccessful) {
                countSuccessful ++;
            }
        }

        logInfo(countSuccessful, gelfMessages.size(), fromFile);
    }

    private void postGelfMessagesInBulk(ArrayList<GelfMessage> gelfMessages, boolean fromFile) {
        // Join all GELF messages with '\n' to send GELF messages in bulk
        StringBuilder data = new StringBuilder();
        for (GelfMessage gelfMessage : gelfMessages) {
            data.append(gelfMessage.toString() + "\n");
        }
        data.toString();

        HttpURLConnection con = httpRequest.openConnection();
        boolean isSuccessful = httpRequest.sendPostRequest(con, data.toString());
        con.disconnect();

        logInfo(isSuccessful ? gelfMessages.size() : 0, gelfMessages.size(), fromFile);
    }

    private void logInfo(int countSuccessful, int numGelfMessages, boolean fromFile) {
        if (countSuccessful == numGelfMessages) {
            logger.info(String.format("Successfully posted all %d messages from %s to the Graylog server%s.",
                    numGelfMessages, fromFile ? "from the file" : "from randomly generated messages", sendInBulk ? " in bulk" : ""));
        } else {
            logger.error(String.format("Error posting all %d messages from %s to the Graylog server%s.",
                    numGelfMessages, fromFile ? "from the file" : "from randomly generated messages", sendInBulk ? " in bulk" : ""));

            if (!sendInBulk) {
                logger.error(String.format("There were issues with %d out of %d total messages. Please check logs.",
                        numGelfMessages, countSuccessful));
            }
        }
    }
}
