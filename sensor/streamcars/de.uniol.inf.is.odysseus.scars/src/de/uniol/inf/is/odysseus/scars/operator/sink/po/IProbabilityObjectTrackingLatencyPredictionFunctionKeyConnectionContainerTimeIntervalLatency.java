package de.uniol.inf.is.odysseus.scars.operator.sink.po;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.metadata.IObjectTrackingLatency;

public interface IProbabilityObjectTrackingLatencyPredictionFunctionKeyConnectionContainerTimeIntervalLatency<K>
		extends IProbability, IObjectTrackingLatency,
		IPredictionFunctionKey<K>, IConnection, ITimeInterval, ILatency {

}
