package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * An estimator can estimate vessels that could be within the result of a
 * spatio-temporal query.
 * 
 * @author Tobias Brandt
 *
 */
public interface Estimator {

	public Set<String> estimateObjectsToPredict(double centerLatitude, double centerLongitude, double radius,
			PointInTime targetTime);

}
