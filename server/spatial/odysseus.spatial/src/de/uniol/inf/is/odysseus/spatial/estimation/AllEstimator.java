package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * Estimates always all elements within a given set.
 * 
 * @author Tobias Brandt
 *
 */
public class AllEstimator implements Estimator {

	private Set<String> allIDs;

	public AllEstimator(Set<String> allIDs) {
		this.allIDs = allIDs;
	}

	@Override
	public Set<String> estimateObjectsToPredict(double centerLatitude, double centerLongitude, double radius, PointInTime targetTime) {
		return this.allIDs;
	}

}
