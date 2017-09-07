package de.uniol.inf.is.odysseus.server.monitoring.physicaloperator;

public class OperatorLatency implements IMeasurableValue{
	private long startTime;
	private long latency;
	private long endZeit;
	
	//True if Latency haas been calculated
	private boolean committed;
	
	public OperatorLatency(){
		this.committed=false;
	}

	public long getLatency() {
		return latency;
	}

	private void setCommitted(boolean committed) {
		this.committed = committed;
	}

	@Override
	public void startMeasurement(long timestamp) {
		startTime = timestamp;
	}

	@Override
	public void stopMeasurement(long timestamp) {
		latency = timestamp - startTime;
		endZeit = timestamp;
		setCommitted(true);		
	}

	@Override
	public boolean isCalculated() {
		return this.committed;
	}
	
	
}
