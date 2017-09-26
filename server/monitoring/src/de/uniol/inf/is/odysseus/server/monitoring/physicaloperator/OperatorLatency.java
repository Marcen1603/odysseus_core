package de.uniol.inf.is.odysseus.server.monitoring.physicaloperator;

public class OperatorLatency implements IMeasurableValue {
	private long startTime, latency, endTime;
	private String operatorName;
	private boolean done, confirmed;

	public OperatorLatency(String opName) {
		setConfirmed(false);
		setOperatorName(opName);
	}

	public OperatorLatency() {
		setConfirmed(false);
	}

	public long getLatency() {
		return latency;
	}

	public void setConfirmed(boolean committed) {
		this.confirmed = committed;
	}

	@Override
	public void startMeasurement(long nanoTimestamp) {
		setStartTime(nanoTimestamp);
		if (isStopped()) {
			stopMeasurement(this.endTime);
		}
	}

	private void setStartTime(long timestamp) {
		this.startTime = timestamp;
	}

	@Override
	public void stopMeasurement(long timestamp) {
		if (isStarted()) {
			this.latency = timestamp - this.startTime;
			setEndTime(timestamp);
			setConfirmed(true);
		} else {
			setEndTime(timestamp);
		}
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

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Override
	public boolean isConfirmed() {
		return this.confirmed;
	}

	public boolean isStarted() {
		if (this.startTime != 0) {
			return true;
		} else
			return false;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone() {
		this.done = true;
	}

	public boolean isStopped() {
		if (this.endTime != 0) {
			return true;
		} else
			return false;
	}
}
