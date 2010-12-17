package de.uniol.inf.is.odysseus.interval_latency_priority;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.Priority;

public class IntervalLatencyPriority extends TimeInterval implements ILatency,
		IPriority, Serializable {

	private static final long serialVersionUID = -4924797905689073685L;

	private ILatency latency;
	private IPriority prio;

	public IntervalLatencyPriority() {
		super(PointInTime.getInfinityTime());
		this.latency = new Latency();
		this.prio = new Priority();
	}

	public IntervalLatencyPriority(long start) {
		super(PointInTime.getZeroTime());// TODO warum hier zero und oben
											// infinity???
		this.latency = new Latency();
		this.prio = new Priority();
		setLatencyStart(start);
	}

	public IntervalLatencyPriority(IntervalLatencyPriority original) {
		super(original);

		this.latency = (ILatency) original.latency.clone();
		this.prio = (IPriority) original.prio.clone();
	}

	@Override
	public final long getLatencyEnd() {
		return this.latency.getLatencyEnd();
	}

	@Override
	public final long getLatency() {
		return this.latency.getLatency();
	}

	@Override
	public final long getLatencyStart() {
		return this.latency.getLatencyStart();
	}

	@Override
	public final void setLatencyEnd(long timestamp) {
		this.latency.setLatencyEnd(timestamp);
	}

	@Override
	public final void setLatencyStart(long timestamp) {
		this.latency.setLatencyStart(timestamp);
	}

	@Override
	public IntervalLatencyPriority clone() {
		return new IntervalLatencyPriority(this);
	}

	@Override
	public String toString() {
		return "( i= " + super.toString() + " ; " + " l=" + this.latency + ""
				+ " ; p=" + this.prio + ")";
	}

	@Override
	public String csvToString() {
		return super.csvToString() + ";" + this.latency.csvToString()+";"+this.prio.csvToString();
	}

	@Override
	public final byte getPriority() {
		return this.prio.getPriority();
	}

	@Override
	public final void setPriority(byte priority) {
		this.prio.setPriority(priority);
	}
}
