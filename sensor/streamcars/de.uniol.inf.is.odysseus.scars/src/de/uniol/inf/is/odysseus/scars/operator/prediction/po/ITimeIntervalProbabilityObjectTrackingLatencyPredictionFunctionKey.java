package de.uniol.inf.is.odysseus.scars.operator.prediction.po;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.metadata.IObjectTrackingLatency;

public interface ITimeIntervalProbabilityObjectTrackingLatencyPredictionFunctionKey<K>
		extends ITimeInterval, IProbability, IObjectTrackingLatency,
		IPredictionFunctionKey<K> {

}
