package de.uniol.inf.is.odysseus.action.benchmark;

import java.util.HashMap;
import java.util.Map;

public class ActuatorBenchmark implements IActuatorBenchmark{
	private Map<String, Integer> tupleNo;
	private Map<String, Long> timestamps;
	
	private static String fileLoc;
	
	public ActuatorBenchmark(){
		this.tupleNo = new HashMap<String, Integer>();
		this.timestamps = new HashMap<String, Long>();
	}
	
	@Override
	public long notifyEnd(String identifier) {
		long end = System.currentTimeMillis();
		long start = this.timestamps.get(identifier);
		long timeInterval = end-start;
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
	
	public static void setFileLoc(String fileLoc) {
		ActuatorBenchmark.fileLoc = fileLoc;
	}

}
