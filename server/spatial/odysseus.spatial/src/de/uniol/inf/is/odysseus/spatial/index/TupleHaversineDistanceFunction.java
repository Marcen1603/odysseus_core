package de.uniol.inf.is.odysseus.spatial.index;

import com.eatthepath.jvptree.DistanceFunction;
import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.spatial.utilities.MetrticSpatialUtils;

public class TupleHaversineDistanceFunction<T extends Tuple<?>> implements DistanceFunction<T> {

	private final int LONG_ATTR;
	private final int LAT_ATTR;

	public TupleHaversineDistanceFunction(int latitudeAttribute, int longitudeAttribute) {
		LONG_ATTR = longitudeAttribute;
		LAT_ATTR = latitudeAttribute;
	}

	@Override
	public double getDistance(T firstPoint, T secondPoint) {
		double latitude1 = firstPoint.getAttribute(LAT_ATTR);
		double longitude1 = firstPoint.getAttribute(LONG_ATTR);

		double latitude2 = secondPoint.getAttribute(LAT_ATTR);
		double longitude2 = secondPoint.getAttribute(LONG_ATTR);

		Coordinate coord1 = new Coordinate(latitude1, longitude1);
		Coordinate coord2 = new Coordinate(latitude2, longitude2);

		return MetrticSpatialUtils.getInstance().calculateDistance(coord1, coord2);
	}

}
