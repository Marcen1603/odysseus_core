package de.uniol.inf.is.odysseus.objecttracking;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public interface IPredictionFunctionKeyTimeIntervalProbabilityApplicationTime<K>
		extends IPredictionFunctionKey<K>, ITimeInterval, IProbability,
		IApplicationTime {
}
