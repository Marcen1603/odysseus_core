package de.uniol.inf.is.odysseus.interval_latency;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

/**
 * @author Jonas Jacobi
 */
public class IntervalLatency extends TimeInterval implements ILatency {

	private static final long serialVersionUID = -3129934770814427153L;
	private final ILatency latency;
	
	public IntervalLatency() {
		latency = new Latency();
	}
	

	public IntervalLatency(IntervalLatency clone) throws CloneNotSupportedException {
		super(clone);
		this.latency = clone.latency.clone();
	}
	
	@Override
	public final long getLatency() {
		return latency.getLatency();
	}

	@Override
	public final long getLatencyEnd() {
		return latency.getLatencyEnd();
	}

	@Override
	public final long getLatencyStart() {
		return latency.getLatencyStart();
	}

	@Override
	public final void setLatencyEnd(long timestamp) {
		latency.setLatencyEnd(timestamp);
	}

	@Override
	public final void setLatencyStart(long timestamp) {
		latency.setLatencyStart(timestamp);
	}

	@Override
	public IntervalLatency clone() throws CloneNotSupportedException {
		return new IntervalLatency(this);
	}

}
