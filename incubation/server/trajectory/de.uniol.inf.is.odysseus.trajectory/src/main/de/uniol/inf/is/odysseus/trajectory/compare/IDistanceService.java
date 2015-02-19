package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.List;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IQueryTrajectory;

public interface IDistanceService<T> {

	public void addQueryTrajectory(IQueryTrajectory<T> queryTrajectory, int k, double lambda) throws IllegalArgumentException;
	
	public List<IConvertedDataTrajectory<T>> getDistance(IQueryTrajectory<T> queryTrajectory, IConvertedDataTrajectory<T> dataTrajectory);
	
	public void removeTrajectory(IQueryTrajectory<T> queryTrajectory, IConvertedDataTrajectory<T> dataTrajectory);
}
