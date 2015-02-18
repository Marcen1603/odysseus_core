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

import de.uniol.inf.is.odysseus.trajectory.compare.data.IDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.textual.ITextualDistance;

public class SpatialDistanceService<T> implements IDistanceService<T> {

	private final static Logger LOGGER = LoggerFactory.getLogger(SpatialDistanceService.class);
	
	private final Map<IQueryTrajectory<T>, QueryTrajectoryData> trajectoryMap = new HashMap<>();
	
	private final ISpatialDistance<T> spatialDistance;
	private final ITextualDistance textualDistance;
	
	public SpatialDistanceService(final ISpatialDistance<T> spatialDistance, final ITextualDistance textualDistance) {
		this.spatialDistance = spatialDistance;
		this.textualDistance = textualDistance;
	}
	
	@Override
	public synchronized void addQueryTrajectory(final IQueryTrajectory<T> queryTrajectory, final int k, 
			final double lambda) throws IllegalArgumentException {
		
		if(this.trajectoryMap.containsKey(queryTrajectory)) {
			throw new IllegalArgumentException("queryTrajectory already added" );
		}
		this.trajectoryMap.put(queryTrajectory, new QueryTrajectoryData(k, lambda));
		LOGGER.debug("New query trajectory added.");
	}
	
	@Override
	public void removeTrajectory(final IQueryTrajectory<T> queryTrajectory,
			final IDataTrajectory<T> dataTrajectory) {
		this.trajectoryMap.get(queryTrajectory).kNearest.remove(dataTrajectory);
	}
	
	
	@Override
	public List<IDataTrajectory<T>> getDistance(final IQueryTrajectory<T> queryTrajectory, 
			final IDataTrajectory<T> dataTrajectory) {
		
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

	private List<IDataTrajectory<T>> computeResult(final QueryTrajectoryData qData) {
		final List<IDataTrajectory<T>> result = new ArrayList<>(qData.k);
		final Iterator<IDataTrajectory<T>> it = qData.kNearest.iterator();
		result.add(it.next());
		for (int i = 1; i < qData.k && it.hasNext(); i++) {
			result.add(it.next());
		}
		return result;
	}
	
	/**
	 * 
	 * @author marcus
	 *
	 */
	private final class QueryTrajectoryData {
		
		private final int k;
		
		private final double lambda;
		
		private final SortedMultiset<IDataTrajectory<T>> kNearest = TreeMultiset.create(new Comparator<IDataTrajectory<T>>() {
			@Override
			public int compare(IDataTrajectory<T> o1, IDataTrajectory<T> o2) {
				if(o1.getDistance() >= o2.getDistance()) {
					return 1;
				}
				return -1;
			}
		});
		
		private QueryTrajectoryData(final int k, final double lambda) {
			this.k = k;
			this.lambda = lambda;
		}
	}
}
