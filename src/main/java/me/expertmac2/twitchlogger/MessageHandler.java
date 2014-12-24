package me.expertmac2.twitchlogger;

import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.MessageEvent;

public class MessageHandler extends ListenerAdapter {

	private final Timer timer = new Timer();
	private int countdown;

	public MessageHandler() {
		countdown = TwitchLogger.instance.getTimeout();
	}

	@Override
	public void onConnect(ConnectEvent event) {
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
	public void onMessage(MessageEvent event) throws Exception {
		countdown = TwitchLogger.instance.getTimeout();
		TwitchLogger.instance.logWriter.addToQueue(new TwitchLogEntry(event));
	}

}