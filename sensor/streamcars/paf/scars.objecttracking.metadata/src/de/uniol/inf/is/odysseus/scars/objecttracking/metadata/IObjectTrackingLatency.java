package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public interface IObjectTrackingLatency extends IMetaAttribute, IClone {

	public void setObjectTrackingLatencyStart();
	public void setObjectTrackingLatencyEnd();
	public long getObjectTrackingLatency();

}
