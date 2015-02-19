package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawIdTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.textual.VectorTextualDistance;

public abstract class AbstractTrajectoryCompareAlgoritm<T extends IDataTrajectory<E>, E> implements ITrajectoryCompareAlgorithm<T, E> {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractTrajectoryCompareAlgoritm.class);
	
	private final IQueryTrajectory<E> queryTrajectory;
	
	private final List<IDataTrajectory<E>> trajectories = new LinkedList<>();
	
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
	public void removeBefore(PointInTime time) {
		
		final List<IDataTrajectory<E>> toBeRemoved = new ArrayList<>();
		
		for(final IDataTrajectory<E> traj : this.trajectories) {
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
	public List<T> getKNearest(RawIdTrajectory trajectory) {
		final IDataTrajectory<E> converted = this.convert(trajectory);
		this.trajectories.add(converted);
		return (List<T>)this.distanceService.getDistance(this.queryTrajectory, converted);
	}
	
	protected abstract ISpatialDistance<E> createDistanceService();
	
	protected abstract IQueryTrajectory<E> convert(final RawQueryTrajectory trajectory, final Map<String, String> textualAttributes,
			int utmZone, final Map<String, String> options);
	
	protected abstract IDataTrajectory<E> convert(final RawIdTrajectory queryTrajectory);
}
