package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;

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

				// Distance calculation is a bit difficult as we don't always
				// know the reference system

				// Let us use the WGS84 system for now. It's not a perfect
				// calculation, hence we have to improve it with geotools later
				// on

				// double angularUnit = tupleGeometry.distance(geometry);
				// double realDistance = angularUnit * (Math.PI / 180) *
				// 6378137;

				double realDistance = calculateDistance(tupleGeometry.getCentroid().getX(),
						tupleGeometry.getCentroid().getY(), geometry.getCentroid().getX(),
						geometry.getCentroid().getY());

				// TODO Use geotools or apache sis for this purpose?

				if (realDistance <= range) {
					rangeTuples.add(tuple);

					System.out.println("Angular unit: " + tupleGeometry.distance(geometry) + ", real distance: "
							+ realDistance + " m");
				}

			}
		}
		return rangeTuples;
	}

	/**
	 * The haversine formula to calculate the distance between two points on the
	 * earth. Algorithm from
	 * http://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
	 * 
	 * @param lat1
	 *            First latitude value
	 * @param lng1
	 *            First longitude value
	 * @param lat2
	 *            Second latitude value
	 * @param lng2
	 *            Second longitude value
	 * @return Distance between the two points in meters
	 */
	public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {

		double latDistance = Math.toRadians(lat1 - lat2);
		double lngDistance = Math.toRadians(lng1 - lng2);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return Math.round(AVERAGE_RADIUS_OF_EARTH * c);
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

}
