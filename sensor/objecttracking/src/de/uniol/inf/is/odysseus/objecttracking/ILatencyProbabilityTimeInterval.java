package de.uniol.inf.is.odysseus.objecttracking;

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public interface ILatencyProbabilityTimeInterval extends ILatency,
		IProbability, ITimeInterval {

}
