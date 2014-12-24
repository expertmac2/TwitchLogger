package me.expertmac2.twitchlogger;

import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class TwitchLogger {

	public static TwitchLogger instance;
	private MessageHandler bot;
	public final ThreadLogWriter logWriter;
	private final String username;
	private final String OAuthToken;
	public final String channel;
	public final String outputDirectory;

	private boolean timeoutEnabled = false;
	private int timeout = 20;

	public TwitchLogger(String user, String oa, String chan, String output) throws FileNotFoundException {
		instance = this;
		username = user;
		OAuthToken = oa;
		channel = chan;
		outputDirectory = output;
		logWriter = new ThreadLogWriter();
	}

	public static void main(String[] args) {
		//System.out.println("super special debug version for ios!!\n");
		TwitchLogger logger;
		try {
			logger = new TwitchLogger(args[0], args[1], args[2], args[3]);
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			System.out.println("Not enough arguments!");
			TwitchLogger.printUsage();
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		for (String arg : args) {
			if (arg.startsWith("--")) {
				String[] split = arg.split("=");
				try {
					switch (split[0]) {
					case "--timeout":
						logger.setTimeoutEnabled(Integer.parseInt(split[1]));
						break;
					}	
				} catch (ArrayIndexOutOfBoundsException aioobe) {
					System.out.println("You have an argument, but no value: " + arg);
					TwitchLogger.printUsage();
					return;
				}
			}
		}

		logger.startLogging();
	}

	public void startLogging() {
		try {//"irc.twitch.tv", 6667, OAuthToken
			bot = new MessageHandler(username);
			logWriter.start();
			bot.connect("irc.twitch.tv", 6667, OAuthToken);
			bot.joinChannel(channel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopLogging() {
		bot.disconnect();
		logWriter.setKeepWriting(false);

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (logWriter.isOkayToExit() && !bot.isConnected()) {
					System.exit(0);
				}
			}

		}, 0L, 1000L);
	}

	public boolean isTimeoutEnabled() {
		return timeoutEnabled;
	}

	public int getTimeout() { 
		return timeout;
	}

	public void setTimeoutEnabled(int i) {
		timeoutEnabled = true;
		timeout = i;
	}

	private static void printUsage() {
		System.out.println(""
				+ "USAGE: java -jar <the jar> <username> <OAuth token> <channel> <log output directory> [any other args]"
				+ "\n\nOptional Args: "
				+ "\n--timeout       : Disconnects the bot after a certain number of seconds passes"
				+ "\n                  without anybody talking. (e.g. --timeout=20 disconnects the "
				+ "\n                  bot if no message is recieved for 20 seconds.)\n");


	}

}
