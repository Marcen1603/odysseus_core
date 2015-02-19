package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;

/**
 * 
 * @author marcus
 *
 */
public interface ITrajectoryCompareAlgorithm<T extends IConvertedDataTrajectory<E>, E> {

	
	public List<T> getKNearest(RawDataTrajectory trajectory);
	
	public void removeBefore(PointInTime time);
}
