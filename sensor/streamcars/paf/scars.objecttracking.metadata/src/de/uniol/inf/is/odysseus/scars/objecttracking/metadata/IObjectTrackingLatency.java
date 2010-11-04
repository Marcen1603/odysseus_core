package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public interface IObjectTrackingLatency extends IMetaAttribute {

	public void setObjectTrackingLatencyStart();
	public void setObjectTrackingLatencyEnd();
	public long getObjectTrackingLatency();
	@Override
	public ILatency clone();

}
