package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;

/**
 * An interface for all algorithm that can compare trajectories.
 * 
 * @author marcus
 *
 */
public interface ITrajectoryCompareAlgorithm<T extends IConvertedDataTrajectory<E>, E> {

	/**
	 * Inserts the specified <i>trajectory</i> and returns the k-nearest trajectories.
	 * @param trajectory the trajectory to insert
	 * @return the k-nearest trajectories
	 */
	public List<T> getKNearest(RawDataTrajectory trajectory);
	
	/**
	 * Removes all trajectories which end timestamp is less than <i>time</i>.
	 * 
	 * @param time
	 */
	public void removeBefore(PointInTime time);
}
