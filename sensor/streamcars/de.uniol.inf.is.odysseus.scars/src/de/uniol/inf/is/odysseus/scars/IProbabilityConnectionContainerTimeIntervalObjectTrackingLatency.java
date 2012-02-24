package de.uniol.inf.is.odysseus.scars;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.metadata.IObjectTrackingLatency;


public interface IProbabilityConnectionContainerTimeIntervalObjectTrackingLatency
		extends IProbability, IConnectionContainer, ITimeInterval,
		IObjectTrackingLatency {

}
