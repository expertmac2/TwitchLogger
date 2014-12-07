package me.expertmac2.twitchlogger;

import java.io.FileNotFoundException;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class MessageHandler extends ListenerAdapter {
	
	private final ThreadLogWriter logWriter;
	
	public MessageHandler() throws FileNotFoundException {
		logWriter = new ThreadLogWriter();
		logWriter.start();
	}

	public void onMessage(MessageEvent event) throws Exception {
		logWriter.addToQueue(event);
	}

}