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
 * Abstract base implementation of 
 * @author marcus
 *
 * @param <T>
 * @param <E>
 */
public abstract class AbstractTrajectoryCompareAlgoritm<T extends IConvertedDataTrajectory<E>, E> implements ITrajectoryCompareAlgorithm<T, E> {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractTrajectoryCompareAlgoritm.class);
	
	private final IConvertedQueryTrajectory<E> queryTrajectory;
	
	private final List<IConvertedDataTrajectory<E>> trajectories = new LinkedList<>();
	
	private final IDistanceService<E> distanceService;
	
	protected AbstractTrajectoryCompareAlgoritm(final Map<String, String> options, final int k,
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
	
	protected abstract ISpatialDistance<E> createDistanceService();
	
	protected abstract IConvertedQueryTrajectory<E> convert(final RawQueryTrajectory trajectory, final Map<String, String> textualAttributes,
			int utmZone, final Map<String, String> options);
	
	protected abstract IConvertedDataTrajectory<E> convert(final RawDataTrajectory queryTrajectory);
}
