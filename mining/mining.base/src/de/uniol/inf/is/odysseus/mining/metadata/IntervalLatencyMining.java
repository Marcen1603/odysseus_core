package de.uniol.inf.is.odysseus.mining.metadata;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

public class IntervalLatencyMining extends TimeInterval implements ILatency, IMiningMetadata, Serializable {


	private static final long serialVersionUID = -4481993965683442055L;
	private ILatency latency;
	private IMiningMetadata mining;

	public IntervalLatencyMining(){
		super(PointInTime.getInfinityTime());
		this.latency = new Latency();
		this.mining = new MiningMetadata();
	}	

	public IntervalLatencyMining(IntervalLatencyMining ilm) {
		super(ilm.getStart().clone());
		this.latency = ilm.latency.clone();
		this.mining = (IMiningMetadata) ilm.mining.clone();
	}

	@Override
	public boolean isCorrected() {
		return this.mining.isCorrected();
	}

	@Override
	public void setCorrected(boolean corrected) {
		this.mining.setCorrected(corrected);
		
	}

	@Override
	public void setLatencyStart(long timestamp) {
		this.latency.setLatencyStart(timestamp);
		
	}

	@Override
	public void setLatencyEnd(long timestamp) {
		this.latency.setLatencyEnd(timestamp);
	}

	@Override
	public long getLatencyStart() {
		return this.latency.getLatencyStart();
	}

	@Override
	public long getLatencyEnd() {
		return this.latency.getLatencyEnd();
	}

	@Override
	public long getLatency() {
		return this.latency.getLatency();
	}
	
	public IntervalLatencyMining clone(){
		return new IntervalLatencyMining(this);
	}
	
	@Override
	public String toString() {
		return "( i= " + super.toString() + " ; " + " l=" + this.latency + ""
				+ " ; p=" + this.mining + ")";
	}
}
