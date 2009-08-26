package de.uniol.inf.is.odysseus.latency;

import de.uniol.inf.is.odysseus.base.IClone;

public interface ILatency extends IClone{
	public void setLatencyStart(long timestamp);
	public void setLatencyEnd(long timestamp);
	public long getLatencyStart();
	public long getLatencyEnd();
	public long getLatency();
}
