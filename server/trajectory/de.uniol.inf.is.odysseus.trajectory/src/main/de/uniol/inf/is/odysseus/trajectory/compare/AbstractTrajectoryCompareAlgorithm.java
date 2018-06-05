/*
 * Copyright 2015 Marcus Behrendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/

package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.textual.VectorTextualDistance;

/**
 * Abstract base implementation of <tt>ITrajectoryCompareAlgorithm</tt>.
 * Subclasses only needs to convert <tt>RawTrajectories<tt> and create an
 * <tt>ISpatialDistance</tt>,
 * 
 * @author marcus
 *
 * @param <T>
 * @param <E>
 */
public abstract class AbstractTrajectoryCompareAlgorithm<T extends IConvertedDataTrajectory<E>, E> implements ITrajectoryCompareAlgorithm<T, E> {

	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractTrajectoryCompareAlgorithm.class);
	
	/** the query trajectory */
	private final IConvertedQueryTrajectory<E> queryTrajectory;
	
	/** the received data trajectories */
	private final List<IConvertedDataTrajectory<E>> trajectories = new LinkedList<>();
	
	/** the distance service */
	private final IDistanceService<E> distanceService;
	
	/**
	 * Creates an instance of <tt>AbstractTrajectoryCompareAlgoritm</tt>.
	 * 
	 * @param options the options for the algorithm
	 * @param k the k-nearest trajectories to find
	 * @param queryTrajectory the raw query trajectory
	 * @param textualAttributes textual attributes of the query trajectory
	 * @param utmZone the UTM zone of the trajectories
	 * @param lambda the importance between spatial and textual distance
	 * @return a new <tt>ITrajectoryCompareAlgorithm</tt>
	 */
	protected AbstractTrajectoryCompareAlgorithm(final Map<String, String> options, final int k,
			final RawQueryTrajectory queryTrajectory, final Map<String, String> textualAttributes,
			final int utmZone, final double lambda) {
		
		this.queryTrajectory = this.convert(queryTrajectory, textualAttributes, utmZone, options);
		this.distanceService = 
				new SpatialTextualDistanceService<>(this.createDistanceService(), textualAttributes != null ? VectorTextualDistance.getInstance() : null);
		this.distanceService.addQueryTrajectory(this.queryTrajectory, k, lambda);
	}	
		
	@Override
	public void removeBefore(final PointInTime time) {
				
		final List<IConvertedDataTrajectory<E>> toBeRemoved = new ArrayList<>();
		for(final IConvertedDataTrajectory<E> traj : this.trajectories) {
			if(traj.getRawTrajectory().getTimeInterval().getEnd().before(time)) {
				this.distanceService.removeTrajectory(this.queryTrajectory, traj);
				toBeRemoved.add(traj);
				if(LOGGER.isDebugEnabled()) {
					LOGGER.debug("Removed trajectory with vehicleId \"" + traj.getRawTrajectory().getVehicleId()
							+ "\" and trajectoryNo " + traj.getRawTrajectory().getTrajectoryNumber() + "\"");
				}
			}
		}
		
		this.trajectories.removeAll(toBeRemoved);
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getKNearest(final RawDataTrajectory trajectory) {
		final IConvertedDataTrajectory<E> converted = this.convert(trajectory);
		this.trajectories.add(converted);
		
		return (List<T>)this.distanceService.getDistance(this.queryTrajectory, converted);
	}
	
	/**
	 * Creates and returns <tt>ISpatialDistance</tt>.
	 * 
	 * @return the spatial <tt>ISpatialDistance</tt>
	 */
	protected abstract ISpatialDistance<E> createDistanceService();
	
	/**
	 * 
	 * @param trajectory
	 * @param textualAttributes
	 * @param utmZone
	 * @param options
	 * @return
	 */
	protected abstract IConvertedQueryTrajectory<E> convert(final RawQueryTrajectory trajectory, final Map<String, String> textualAttributes,
			int utmZone, final Map<String, String> options);
	
	/**
	 * Converts a <tt>IConvertedDataTrajectory</tt> and returns the converted <tt>RawDataTrajectory</tt>.
	 * 
	 * @param queryTrajectory the <tt>IConvertedDataTrajectory</tt> to be converted
	 * @return the converted <tt>RawDataTrajectory</tt>
	 */
	protected abstract IConvertedDataTrajectory<E> convert(final RawDataTrajectory queryTrajectory);
}
