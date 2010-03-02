package de.uniol.inf.is.odysseus.action.benchmark;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;

public class BenchmarkData implements IMetaAttribute {
	private String identifier;
	private long creationTime;
	private Map<String, Long> outputTimes;
	
	public BenchmarkData(String identifier){
		this.identifier = identifier;
		this.creationTime = System.nanoTime();
		this.outputTimes = new HashMap<String, Long>();
	}
	
	/**
	 * Copy constructor
	 * @param benchmarkData
	 */
	public BenchmarkData(BenchmarkData benchmarkData) {
		this.identifier = benchmarkData.identifier;
		this.creationTime = benchmarkData.creationTime;
		this.outputTimes = new HashMap<String, Long>(benchmarkData.outputTimes);
	}
	
	public void addOutputTime(String id){
		this.outputTimes.put(id, System.nanoTime());
	}

	@Override
	public IClone clone() {
		return new BenchmarkData(this);
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public long getCreationTime() {
		return creationTime;
	}
	
	public Map<String, Long> getOutputTimes() {
		return outputTimes;
	}
	
	


}
