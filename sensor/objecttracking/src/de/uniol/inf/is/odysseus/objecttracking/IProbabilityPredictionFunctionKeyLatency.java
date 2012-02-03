package de.uniol.inf.is.odysseus.objecttracking;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public interface IProbabilityPredictionFunctionKeyLatency<K> extends IProbability,
		IPredictionFunctionKey<K>, ILatency {

}
