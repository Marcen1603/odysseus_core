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

public class SpatialTextualDistanceService<T> implements IDistanceService<T> {

	private final static Logger LOGGER = LoggerFactory.getLogger(SpatialTextualDistanceService.class);
	
	private final Map<IConvertedQueryTrajectory<T>, QueryTrajectoryData> trajectoryMap = new HashMap<>();
	
	private final ISpatialDistance<T> spatialDistance;
	private final ITextualDistance textualDistance;
	
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
	 * @author marcus
	 *
	 */
	private final class QueryTrajectoryData {
		
		private final int k;
		
		private final double lambda;
		
		private final SortedMultiset<IConvertedDataTrajectory<T>> kNearest = TreeMultiset.create(new Comparator<IConvertedDataTrajectory<T>>() {
			@Override
			public int compare(IConvertedDataTrajectory<T> o1, IConvertedDataTrajectory<T> o2) {
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
