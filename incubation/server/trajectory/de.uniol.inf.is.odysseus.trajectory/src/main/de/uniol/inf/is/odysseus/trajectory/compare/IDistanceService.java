package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.List;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IQueryTrajectory;

public interface IDistanceService<T> {

	public void addQueryTrajectory(IQueryTrajectory<T> queryTrajectory, int k, double lambda) throws IllegalArgumentException;
	
	public List<IDataTrajectory<T>> getDistance(IQueryTrajectory<T> queryTrajectory, IDataTrajectory<T> dataTrajectory);
	
	public void removeTrajectory(IQueryTrajectory<T> queryTrajectory, IDataTrajectory<T> dataTrajectory);
}
