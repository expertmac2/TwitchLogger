package me.expertmac2.twitchlogger;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.pircbotx.hooks.events.MessageEvent;

public class TwitchLogEntry {

	public final String sender;
	public final String message;
	public final int month;
	public final int day;
	public final int year;
	public final int hours;
	public final int minutes;
	public final int seconds;
	
	public TwitchLogEntry(MessageEvent event) {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		
		sender = event.getUser().getNick();
		message = event.getMessage();
		month = now.get(Calendar.MONTH) + 1;
		day = now.get(Calendar.DAY_OF_WEEK);
		year = now.get(Calendar.YEAR);
		hours = now.get(Calendar.HOUR_OF_DAY);
		minutes = now.get(Calendar.MINUTE);
		seconds = now.get(Calendar.SECOND);
	}

	@Override
	public String toString() {
		return "[" + dateString() + " " + timeString() + "] " + sender + ": " + message;
	}
	
	private String timeString() {
		String formattedHours = String.valueOf(hours);
		String formattedMinutes = String.valueOf(minutes);
		String formattedSeconds = String.valueOf(seconds);
		if (hours < 10) {
			formattedHours = String.format("%02d", hours);
		}
		if (minutes < 10) {
			formattedMinutes = String.format("%02d", minutes);
		}
		if (seconds < 10) {
			formattedSeconds = String.format("%02d", seconds);
		}
		return formattedHours + "-" + formattedMinutes + "-" + formattedSeconds;
	}
	
	private String dateString() {
		return month + "-" + day + "-" + year;
	}
}
