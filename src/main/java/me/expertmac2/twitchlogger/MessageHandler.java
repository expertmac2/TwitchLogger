package me.expertmac2.twitchlogger;

import java.io.FileNotFoundException;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class MessageHandler extends ListenerAdapter {
	
	public void onMessage(MessageEvent event) throws Exception {
		TwitchLogger.instance.logWriter.addToQueue(new TwitchLogEntry(event));
	}

}