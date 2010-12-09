package de.uniol.inf.is.odysseus.metadata;


public interface ILatency extends IMetaAttribute{
	public void setLatencyStart(long timestamp);
	public void setLatencyEnd(long timestamp);
	public long getLatencyStart();
	public long getLatencyEnd();
	public long getLatency();
	@Override
	public ILatency clone();
}
