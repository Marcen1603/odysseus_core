package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.List;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedQueryTrajectory;

/**
 * An interface 
 * 
 * @author marcus
 *
 * @param <T>
 */
public interface IDistanceService<T> {

	/**
	 * 
	 * @param queryTrajectory
	 * @param k
	 * @param lambda
	 * @throws IllegalArgumentException
	 */
	public void addQueryTrajectory(IConvertedQueryTrajectory<T> queryTrajectory, int k, double lambda) throws IllegalArgumentException;
	
	/**
	 * 
	 * @param queryTrajectory
	 * @param dataTrajectory
	 * @return
	 */
	public List<IConvertedDataTrajectory<T>> getDistance(IConvertedQueryTrajectory<T> queryTrajectory, IConvertedDataTrajectory<T> dataTrajectory);
	
	/**
	 * 
	 * @param queryTrajectory
	 * @param dataTrajectory
	 */
	public void removeTrajectory(IConvertedQueryTrajectory<T> queryTrajectory, IConvertedDataTrajectory<T> dataTrajectory);
}
