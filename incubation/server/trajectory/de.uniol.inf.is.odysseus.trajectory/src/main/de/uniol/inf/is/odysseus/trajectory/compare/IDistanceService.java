package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.List;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedQueryTrajectory;

public interface IDistanceService<T> {

	public void addQueryTrajectory(IConvertedQueryTrajectory<T> queryTrajectory, int k, double lambda) throws IllegalArgumentException;
	
	public List<IConvertedDataTrajectory<T>> getDistance(IConvertedQueryTrajectory<T> queryTrajectory, IConvertedDataTrajectory<T> dataTrajectory);
	
	public void removeTrajectory(IConvertedQueryTrajectory<T> queryTrajectory, IConvertedDataTrajectory<T> dataTrajectory);
}
