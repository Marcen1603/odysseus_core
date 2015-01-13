package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * 
 * @author marcus
 *
 */
public interface ITrajectoryCompareAlgorithm {

	public int getK();
	
	public Tuple<ITimeInterval> getKNearest(Tuple<ITimeInterval> incoming);
}
