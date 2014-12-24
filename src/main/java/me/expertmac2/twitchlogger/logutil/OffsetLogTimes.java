package me.expertmac2.twitchlogger.logutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import me.expertmac2.twitchlogger.TwitchLogEntry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Hello world!
 *
 */
public class OffsetLogTimes {
	
    public static void offset(String log, String res, long offset, boolean sub) throws FileNotFoundException, IOException {
        String results;
        try(BufferedReader br = new BufferedReader(new FileReader(log))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            results = sb.toString();
        }
        
        System.out.println("file read, moving on");
        
    	Gson gson = new Gson();
    	java.lang.reflect.Type type = new TypeToken<List<TwitchLogEntry>>() {
        }.getType();
        List<TwitchLogEntry> logEntries = gson.fromJson(results, type);
        List<TwitchLogEntry> fixedLogEntries = new ArrayList<TwitchLogEntry>();
        
        System.out.println("beginning to modify logEntries with offset " + offset + " (sub?: " + sub + ")");
        
        for (TwitchLogEntry logEntry : logEntries) {
        	// year month day hours minutes seconds
        	Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        	c.set(logEntry.year, logEntry.month - 1, logEntry.day, logEntry.hours, logEntry.minutes, logEntry.seconds);
        	if (sub) {
        		c.setTimeInMillis(c.getTimeInMillis() - (offset));
        	} else {
        		c.setTimeInMillis(c.getTimeInMillis() + (offset));
        	}
        	TwitchLogEntry fixedEntry = new TwitchLogEntry(logEntry.sender, logEntry.message, c);
        	fixedLogEntries.add(fixedEntry);
        	System.out.println("processed message by " + logEntry.sender + ", before: " + logEntry.toString() + ", after: " + fixedEntry.toString());
        }
        
        System.out.println("processed entries, moving on");
        System.out.println("writing results to a file now");
        
        File jsonFile = new File(res);
		jsonFile.getParentFile().mkdirs();
		PrintWriter jsonWriter = new PrintWriter(jsonFile);
		
		jsonWriter.println("[");
		
		for (TwitchLogEntry logEntry : fixedLogEntries) {
			jsonWriter.println(gson.toJson(logEntry) + ",");
		}
        
		jsonWriter.println("]");
		jsonWriter.flush();
		jsonWriter.close();
		
		System.out.println("done.");
    }
}
