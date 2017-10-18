package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class AllEstimator implements Estimator {
	
	private Set<String> allIDs;
	
	public AllEstimator(Set<String> allIDs) {
		this.allIDs = allIDs;
	}

	@Override
	public Set<String> estimateObjectsToPredict(String centerObjectId, double radius, PointInTime targetTime) {
		return this.allIDs;
	}

}
