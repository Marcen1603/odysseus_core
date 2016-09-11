package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;
import de.uniol.inf.is.odysseus.spatial.utilities.SpatialUtils;

/**
 * A very simple implementation for a spatial data structure. No performance
 * improvements are used.
 * 
 * @author Tobias Brandt
 *
 */
public class NaiveSTDataStructure implements IMovingObjectDataStructure {

	public static final String TYPE = "naive";

	public final static double AVERAGE_RADIUS_OF_EARTH = 6378137;

	private List<Tuple<?>> tuples;
	private int geometryPosition;
	private String name;

	private List<ISpatialListener> listeners = new ArrayList<ISpatialListener>();

	public NaiveSTDataStructure(String name, int geometryPosition) {
		tuples = new ArrayList<Tuple<?>>();
		this.name = name;
		this.geometryPosition = geometryPosition;
	}

	@Override
	public void add(Object o) {
		if (o instanceof Tuple<?>) {
			tuples.add((Tuple<?>) o);
			notifyListeners();
		}
	}

	// TODO Different distance functions should be possible (e.g. city distance
	// metric, ...)
	@Override
	public List<Tuple<?>> getKNN(Geometry geometry, int k) {

		// Just copy the list ...
		List<Tuple<?>> sortedTuples = new ArrayList<Tuple<?>>(tuples);
		// and sort the list by the distance to the given point
		sortedTuples.sort(new Comparator<Tuple<?>>() {

			@Override
			public int compare(Tuple<?> o1, Tuple<?> o2) {
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
		return new ArrayList<Tuple<?>>(sortedTuples.subList(0, k));

	}

	public List<Tuple<?>> getNeighborhood(Geometry geometry, double range) {

		List<Tuple<?>> rangeTuples = new ArrayList<Tuple<?>>();

		for (Tuple<?> tuple : tuples) {
			Object o = tuple.getAttribute(geometryPosition);
			GeometryWrapper geometryWrapper = null;
			if (o instanceof GeometryWrapper) {
				geometryWrapper = (GeometryWrapper) o;
				Geometry tupleGeometry = geometryWrapper.getGeometry();

				// TODO Use the right coordinate reference system
				SpatialUtils spatialUtils = SpatialUtils.getInstance();
				double realDistance = spatialUtils.calculateDistance(null, geometry.getCentroid().getCoordinate(),
						tupleGeometry.getCentroid().getCoordinate());

				if (realDistance <= range) {
					rangeTuples.add(tuple);
				}

			}
		}
		return rangeTuples;
	}

	public List<Tuple<?>> queryBoundingBox(List<Coordinate> coordinates) {

		// Create a polygon with the given points
		GeometryFactory factory = new GeometryFactory();
		LinearRing ring = factory.createLinearRing(coordinates.toArray(new Coordinate[1]));
		Polygon polygon = factory.createPolygon(ring, null);

		// For every point in our list ask JTS if the points lies within the
		// polygon
		List<Tuple<?>> result = tuples.parallelStream().filter(e -> polygon.contains(getGeometry(e)))
				.collect(Collectors.toList());

		// Collect the points and return them
		return result;
	}

	@Override
	public void addListener(ISpatialListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(ISpatialListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all listeners that the data structure changed
	 */
	private void notifyListeners() {
		for (ISpatialListener listener : listeners) {
			listener.onMovingObjectDataStructureChange(this);
		}
	}

	@Override
	public String getName() {
		return this.name;
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
