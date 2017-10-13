package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;

public class ExtendedRadiusEstimatior implements Estimator {
	
	private IMovingObjectDataStructure index;
	private double radiusExtensionFactor;
	
	public ExtendedRadiusEstimatior(IMovingObjectDataStructure index, double radiusExtensionFactor) {
		this.index = index;
		this.radiusExtensionFactor = radiusExtensionFactor;
	}

	@Override
	public Set<String> estimateObjectsToPredict(String centerObjectId, double radius, PointInTime targetTime) {
		Map<String, List<ResultElement>> queryCircleWOPrediction = this.index
				.queryCircleWOPrediction(centerObjectId, radius * this.radiusExtensionFactor);
		return queryCircleWOPrediction.keySet();
	}

}
