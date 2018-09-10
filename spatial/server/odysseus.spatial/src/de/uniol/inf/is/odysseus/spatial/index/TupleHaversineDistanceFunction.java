package de.uniol.inf.is.odysseus.spatial.index;

import com.eatthepath.jvptree.DistanceFunction;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.utilities.MetricSpatialUtils;

/**
 * A distance function (in meters) for tuples with a spatial point.
 * 
 * @author Tobias Brandt
 *
 */
public class TupleHaversineDistanceFunction<T extends Tuple<?>> implements DistanceFunction<T> {

	private final int POINT_ATTR;

	public TupleHaversineDistanceFunction(int pointAttributeIndex) {
		POINT_ATTR = pointAttributeIndex;
	}

	@Override
	public double getDistance(T firstPoint, T secondPoint) {
		GeometryWrapper geometryWrapper1 = firstPoint.getAttribute(POINT_ATTR);
		GeometryWrapper geometryWrapper2 = secondPoint.getAttribute(POINT_ATTR);
		return MetricSpatialUtils.getInstance().calculateDistance(geometryWrapper1.getGeometry().getCoordinate(),
				geometryWrapper2.getGeometry().getCoordinate());
	}

}
