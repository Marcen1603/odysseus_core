package de.uniol.inf.is.odysseus.scars;

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.metadata.IConnectionContainer;

public interface IProbabilityConnectionContainerTimeIntervalLatency extends
		IProbability, IConnectionContainer, ITimeInterval, ILatency {

}
