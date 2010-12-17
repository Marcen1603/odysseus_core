package de.uniol.inf.is.odysseus.latency;

import de.uniol.inf.is.odysseus.metadata.ILatency;

public class Latency implements ILatency{

	private long lstart;
	private long lend;
	
	public Latency(){
		this.lstart = System.nanoTime();
	}
	
	public Latency(long start, long end){
		this.lend = end;
		this.lstart = start;
	}
	
	private Latency(Latency copy){
		this.lend = copy.lend;
		this.lstart = copy.lstart;
	}
	
	@Override
	public long getLatency() {
		return this.lend - this.lstart;
	}

	@Override
	public long getLatencyEnd() {
		return this.lend;
	}

	@Override
	public long getLatencyStart() {
		return this.lstart;
	}

	@Override
	public void setLatencyEnd(long timestamp) {
		this.lend = timestamp;
	}

	@Override
	public void setLatencyStart(long timestamp) {
		this.lstart = timestamp;
		
	}
	
	@Override
	public ILatency clone(){
		return new Latency(this);
	}
	
	@Override
	public String toString(){
		return "" + (this.lend - this.lstart);
	}
	
	@Override
	public String csvToString() {
		return ""+ this.lstart+";"+this.lend+";"+(this.lend - this.lstart);
	}

}
