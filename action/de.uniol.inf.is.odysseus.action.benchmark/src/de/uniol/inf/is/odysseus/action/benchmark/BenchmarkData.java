package de.uniol.inf.is.odysseus.action.benchmark;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;

public class BenchmarkData {
	private long creationTime;
	private Map<String, Long> outputTimes;
	
	public BenchmarkData(String identifier){
		this.creationTime = System.nanoTime();
		this.outputTimes = new HashMap<String, Long>();
	}
	
	
	public BenchmarkData(ITimeInterval object) {
		this.creationTime = object.getStart().getMainPoint();
		this.outputTimes = new HashMap<String, Long>();
	}

	public void addOutputTime(String id){
		this.outputTimes.put(id, System.currentTimeMillis());
	}
	
	public long getCreationTime() {
		return creationTime;
	}
	
	public Map<String, Long> getOutputTimes() {
		return outputTimes;
	}
	
	


}
