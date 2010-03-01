package de.uniol.inf.is.odysseus.action.benchmark;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ActuatorBenchmark implements IActuatorBenchmark{
	private Map<String, Integer> tupleNo;
	private Map<String, Long> timestamps;
	private FileWriter file;
	
	private static String fileLoc = System.getProperty("user.home");
	
	public ActuatorBenchmark(){
		this.tupleNo = new HashMap<String, Integer>();
		this.timestamps = new HashMap<String, Long>();
	}
	
	@Override
	public long notifyEnd(String identifier) {
		long end = System.currentTimeMillis();
		long start = this.timestamps.remove(identifier);
		long timeInterval = end-start;
		
		//write to file
		if (this.file == null){
			try {
				this.file = new FileWriter(
						fileLoc+
						File.separator+
						"odyLog"+System.currentTimeMillis()%100+
						".log");
				this.file.append("identifier; time\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			this.file.append(identifier+";"+timeInterval);
			this.file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return timeInterval;
	}

	@Override
	public String notifyStart(String identifier) {
		long timestamp = System.currentTimeMillis();
		Integer tupleNo = this.tupleNo.get(identifier);
		if (tupleNo == null){
			tupleNo = -1;
		}
		tupleNo++;
		String newIdentifier = identifier+tupleNo;
		this.timestamps.put(newIdentifier, timestamp);
		this.tupleNo.put(identifier, tupleNo);
		return newIdentifier;
	}
}
