package me.expertmac2.twitchlogger;

import java.util.Timer;
import java.util.TimerTask;

import org.jibble.pircbot.PircBot;

public class MessageHandler extends PircBot {

	private final Timer timer = new Timer();
	private int countdown;

	public MessageHandler(String username) {
		this.setName(username);
		countdown = TwitchLogger.instance.getTimeout();
	}

	@Override
	public void onConnect() {
		if (TwitchLogger.instance.isTimeoutEnabled()) {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					countdown--;
					if (countdown <= 0) {
						TwitchLogger.instance.stopLogging();
					}
				}

			}, 0L, 1000L);
		}
	}

	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		countdown = TwitchLogger.instance.getTimeout();
		TwitchLogger.instance.logWriter.addToQueue(new TwitchLogEntry(sender, message));
	}

}