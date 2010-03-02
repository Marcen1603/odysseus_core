package de.uniol.inf.is.odysseus.action.benchmark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ActuatorBenchmark implements IActuatorBenchmark{
	private Map<String, Integer> tupleNo;
	private Map<String, Long> timestamps;
	private static PrintWriter file;
	
	private static String fileLoc = System.getProperty("user.home");
	
	public ActuatorBenchmark(){
		this.tupleNo = new HashMap<String, Integer>();
		this.timestamps = new HashMap<String, Long>();
	}
	
	@Override
	public long notifyEnd(String identifier, String id, Operation op) {
		long end = System.currentTimeMillis();
		long start = this.timestamps.remove(identifier+id+op.toString());
		long timeInterval = end-start;
		
		//write to file
		synchronized (file) {
			if (file == null){
				try {
					this.file = new PrintWriter(new BufferedWriter(new FileWriter(
							fileLoc+
							File.separator+
							"odyLog"+System.currentTimeMillis()%100+
							".csv")));
					this.file.println("Identifier; ID; Operation; TimeConsumed");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			file.println(identifier+";"+id+";"+op.toString()+timeInterval);
		}
			return timeInterval;
	}

	@Override
	public String notifyStart(String identifier, Operation op) {
		long timestamp = System.currentTimeMillis();
		Integer tupleNo = this.tupleNo.get(identifier);
		if (tupleNo == null){
			tupleNo = -1;
		}
		tupleNo++;
		String newIdentifier = identifier+tupleNo+op.toString();
		this.timestamps.put(newIdentifier, timestamp);
		this.tupleNo.put(identifier, tupleNo);
		return tupleNo.toString();
	}
	
	public static void closeWrite(){
		synchronized (file) {
			if (file != null){
				file.close();
			}
		}
		
	}
}
