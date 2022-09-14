package org.graylog.task.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.graylog.task.dto.Message;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

@Component
public class MessageParser {

    private final Logger logger = LogManager.getLogger(MessageParser.class.getName());

    public MessageParser() {}

    public ArrayList<Message> fromFile(String filePath) {
        ArrayList<Message> messages = new ArrayList<Message>();
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                ObjectMapper mapper = new ObjectMapper();
                Message message = mapper.readValue(data, Message.class);
                messages.add(message);
            }
            scanner.close();
        } catch (FileNotFoundException fnfe) {
            logger.error("File not found.", fnfe.getMessage());
            fnfe.printStackTrace();
            return null;
        } catch (JsonMappingException jme) {
            logger.error("Error mapping text file to message object.", jme.getMessage());
            jme.printStackTrace();
            return null;
        } catch (JsonProcessingException jpe) {
            logger.error("Error processing text file to message object.", jpe.getMessage());
            jpe.printStackTrace();
            return null;
        }

        return messages;
    }
}
