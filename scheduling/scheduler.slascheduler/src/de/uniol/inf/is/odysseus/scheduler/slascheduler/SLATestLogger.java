package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SLATestLogger {

	public static final String FILE_NAME = System.getProperty("user.home")
			+ File.separator + "odysseus" + File.separator
			+ "sla_scheduling.log";
	
	public static int bufferSize = 1000;
	
	public static int maxMessages = 1000000;
	
	public static int skip = 10000000;
	
	public static List<String> messageBuffer = new ArrayList<String>();
	
	public static FileWriter writer;

	public static void log(String message) {
		if (skip > 0) {
			skip--;
		} else if (maxMessages > 0) {
			messageBuffer.add(message);
			if (messageBuffer.size() == bufferSize) {
				writeToFile();
			}
			maxMessages--;
		} else {
			tearDown();
		}
	}
	
	public static void init() {
		File file = new File(FILE_NAME);
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeToFile() {
		for (String msg : messageBuffer) {
			try {
				writer.write(msg);
				writer.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		messageBuffer.clear();
	}
	
	public static void tearDown() {
		writeToFile();
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
