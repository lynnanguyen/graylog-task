package org.graylog.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graylog.task.utilities.Cli;
import org.graylog.task.utilities.HelpMenu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@SpringBootApplication
@Command(
		name = "graylogTask",
		description = "It reads a message file or generates random messages and sends GELF messages to a graylog server."
)
public class TaskApplication implements Runnable {

	@Option(names = {"-h", "--help"}, description = "Print help menu and exit.")
	private boolean displayHelp;

	@Option(names = {"-f", "--file"}, description = "File of messages to send to a Graylog server.")
	private String filePath;

	@Option(names = {"-u", "--url"}, description = "Graylog API endpoint to send GELF message. Default = 'http://127.0.0.1:12201/gelf'")
	private String apiUrl;

	@Option(names = {"-r"}, description = "Flag to generate random messages and send to the Graylog server.")
	private Boolean generateRandomMessage;

	@Option(names = {"-n"}, description = "Number of random messages to generate and send to the Graylog server, must be > 0. Default = 1 if '-r' is specified.")
	private Integer numRandomMessages;

	@Option(names = {"-b" , "--bulk"}, description = "Flag to send GELF messages in bulk or not. Default = true.")
	private Boolean sendInBulk;

	private static ConfigurableApplicationContext context;

	private final static Logger logger = LogManager.getLogger(TaskApplication.class.getName());

	public static void main(String[] args) {
		context = SpringApplication.run(TaskApplication.class, args);

		logger.info("Graylog task started.");
		logger.info("Author: Lynna Nguyen - 2022");
		logger.info("Version 1.0.0");

		new CommandLine(new TaskApplication()).execute(args);
	}

	@Override
	public void run() {
		// Display help if '-h' or '--help' flag is specified
		if (displayHelp) {
			HelpMenu helpMenu = context.getBean(HelpMenu.class);
			helpMenu.displayHelpMenu();
			return;
		}

		Cli cli = context.getBean(Cli.class);
		cli.setArguments(filePath, apiUrl, generateRandomMessage, numRandomMessages, sendInBulk);
		cli.handleCommands();
	}
}
