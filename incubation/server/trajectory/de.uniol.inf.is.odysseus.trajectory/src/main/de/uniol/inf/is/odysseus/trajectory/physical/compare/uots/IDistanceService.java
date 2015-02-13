package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.List;

public interface IDistanceService {

	public void addQueryTrajectory(UotsTrajectory queryTrajectory, int k) throws IllegalArgumentException;
	
	public List<UotsTrajectory> getDistance(UotsTrajectory queryTrajectory, UotsTrajectory dataTrajectory);
	
	public void removeTrajectory(UotsTrajectory queryTrajectory, UotsTrajectory dataTrajectory);
}
