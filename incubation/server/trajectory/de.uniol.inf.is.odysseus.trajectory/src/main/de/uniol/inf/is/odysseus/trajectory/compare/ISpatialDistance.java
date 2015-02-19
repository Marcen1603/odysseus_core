package de.uniol.inf.is.odysseus.trajectory.compare;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedTrajectory;


public interface ISpatialDistance<T> {

	public double getDistance(IConvertedTrajectory<T, ?> queryTrajectory, IConvertedTrajectory<T, ?> dataTrajectory);
}
