package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SLATestLogger {

	public static final String FILE_NAME = "sla_scheduling.log";
	public static final String PATH = System.getProperty("user.home")
			+ File.separator + "odysseus" + File.separator;

	private static final String CSV_SEPERATOR = ",";

	public static int bufferSize = 1000;

	public static int maxMessages = 1000000;

	public static int skip = 0;

	public static List<String> messageBuffer = new ArrayList<String>();

	public static FileWriter writer;

	public static Map<String, SLATestLoggerData> csvData = new HashMap<String, SLATestLoggerData>();
	
	public static boolean closed = false;

	public static void log(String message) {
		log(message, true);
	}

	public static void log(String message, boolean toFile) {
		if (skip > 0) {
			skip--;
		} else if (maxMessages > 0) {
			if (toFile) {
				messageBuffer.add(message);
				if (messageBuffer.size() == bufferSize) {
					writeToFile(writer, messageBuffer);
				}
			} else {
				System.err.println(message);
			}
			maxMessages--;
		} else if (!closed) {
			tearDown(writer, messageBuffer);
			closed = true;
		}
	}

	public static void logCSV(String id, Object... obj) {
		SLATestLoggerData data = csvData.get(id);
		if (data == null) {
			throw new RuntimeException("logger not initialized: " + id);
		}
		if (data.skip > 0) {
			skip--;
		} else if (data.maxMessages > 0) {
			StringBuilder sb = new StringBuilder();
			for (Object o : obj) {
				sb.append(o).append(CSV_SEPERATOR);
			}
			data.messageBuffer.add(sb.toString());
			if (data.messageBuffer.size() == data.bufferSize) {
				writeToFile(data.writer, data.messageBuffer);
			}
			data.maxMessages--;
		} else if (!data.closed) {
			tearDown(data.writer, data.messageBuffer);
			data.closed = true;
		}
		
		
	}

	public static void initCSVLogger(String id, int maxMessages, int skip, String... csvHeader) {
		SLATestLoggerData data = new SLATestLoggerData(id, maxMessages, skip);
		csvData.put(id, data);
		logCSV(id, csvHeader);
	}

	public static void init() {
		File file = new File(PATH, FILE_NAME);
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeToFile(FileWriter writer, List<String> messageBuffer) {
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

	public static void tearDown(FileWriter writer, List<String> messageBuffer) {
		writeToFile(writer, messageBuffer);
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String formatNanoTime(long nano) {
		StringBuilder sb = new StringBuilder();
		long afterPoint = nano % 1000000000;
		nano = nano / 1000000000;
		long sec = nano % 60;
		nano = nano / 60;
		long min = nano % 60;
		nano = nano / 60;
		sb.append(nano).append(":").append(min).append(":").append(sec)
				.append(".").append(afterPoint);
		return sb.toString();
	}

}
