package org.graylog.task.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class HelpMenu {

    private final Logger logger = LogManager.getLogger(HelpMenu.class.getName());

    public HelpMenu() {
        super();
    }

    public void displayHelpMenu() {
        logger.info("Print out help menu and exit.");
        String helpMessage = new StringBuilder()
                .append("Graylog Task \n")
                .append("Author: Lynna Nguyen - 2022 \n")
                .append("Version 1.0.0 \n")
                .append("\n")
                .append("Usage: java -jar graylog-task-lynna-nguyen.jar [arguments] \n")
                .append("\n")
                .append("Arguments: \n")
                .append("-h  or  --help \t\t Print help menu (this) and exit. \n")
                .append("-f  or  --file \t\t STRING - File of messages to send to the Graylog server. \n")
                .append("-u  or  --url  \t\t STRING - Graylog URL endpoint to send GELF message. Default = 'http://127.0.0.1:12201/gelf'. \n")
                .append("-r             \t\t BOOLEAN - Flag to generate random GELF messages and send to the Graylog server. Default = false. \n")
                .append("-n             \t\t INTEGER - Number of random GELF messages to generate and send to the Graylog server, must be > 0. Default = 1 if '-r' is specified. \n")
                .append("-b  or  --bulk \t\t BOOLEAN - Flag to send GELF message in bulk or individually. Default = false. \n")
                .append("NOTE: If neither the '-f' flag or the '-r' flag is specified, one random message will be generated and sent to the Graylog server. \n")
                .toString();
        System.out.println(helpMessage);
    }
}
