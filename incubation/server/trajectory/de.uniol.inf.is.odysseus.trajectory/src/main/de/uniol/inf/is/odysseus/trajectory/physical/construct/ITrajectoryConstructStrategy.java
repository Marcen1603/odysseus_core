package de.uniol.inf.is.odysseus.trajectory.physical.construct;

import java.util.Deque;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public interface ITrajectoryConstructStrategy {
	
	public Deque<Deque<Tuple<ITimeInterval>>> getResultsToTransfer(Tuple<ITimeInterval> incoming);
	
	public void process_close();
}
