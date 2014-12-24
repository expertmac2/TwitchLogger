package me.expertmac2.twitchlogger.logutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.google.gson.Gson;

import me.expertmac2.twitchlogger.TwitchLogEntry;

/**
 * Hello world!
 *
 */
public class TextToJSON {
	
    public static void toJson(String log, String res, boolean toGMT) throws IOException {
    	System.out.println("reading file.");
    	
        List<TwitchLogEntry> logEntries = new ArrayList<TwitchLogEntry>();
        try(BufferedReader br = new BufferedReader(new FileReader(log))) {
            String line = br.readLine();

            while (line != null) {
            	String[] contentSplit = line.split("]");
            	String[] dateAndTime = contentSplit[0].substring(1).split(" ");
            	String[] senderAndMessage = contentSplit[1].split(":");
            	
            	Calendar cal = (toGMT) ? toGMT(processDateAndTime(dateAndTime[0], dateAndTime[1])) : processDateAndTime(dateAndTime[0], dateAndTime[1]);
            	TwitchLogEntry entry = new TwitchLogEntry(senderAndMessage[0].substring(1), senderAndMessage[1].substring(1), cal);
            	System.out.println("processed entry (" + entry + ")");
            	logEntries.add(entry);
            	
                line = br.readLine();
            }
        }
        
        System.out.println("done, moving on. writing to json file now");
        
        Gson gson = new Gson();
        File jsonFile = new File(res);
		jsonFile.getParentFile().mkdirs();
		PrintWriter jsonWriter = new PrintWriter(jsonFile);
		
		jsonWriter.println("[");
		
		for (TwitchLogEntry logEntry : logEntries) {
			jsonWriter.println(gson.toJson(logEntry) + ",");
		}
        
		jsonWriter.println("]");
		jsonWriter.flush();
		jsonWriter.close();
		
		System.out.println("done.");
    }
    
    private static Calendar processDateAndTime(String dateString, String timeString) {
    	Calendar local = Calendar.getInstance();
    	String[] date = dateString.split("-");
    	String[] time = timeString.split("-");
    	
    	local.set(Calendar.DATE, Integer.parseInt(date[0]));
    	local.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
    	local.set(Calendar.YEAR, Integer.parseInt(date[2]));
    	local.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
    	local.set(Calendar.MINUTE, Integer.parseInt(time[1]));
    	local.set(Calendar.SECOND, Integer.parseInt(time[2]));
    	
    	return local;
    }
    
    private static Calendar toGMT(Calendar otherTimeZone) {
    	Calendar gmt = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    	gmt.setTimeInMillis(otherTimeZone.getTimeInMillis());
    	return gmt;
    }
}
