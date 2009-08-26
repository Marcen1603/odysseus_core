package de.uniol.inf.is.odysseus.interval_latency;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;

/**
 * @author Jonas Jacobi
 */
public class IntervalLatency extends TimeInterval implements ILatency {

	private static final long serialVersionUID = -3129934770814427153L;
	private long start;
	private long end;

	@Override
	public long getLatency() {
		return end - start;
	}

	@Override
	public long getLatencyEnd() {
		return end;
	}

	@Override
	public long getLatencyStart() {
		return start;
	}

	@Override
	public void setLatencyEnd(long timestamp) {
		end = timestamp;
	}

	@Override
	public void setLatencyStart(long timestamp) {
		start = timestamp;
	}

}
