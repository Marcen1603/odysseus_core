package de.uniol.inf.is.odysseus.server.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public class OperatorLatency implements IMeasurableValue {
	private long startTime, latency, endTime;
	private boolean done, confirmed;
	private IPhysicalOperator operator;

	public OperatorLatency(IPhysicalOperator o) {
		setConfirmed(false);
		this.setOperator(o);
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

	public IPhysicalOperator getOperator() {
		return operator;
	}

	public void setOperator(IPhysicalOperator operator) {
		this.operator = operator;
	}
}
