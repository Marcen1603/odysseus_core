package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawIdTrajectory;

/**
 * 
 * @author marcus
 *
 */
public interface ITrajectoryCompareAlgorithm<T extends IDataTrajectory<E>, E> {

	
	public List<T> getKNearest(RawIdTrajectory trajectory);
	
	public void removeBefore(PointInTime time);
}
