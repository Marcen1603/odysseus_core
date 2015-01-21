package de.uniol.inf.is.odysseus.trajectory.physical.construct;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.trajectory.physical.Trajectory;

public interface ITrajectoryConstructStrategy {
	
	public List<Trajectory> getResultsToTransfer(Tuple<ITimeInterval> incoming);
	
	public void process_close();
}
