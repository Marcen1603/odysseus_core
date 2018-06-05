package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;
import de.uniol.inf.is.odysseus.spatial.index.SpatialIndex;

public class ExtendedRadiusEstimatior implements Estimator {

	@SuppressWarnings("rawtypes")
	private SpatialIndex index;
	private double radiusExtensionFactor;

	public ExtendedRadiusEstimatior(@SuppressWarnings("rawtypes") SpatialIndex index, double radiusExtensionFactor) {
		this.index = index;
		this.radiusExtensionFactor = radiusExtensionFactor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> estimateObjectsToPredict(double centerLatitude, double centerLongitude, double radius,
			PointInTime targetTime) {
		Map<String, ResultElement> queryCircleOnLatestElements = this.index.queryCircleOnLatestElements(centerLatitude,
				centerLongitude, radius * this.radiusExtensionFactor, null);
		return queryCircleOnLatestElements.keySet();
	}

}
