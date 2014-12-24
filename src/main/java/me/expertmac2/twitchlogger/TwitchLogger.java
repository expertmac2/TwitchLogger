package me.expertmac2.twitchlogger;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import me.expertmac2.twitchlogger.logutil.OffsetLogTimes;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Listener;

public class TwitchLogger {

	private LoggerGUI loggerGui;
	public static TwitchLogger instance;
	private PircBotX bot;
	public final ThreadLogWriter logWriter;
	private final String username;
	private final String OAuthToken;
	public final String channel;
	public final String outputDirectory;

	private boolean timeoutEnabled = false;
	private boolean showGui = true;
	private int timeout = 20;
	private long clearInterval = 3600L;

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
					case "--nogui":
						logger.setShowGui(false);
						break;
					case "--clearInterval":
						logger.setClearInterval((Integer.parseInt(split[1]) * 1000));
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
		if (showGui) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						loggerGui = new LoggerGUI();
						loggerGui.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		try {
			bot = createBot("irc.twitch.tv", 6667, OAuthToken);
			logWriter.start();
			bot.startBot();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopLogging() {
		bot.close();
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

	public PircBotX createBot(String host, int p, String pass) throws Exception {
		MessageHandler messageHandler = new MessageHandler();
		Configuration config = new Configuration.Builder()
		.setName(username)
		.addServer(host, p)
		.setServerPassword(pass)
		.addAutoJoinChannel(channel)
		.addListener(messageHandler)
		//.setAutoReconnect(false)
		.buildConfiguration();

		return new PircBotX(config);
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

	public void setShowGui(boolean bool) {
		showGui = bool;
	}
	
	public boolean isGuiEnabled() {
		return showGui;
	}
	
	public void setClearInterval(long i) {
		clearInterval = i;
	}
	
	public long getClearInterval() {
		return clearInterval;
	}

	private static void printUsage() {
		System.out.println(""
				+ "USAGE: java -jar <the jar> <username> <OAuth token> <channel> <log output directory> [any other args]"
				+ "\n\nOptional Args: "
				+ "\n--nogui         : Starts the bot without a GUI.\n"
				+ "\n--timeout       : Disconnects the bot after a certain number of seconds passes"
				+ "\n                  without anybody talking. (e.g. --timeout=20 disconnects the "
				+ "\n                  bot if no message is recieved for 20 seconds.)\n"
				+ "\n--clearInterval : The interval (in seconds) of how often the logger should" 
				+ "\n                  clear the TextArea in the GUI. Ignored if --nogui is on."
				+ "\n                  (e.g. --clearInterval=20 clears every 20 seconds.)\n"
				);


	}

}
