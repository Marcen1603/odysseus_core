package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public interface IObjectTrackingLatency extends IMetaAttribute, IClone {

	public void setObjectTrackingLatencyStart();
	public void setObjectTrackingLatencyEnd();
	public long getObjectTrackingLatency();

	public void setObjectTrackingLatencyStart(String operatorId);
	public void setObjectTrackingLatencyEnd(String operatorId);
	public long getObjectTrackingLatency(String operatorId);

	public HashMap<String, Long> getOperatorLatencies();
	public void setOperatorLatencies(HashMap<String, Long> operatorLatencies);
}
