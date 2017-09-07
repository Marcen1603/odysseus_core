package de.uniol.inf.is.odysseus.server.monitoring.query;

public class OperatorLatency {
	private long startTime;
	private long latency;
	private long endZeit;
	
	//True if Latency haas been calculated
	private boolean committed;
	
	OperatorLatency(){
		this.committed=false;
	}

	public void setStartTime(long s){
		startTime = s;
	}
	
	public void calcLatency(long endTime){
		latency = endTime - startTime;
		endZeit = endTime;
		setCommitted(true);
	}

	public long getLatency() {
		return latency;
	}

	public boolean isCommitted() {
		return committed;
	}

	public void setCommitted(boolean committed) {
		this.committed = committed;
	}
}
