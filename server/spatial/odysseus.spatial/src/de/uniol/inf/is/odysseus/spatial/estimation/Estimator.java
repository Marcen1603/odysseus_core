package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface Estimator {
	
	public Set<String> estimateObjectsToPredict(String centerObjectId, double radius, PointInTime targetTime);

}
