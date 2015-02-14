package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.List;

public interface IDistanceService {

	public void addQueryTrajectory(UotsQueryTrajectory queryTrajectory, int k) throws IllegalArgumentException;
	
	public List<UotsTrajectory> getDistance(UotsQueryTrajectory queryTrajectory, UotsTrajectory dataTrajectory);
	
	public void removeTrajectory(UotsQueryTrajectory queryTrajectory, UotsTrajectory dataTrajectory);
}
