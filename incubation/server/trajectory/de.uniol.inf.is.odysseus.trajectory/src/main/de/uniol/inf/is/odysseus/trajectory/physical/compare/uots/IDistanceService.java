package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.List;

public interface IDistanceService {

	
	public List<UotsTrajectory> getDistance(UotsTrajectory queryTrajectory, UotsTrajectory dataTrajectory, double upperBound, int k);
	
	public void removeTrajectory(UotsTrajectory queryTrajectory);
}
