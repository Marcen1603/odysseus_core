package de.uniol.inf.is.odysseus.scars;

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.metadata.IObjectTrackingLatency;

public interface IProbabilityLatencyObjectTrackingLatencyPredictionFunctionKeyConnectionContainerTimeInterval<K>
		extends IProbability, ILatency, IObjectTrackingLatency,
		IPredictionFunctionKey<K>, IConnectionContainer, ITimeInterval {

}
