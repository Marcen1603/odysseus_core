package de.uniol.inf.is.odysseus.trajectory.physical;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public interface ITrajectoryConstructStrategy {
	
	public List<RouteConstructResult> getResultsToTransfer(Tuple<ITimeInterval> incoming);
}
