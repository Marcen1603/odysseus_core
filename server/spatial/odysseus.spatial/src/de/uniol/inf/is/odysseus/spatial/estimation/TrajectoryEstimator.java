package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface TrajectoryEstimator {

	public Set<String> estimateObjectsToPredict(double centerLatitude, double centerLongitude, double radius,
			PointInTime startTime, PointInTime endTime);

}
