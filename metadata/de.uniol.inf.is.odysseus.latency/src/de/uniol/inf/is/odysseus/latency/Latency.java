package de.uniol.inf.is.odysseus.latency;

public class Latency implements ILatency{

	private long lstart;
	private long lend;
	
	public Latency(){
		this.lstart = 0;
		this.lend = 0;
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
	
	public ILatency clone(){
		return new Latency(this);
	}

}
