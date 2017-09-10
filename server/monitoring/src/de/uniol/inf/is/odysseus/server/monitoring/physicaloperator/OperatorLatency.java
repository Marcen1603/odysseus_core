package de.uniol.inf.is.odysseus.server.monitoring.physicaloperator;

public class OperatorLatency implements IMeasurableValue {
	private long startTime = 0;
	private long latency;
	private long endTime;

	// True if Latency has been calculated
	private boolean committed;

	public OperatorLatency() {
		this.committed = false;
	}

	public long getLatency() {
		return latency;
	}

	private void setCommitted(boolean committed) {
		this.committed = committed;
	}

	@Override
	public void startMeasurement(long timestamp) {
		setStartTime(timestamp);
	}

	private void setStartTime(long timestamp) {
		this.startTime = timestamp;
	}

	@Override
	public void stopMeasurement(long timestamp) {
		latency = timestamp - startTime;
		setEndTime(timestamp);
		setCommitted(true);
	}

	@Override
	public boolean isCalculated() {
		return this.committed;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	private void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
