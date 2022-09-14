package org.graylog.task;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import picocli.CommandLine;
import picocli.CommandLine.MissingParameterException;
import picocli.CommandLine.ParseResult;

import static org.junit.Assert.*;

@SpringBootTest
public class TaskApplicationTest {

	@Test
	public void testHelpOptions() {
		String[] args = new String[] {"-h"};
		TaskApplication app = new TaskApplication();
		CommandLine cmd = new CommandLine(app);
		ParseResult parseResult = cmd.parseArgs(args);

		assertEquals(parseResult.hasMatchedOption("--help"), true);
		assertEquals(parseResult.matchedOptions().isEmpty(), false);
		assertEquals((boolean) parseResult.matchedOption("--help").getValue(), true);

		args = new String[] {"--help"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals(parseResult.hasMatchedOption("-h"), true);
		assertEquals(parseResult.matchedOptions().isEmpty(), false);
		assertEquals((boolean) parseResult.matchedOption("-h").getValue(), true);

		args = new String[] {"-h=true"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals((boolean) parseResult.matchedOption("-h").getValue(), true);

		args = new String[] {"--help=true"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals((boolean) parseResult.matchedOption("--help").getValue(), true);

		args = new String[] {"-h=false"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals((boolean) parseResult.matchedOption("-h").getValue(), false);

		args = new String[] {"--help=false"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals((boolean) parseResult.matchedOption("--help").getValue(), false);
	}

	@Test
	public void testFileOption() {
		String[] args = new String[] {"-f", "/test-file.txt"};
		TaskApplication app = new TaskApplication();
		CommandLine cmd = new CommandLine(app);
		ParseResult parseResult = cmd.parseArgs(args);

		assertEquals(parseResult.hasMatchedOption("--file"), true);
		assertEquals(parseResult.matchedOptions().isEmpty(), false);
		assertEquals(parseResult.matchedOption("--file").getValue(), "/test-file.txt");

		args = new String[] {"--file", "/test-file.txt"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals(parseResult.hasMatchedOption("-f"), true);
		assertEquals(parseResult.matchedOptions().isEmpty(), false);
		assertEquals(parseResult.matchedOption("-f").getValue(), "/test-file.txt");
	}

	@Test
	public void testFileOptionWithoutFileSpecified() {
		String[] args1 = new String[] {"-f"};
		TaskApplication app1 = new TaskApplication();
		CommandLine cmd1 = new CommandLine(app1);

		assertThrows(MissingParameterException.class, () -> {
			cmd1.parseArgs(args1);
		});

		String[] args2 = new String[] {"--file"};
		TaskApplication app2 = new TaskApplication();
		CommandLine cmd2 = new CommandLine(app2);

		assertThrows(MissingParameterException.class, () -> {
			cmd2.parseArgs(args2);
		});
	}

	@Test
	public void testUrlOption() {
		String[] args = new String[] {"-u", "http://127.0.0.1:9000"};
		TaskApplication app = new TaskApplication();
		CommandLine cmd = new CommandLine(app);
		ParseResult parseResult = cmd.parseArgs(args);

		assertEquals(parseResult.hasMatchedOption("--url"), true);
		assertEquals(parseResult.matchedOptions().isEmpty(), false);
		assertEquals(parseResult.matchedOption("--url").getValue(), "http://127.0.0.1:9000");

		args = new String[] {"--url", "http://127.0.0.1:9000"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals(parseResult.hasMatchedOption("-u"), true);
		assertEquals(parseResult.matchedOptions().isEmpty(), false);
		assertEquals(parseResult.matchedOption("-u").getValue(), "http://127.0.0.1:9000");
	}

	@Test
	public void testUrlOptionWithNoUrlSpecified() {
		String[] args1 = new String[] {"-u"};
		TaskApplication app1 = new TaskApplication();
		CommandLine cmd1 = new CommandLine(app1);

		assertThrows(MissingParameterException.class, () -> {
			cmd1.parseArgs(args1);
		});

		String[] args2 = new String[] {"--url"};
		TaskApplication app2 = new TaskApplication();
		CommandLine cmd2 = new CommandLine(app2);

		assertThrows(MissingParameterException.class, () -> {
			cmd2.parseArgs(args2);
		});
	}

	@Test
	public void testROption() {
		String[] args = new String[] {"-r"};
		TaskApplication app = new TaskApplication();
		CommandLine cmd = new CommandLine(app);
		ParseResult parseResult = cmd.parseArgs(args);

		assertEquals(parseResult.hasMatchedOption("-r"), true);
		assertEquals(parseResult.matchedOptions().isEmpty(), false);
		assertEquals((boolean) parseResult.matchedOption("-r").getValue(), true);

		args = new String[] {"-r=true"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals((boolean) parseResult.matchedOption("-r").getValue(), true);

		args = new String[] {"-r=false"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals((boolean) parseResult.matchedOption("-r").getValue(), false);
	}

	@Test
	public void testNOption() {
		String[] args = new String[] {"-n", "1"};
		TaskApplication app = new TaskApplication();
		CommandLine cmd = new CommandLine(app);
		ParseResult parseResult = cmd.parseArgs(args);

		assertEquals(parseResult.hasMatchedOption("-n"), true);
		assertEquals(parseResult.matchedOptions().isEmpty(), false);
		assertEquals((int) parseResult.matchedOption("-n").getValue(), 1);
	}

	@Test
	public void testBulkOption() {
		String[] args = new String[] {"-b"};
		TaskApplication app = new TaskApplication();
		CommandLine cmd = new CommandLine(app);
		ParseResult parseResult = cmd.parseArgs(args);

		assertEquals(parseResult.hasMatchedOption("--bulk"), true);
		assertEquals(parseResult.matchedOptions().isEmpty(), false);
		assertEquals((boolean) parseResult.matchedOption("--bulk").getValue(), true);

		args = new String[] {"--bulk"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals(parseResult.hasMatchedOption("-b"), true);
		assertEquals(parseResult.matchedOptions().isEmpty(), false);
		assertEquals((boolean) parseResult.matchedOption("-b").getValue(), true);

		args = new String[] {"-b=true"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals((boolean) parseResult.matchedOption("-b").getValue(), true);

		args = new String[] {"--bulk=true"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals((boolean) parseResult.matchedOption("--bulk").getValue(), true);

		args = new String[] {"-b=false"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals((boolean) parseResult.matchedOption("-b").getValue(), false);

		args = new String[] {"--bulk=false"};
		app = new TaskApplication();
		cmd = new CommandLine(app);
		parseResult = cmd.parseArgs(args);

		assertEquals((boolean) parseResult.matchedOption("--bulk").getValue(), false);
	}
}
