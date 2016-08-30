package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;

/**
 * A very simple implementation for a spatial dataStructure. No performance
 * improvements are used.
 * 
 * @author Tobias Brandt
 *
 */
public class NaiveSTDataStructure implements IMovingObjectDataStructure {

	public static final String TYPE = "naive";

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

	@Override
	public List<Tuple<?>> getKNN(Geometry geometry, int k) {

		Point centre = geometry.getCentroid();

		// Just copy the list ...
		List<Tuple<?>> sortedTuples = new ArrayList<Tuple<?>>(tuples);
		// and sort the list by the distance to the given point
		sortedTuples.sort(new Comparator<Tuple<?>>() {

			@Override
			public int compare(Tuple<?> o1, Tuple<?> o2) {
				// Calculate the distance of both tuples to the center
				double distance1 = getPoint(o1).distance(centre);
				double distance2 = getPoint(o2).distance(centre);

				if (distance1 < distance2) {
					return -1;
				} else if (distance1 > distance2) {
					return 1;
				} else {
					return 0;
				}

			}

			private Point getPoint(Tuple<?> tuple) {
				Object o = tuple.getAttribute(geometryPosition);
				GeometryWrapper geometryWrapper = null;
				if (o instanceof GeometryWrapper) {
					geometryWrapper = (GeometryWrapper) o;
					Point centre = geometryWrapper.getGeometry().getCentroid();
					return centre;
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
		return sortedTuples.subList(0, k);

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
