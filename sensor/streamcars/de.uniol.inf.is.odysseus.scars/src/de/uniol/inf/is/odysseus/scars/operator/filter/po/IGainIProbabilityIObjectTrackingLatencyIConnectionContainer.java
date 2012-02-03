package de.uniol.inf.is.odysseus.scars.operator.filter.po;

import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.metadata.IObjectTrackingLatency;

public interface IGainIProbabilityIObjectTrackingLatencyIConnectionContainer
		extends IGain, IProbability, IObjectTrackingLatency,
		IConnectionContainer {

}
