package de.uniol.inf.is.odysseus.action.benchmark;

import java.util.HashMap;
import java.util.Map;

public class BenchmarkData {
	private long creationTime;
	private Map<String, Long> outputTimes;
	
	public BenchmarkData(String identifier){
		this.creationTime = System.nanoTime();
		this.outputTimes = new HashMap<String, Long>();
	}
	
	
	public BenchmarkData(long creationTime) {
		this.creationTime = creationTime;
		this.outputTimes = new HashMap<String, Long>();
	}

	public void addOutputTime(String id){
		this.outputTimes.put(id, System.currentTimeMillis());
	}
	
	public void addOutputTime(String id, long timestamp){
		this.outputTimes.put(id, timestamp);
	}
	
	public long getCreationTime() {
		return creationTime;
	}
	
	public Map<String, Long> getOutputTimes() {
		return outputTimes;
	}
	
	


}
