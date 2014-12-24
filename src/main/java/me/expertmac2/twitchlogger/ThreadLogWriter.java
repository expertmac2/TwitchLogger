package me.expertmac2.twitchlogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.Gson;

public class ThreadLogWriter extends Thread {

	private final PrintWriter writer;
	private final PrintWriter jsonWriter;
	private final SimpleDateFormat filenameFormat = new SimpleDateFormat("dd-M-yyyy_hh-mm-ss");
	private boolean keepWriting = true;
	private boolean isOkayToExit = false;

	private final ConcurrentLinkedQueue<TwitchLogEntry> logQueue = new ConcurrentLinkedQueue<TwitchLogEntry>();
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
		
		System.out.println("Logging to: " + file.getAbsolutePath());
		
		jsonWriter.println("[");
	}

	@Override
	public void run() {
		while (keepWriting) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			TwitchLogEntry entry;
			while ((entry = logQueue.poll()) != null) { // clear out logQueue
				writeNormalText(entry.toString());
				jsonWriter.println(gson.toJson(entry) + ",");
			}
			
			writer.flush();
			jsonWriter.flush();		
		}
		
		jsonWriter.println("]");
		writer.flush();
		jsonWriter.flush();
		
		isOkayToExit = true;
	}
	
	private void writeNormalText(String message) {
		writer.println(message);
		System.out.println(message);
	}

	public void addToQueue(TwitchLogEntry entry) {
		logQueue.add(entry);
	}
	
	public void setKeepWriting(boolean bool) {
		keepWriting = bool;
	}
	
	public boolean isOkayToExit() {
		return isOkayToExit;
	}

}
