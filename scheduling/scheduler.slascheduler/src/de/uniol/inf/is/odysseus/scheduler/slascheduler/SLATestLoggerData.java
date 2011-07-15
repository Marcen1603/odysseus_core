package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SLATestLoggerData {
	
	public SLATestLoggerData(String id, int maxMessages, int skip) {
		File file = new File(SLATestLogger.PATH, id + ".csv");
		try {
			this.writer = new FileWriter(file);
			this.messageBuffer = new ArrayList<String>();
			this.skip = skip;
			this.maxMessages = maxMessages;
			this.bufferSize = SLATestLogger.bufferSize;
			this.closed = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileWriter writer;
	public List<String> messageBuffer;
	public int skip;
	public int maxMessages;
	public int bufferSize;
	public boolean closed;
	
}
