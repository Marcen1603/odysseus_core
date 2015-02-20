package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.textual.ITextualDistance;

/**
 * The default implementation of <tt>IDistanceService</tt>.
 * 
 * @author marcus
 *
 * @param <T> the type of the data for the trajectories
 */
public class SpatialTextualDistanceService<T> implements IDistanceService<T> {

	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(SpatialTextualDistanceService.class);
	
	/** the query trajectories and their corresponding data trajectories */
	private final Map<IConvertedQueryTrajectory<T>, QueryTrajectoryData> trajectoryMap = new HashMap<>();
	
	/** for measuring spatial distance -> can be <tt>null</tt> */
	private final ISpatialDistance<T> spatialDistance;
	
	/** for measuring textual distance -> can be <tt>null</tt> */
	private final ITextualDistance textualDistance;
	
	/**
	 * Creates an instance of <tt>SpatialTextualDistanceService</tt>.
	 * 
	 * @param spatialDistance for measuring spatial distance
	 * @param textualDistance for measuring textual distance -> can be <tt>null</tt>
	 */
	public SpatialTextualDistanceService(final ISpatialDistance<T> spatialDistance, final ITextualDistance textualDistance) {
		this.spatialDistance = spatialDistance;
		this.textualDistance = textualDistance;
	}
	
	@Override
	public synchronized void addQueryTrajectory(final IConvertedQueryTrajectory<T> queryTrajectory, final int k, 
			final double lambda) throws IllegalArgumentException {
		
		if(this.trajectoryMap.containsKey(queryTrajectory)) {
			throw new IllegalArgumentException("queryTrajectory already added" );
		}
		this.trajectoryMap.put(queryTrajectory, new QueryTrajectoryData(k, lambda));
		LOGGER.debug("New query trajectory added.");
	}
	
	@Override
	public void removeTrajectory(final IConvertedQueryTrajectory<T> queryTrajectory,
			final IConvertedDataTrajectory<T> dataTrajectory) {
		this.trajectoryMap.get(queryTrajectory).kNearest.remove(dataTrajectory);
	}
	
	
	@Override
	public List<IConvertedDataTrajectory<T>> getDistance(final IConvertedQueryTrajectory<T> queryTrajectory, 
			final IConvertedDataTrajectory<T> dataTrajectory) {
		
		final QueryTrajectoryData qData = this.trajectoryMap.get(queryTrajectory);

		double result = this.spatialDistance.getDistance(queryTrajectory, dataTrajectory);
		
		if(this.textualDistance != null) {
			result = qData.lambda * result +
					(1.0 - qData.lambda) * this.textualDistance.getDistance(queryTrajectory, dataTrajectory);
			
		}
		dataTrajectory.setDistance(result);
		
		qData.kNearest.add(dataTrajectory);
		
		return this.computeResult(qData);
	}

	/**
	 * Computes the k-nearest data trajecotries.
	 * @param qData
	 * @return
	 */
	private List<IConvertedDataTrajectory<T>> computeResult(final QueryTrajectoryData qData) {
		final List<IConvertedDataTrajectory<T>> result = new ArrayList<>(qData.k);
		final Iterator<IConvertedDataTrajectory<T>> it = qData.kNearest.iterator();
		result.add(it.next());
		for (int i = 1; i < qData.k && it.hasNext(); i++) {
			result.add(it.next());
		}
		return result;
	}
	
	/**
	 * 
	 * Class for holding parameters for a query trajectory.
	 * 
	 * @author marcus
	 *
	 */
	private final class QueryTrajectoryData {
		
		/** the k-nearest trajectories to find */
		private final int k;
		
		/** the importance between spatial and textual distance */
		private final double lambda;
		
		/** stores the k-nearest data trajectories */
		private final SortedMultiset<IConvertedDataTrajectory<T>> kNearest = TreeMultiset.create(new Comparator<IConvertedDataTrajectory<T>>() {
			@Override
			public int compare(final IConvertedDataTrajectory<T> o1, final IConvertedDataTrajectory<T> o2) {
				if(o1.getDistance() >= o2.getDistance()) {
					return 1;
				}
				return -1;
			}
		});
		
		/**
		 * Creates an instance of <tt>QueryTrajectoryData</tt>.
		 * @param k the k-nearest trajectories to find
		 * @param lambda the importance between spatial and textual distance
		 */
		private QueryTrajectoryData(final int k, final double lambda) {
			this.k = k;
			this.lambda = lambda;
		}
	}
}
