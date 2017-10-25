package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.MovingObjectIndexOld;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;

public class ExtendedRadiusEstimatior implements Estimator {

	private MovingObjectIndexOld index;
	private double radiusExtensionFactor;

	public ExtendedRadiusEstimatior(MovingObjectIndexOld index, double radiusExtensionFactor) {
		this.index = index;
		this.radiusExtensionFactor = radiusExtensionFactor;
	}

	@Override
	public Set<String> estimateObjectsToPredict(double centerLatitude, double centerLongitude, double radius,
			PointInTime targetTime) {
		Map<String, List<ResultElement>> queryCircleWOPrediction = this.index.queryCircleWOPrediction(centerLatitude,
				centerLongitude, radius * this.radiusExtensionFactor, null);
		return queryCircleWOPrediction.keySet();
	}

}
