package me.expertmac2.twitchlogger.logutil;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LogUtil {

	public static void main(String[] args) {
		try {
			String args0 = args[0].toLowerCase();
			switch (args0) {
			case "--offset":
				OffsetLogTimes.offset(args[1], args[2], Long.valueOf(args[3]), Boolean.parseBoolean(args[4]));
				break;
			case "--tojson":
				TextToJSON.toJson(args[1], args[2], Boolean.parseBoolean(args[3]));
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			printUsage();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void printUsage() {
		System.out.println("LogUtil: missing args :("
				+ "\n\nOffsetLogTimes:"
				+ "\njava -cp [jar] me.expertmac2.twitchlogger.logutil. --offset [log to offset] [results directory] [offset (milliseconds)] [subtract? false = add]"
				+ "\n\nTextToJSON:"
				+ "\njava -jar [jar] --toJson [log as text file] [results directory] [change times to gmt?]");
	}

}
