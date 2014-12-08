package me.expertmac2.twitchlogger;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Listener;

public class TwitchLogger {

	public static TwitchLogger instance;
	private PircBotX bot;
	public final ThreadLogWriter logWriter;
	private final String username;
	private final String OAuthToken;
	public final String channel;
	public final String outputDirectory;

	public TwitchLogger(String user, String oa, String chan, String output) throws FileNotFoundException {
		instance = this;
		username = user;
		OAuthToken = oa;
		channel = chan;
		outputDirectory = output;
		logWriter = new ThreadLogWriter();
	}

	public static void main(String[] args) {
		TwitchLogger logger;
		try {
			logger = new TwitchLogger(args[0], args[1], args[2], args[3]);
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			System.out.println("Not enough arguments!");
			System.out.println("USAGE: java -jar [the jar] [username] [OAuth token] [channel] [log output directory]");
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		logger.startLogging();
	}

	public void startLogging() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoggerGUI frame = new LoggerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			bot = createBot("irc.twitch.tv", 6667, OAuthToken);
			logWriter.start();
			bot.startBot();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopBot() {
		bot.close();
	}
	
	public boolean isBotConnected() {
		return bot.isConnected();
	}
	
	public PircBotX createBot(String host, int p, String pass) throws Exception {
		MessageHandler messageHandler = new MessageHandler();
		Configuration config = new Configuration.Builder()
				.setName(username)
				.addServer(host, p)
				.setServerPassword(pass)
				.addAutoJoinChannel(channel)
				.addListener(messageHandler)
				.buildConfiguration();

		return new PircBotX(config);
	}

}
