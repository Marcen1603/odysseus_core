package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * 
 * @author marcus
 *
 */
public interface ITrajectoryCompareAlgorithm<T extends AbstractAdvancedTrajectory> {

	public int getK();
	
	public List<T> getKNearest(RawTrajectory incoming);
	
	public void removeBefore(PointInTime time);
}
