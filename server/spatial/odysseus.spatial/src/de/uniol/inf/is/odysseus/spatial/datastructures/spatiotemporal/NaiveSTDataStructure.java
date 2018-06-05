package de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.utilities.MetricSpatialUtils;

/**
 * A very simple implementation for a spatial data structure. No performance
 * improvements are used.
 * 
 * @author Tobias Brandt
 *
 */
@Deprecated
public class NaiveSTDataStructure implements ISpatioTemporalDataStructure {

	public static final String TYPE = "naive";

	private int geometryPosition;
	private String name;

	private DefaultTISweepArea<Tuple<ITimeInterval>> sweepArea;

	public NaiveSTDataStructure(String name, int geometryPosition)
			throws InstantiationException, IllegalAccessException {
		this.name = name;
		this.geometryPosition = geometryPosition;
		this.sweepArea = new DefaultTISweepArea<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(Object o) {
		if (o instanceof Tuple<?>) {
			this.sweepArea.insert((Tuple<ITimeInterval>) o);
		}
	}

	@Override
	public void cleanUp(PointInTime timestamp) {
		this.sweepArea.extractElementsBefore(timestamp);
	}

	// TODO Different distance functions should be possible (e.g. city distance
	// metric, Haversine, ellipsoid, Euklidean, ...)
	@Override
	public List<Tuple<ITimeInterval>> queryKNN(Geometry geometry, int k, ITimeInterval t) {

		List<Tuple<ITimeInterval>> elements = this.sweepArea.queryOverlapsAsList(t);
		// Just copy the list ...
		List<Tuple<ITimeInterval>> sortedTuples = new ArrayList<Tuple<ITimeInterval>>(elements);
		// and sort the list by the distance to the given point
		sortedTuples.sort(new Comparator<Tuple<ITimeInterval>>() {

			@Override
			public int compare(Tuple<ITimeInterval> o1, Tuple<ITimeInterval> o2) {
				// Calculate the distance of both tuples to the center (here we
				// can use this distance calculation, as we are only interested
				// in the distance comparison, not in the real distances)
				double distance1 = getGeometry(o1).distance(geometry);
				double distance2 = getGeometry(o2).distance(geometry);

				if (distance1 < distance2) {
					return -1;
				} else if (distance1 > distance2) {
					return 1;
				} else {
					return 0;
				}

			}

			private Geometry getGeometry(Tuple<?> tuple) {
				Object o = tuple.getAttribute(geometryPosition);
				GeometryWrapper geometryWrapper = null;
				if (o instanceof GeometryWrapper) {
					geometryWrapper = (GeometryWrapper) o;
					Geometry geometry = geometryWrapper.getGeometry();
					return geometry;
				} else {
					return null;
				}
			}

		});

		// Now we can return the first k elements from the sorted list
		if (sortedTuples.size() < k) {
			// Maybe we have less than k neighbors because the number of
			// elements is too small
			return sortedTuples;
		}
		// Very important: Do not return the subList itself as it is only a view
		return new ArrayList<Tuple<ITimeInterval>>(sortedTuples.subList(0, k));

	}

	public List<Tuple<ITimeInterval>> queryCircle(Geometry geometry, double range, ITimeInterval t) {

		List<Tuple<ITimeInterval>> elements = this.sweepArea.queryOverlapsAsList(t);

		List<Tuple<ITimeInterval>> rangeTuples = new ArrayList<Tuple<ITimeInterval>>();

		for (Tuple<ITimeInterval> tuple : elements) {
			Geometry tupleGeometry = getGeometry(tuple);

			// TODO Use the right coordinate reference system
			MetricSpatialUtils spatialUtils = MetricSpatialUtils.getInstance();
			double realDistance = spatialUtils.calculateDistance(null, geometry.getCentroid().getCoordinate(),
					tupleGeometry.getCentroid().getCoordinate());

			if (realDistance <= range) {
				rangeTuples.add(tuple);
			}
		}
		return rangeTuples;
	}

	@Override
	public List<Tuple<ITimeInterval>> queryBoundingBox(List<Point> polygonPoints, ITimeInterval t) {

		// Get all elements that are in the given time interval
		List<Tuple<ITimeInterval>> elements = this.sweepArea.queryOverlapsAsList(t);

		// Check if the first and the last point is equal. If not, we have to
		// add the first point at the end to have a closed ring.
		Point firstPoint = polygonPoints.get(0);
		Point lastPoint = polygonPoints.get(polygonPoints.size() - 1);
		if (!firstPoint.equals(lastPoint)) {
			polygonPoints.add(firstPoint);
		}

		// Create a polygon with the given points
		GeometryFactory factory = new GeometryFactory();
		LinearRing ring = factory.createLinearRing(
				polygonPoints.stream().map(p -> p.getCoordinate()).toArray(size -> new Coordinate[size]));
		Polygon polygon = factory.createPolygon(ring, null);

		// For every point in our list ask JTS if the points lies within the
		// polygon
		List<Tuple<ITimeInterval>> result = null;
		result = elements.parallelStream().filter(e -> polygon.contains(getGeometry(e))).collect(Collectors.toList());

		// Collect the points and return them
		return result;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getGeometryPosition() {
		return geometryPosition;
	}

	private Geometry getGeometry(Tuple<?> tuple) {
		Object o = tuple.getAttribute(geometryPosition);
		GeometryWrapper geometryWrapper = null;
		if (o instanceof GeometryWrapper) {
			geometryWrapper = (GeometryWrapper) o;
			Geometry geometry = geometryWrapper.getGeometry();
			return geometry;
		} else {
			return null;
		}
	}

}
