package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawIdTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.IDistanceService;

public abstract class AbstractTrajectoryCompareAlgoritm<T extends IDataTrajectory<E>, E> implements ITrajectoryCompareAlgorithm<T, E> {

	private final IQueryTrajectory<E> queryTrajectory;
	
	private final List<IDataTrajectory<E>> trajectories = new LinkedList<>();
	
	private final IDistanceService<E> distanceService;
	
	protected AbstractTrajectoryCompareAlgoritm(final Map<String, String> options, final int k,
			final RawQueryTrajectory queryTrajectory, final Map<String, String> textualAttributes,
			final int utmZone, final double lambda) {
		
		this.queryTrajectory = this.convert(queryTrajectory, textualAttributes, utmZone, options);
		this.distanceService = this.createDistanceService();
		this.distanceService.addQueryTrajectory(this.queryTrajectory, k, lambda);
	}	
	
	@Override
	public void removeBefore(PointInTime time) {
		for(final IDataTrajectory<E> traj : this.trajectories) {
			if(traj.getRawTrajectory().getTimeInterval().getEnd().afterOrEquals(time)) {
				break;
			}
			this.distanceService.removeTrajectory(this.queryTrajectory, traj);
		}
	}
	
	@Override
	public List<T> getKNearest(RawIdTrajectory trajectory) {
		return (List<T>)this.distanceService.getDistance(this.queryTrajectory, this.convert(trajectory));
	}
	
	protected abstract IDistanceService<E> createDistanceService();
	
	protected abstract IQueryTrajectory<E> convert(final RawQueryTrajectory trajectory, final Map<String, String> textualAttributes,
			int utmZone, final Map<String, String> options);
	
	protected abstract IDataTrajectory<E> convert(final RawIdTrajectory queryTrajectory);
}
