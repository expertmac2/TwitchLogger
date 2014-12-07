package me.expertmac2.twitchlogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.pircbotx.hooks.events.MessageEvent;

import com.google.gson.Gson;

public class ThreadLogWriter extends Thread {

	private final PrintWriter writer;
	private final PrintWriter jsonWriter;
	private final SimpleDateFormat filenameFormat = new SimpleDateFormat("dd-M-yyyy_hh-mm-ss");
	private boolean keepWriting = true;

	private final ConcurrentLinkedQueue<MessageEvent> logQueue = new ConcurrentLinkedQueue<MessageEvent>();
	private final Gson gson = new Gson();
	
	public ThreadLogWriter() throws FileNotFoundException {
		String filenamePrefix = TwitchLogger.instance.channel + "_";
		String path = TwitchLogger.instance.outputDirectory + "\\";
		
		File file = new File(path + filenamePrefix + filenameFormat.format(new Date()) + ".txt");
		file.getParentFile().mkdirs();
		writer = new PrintWriter(file);
		
		File jsonFile = new File(path + filenamePrefix + filenameFormat.format(new Date()) + ".json");
		jsonFile.getParentFile().mkdirs();
		jsonWriter = new PrintWriter(jsonFile);
		
		jsonWriter.println("[");
	}

	@Override
	public void run() {
		while (keepWriting) {
			MessageEvent event = logQueue.poll();
			if (event == null)
				continue;

			TwitchLogEntry entry = new TwitchLogEntry(event);

			writer.println(entry.toString());
			jsonWriter.println(gson.toJson(entry) + ",");
			System.out.println(entry.toString());
			
			writer.flush();
			jsonWriter.flush();
		}
	}

	public void addToQueue(MessageEvent event) {
		logQueue.add(event);
	}

}
