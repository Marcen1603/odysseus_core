package de.uniol.inf.is.odysseus.trajectory.compare;

import de.uniol.inf.is.odysseus.trajectory.compare.data.ITrajectory;


public interface ISpatialDistance<T> {

	public double getDistance(ITrajectory<T, ?> queryTrajectory, ITrajectory<T, ?> dataTrajectory);
}
