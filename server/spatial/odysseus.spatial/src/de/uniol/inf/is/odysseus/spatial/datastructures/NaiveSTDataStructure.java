package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;

/**
 * 
 * @author Tobias Brandt
 *
 */
public class NaiveSTDataStructure implements IMovingObjectDataStructure {

	private List<Tuple<?>> tuples;

	private String name;

	private List<ISpatialListener> listeners = new ArrayList<ISpatialListener>();

	public NaiveSTDataStructure(String name) {
		tuples = new ArrayList<Tuple<?>>();
		this.name = name;
	}

	@Override
	public void add(Object o) {
		if (o instanceof Tuple<?>) {
			tuples.add((Tuple<?>) o);
			notifyListeners();
		}
	}

	public List<Tuple<?>> getKNN(Tuple<?> tuple, int geometryPosition, int k) {

		// Get the point from which we want to get the neighbors
		Object o = tuple.getAttribute(geometryPosition);
		GeometryWrapper geometryWrapper = null;
		if (o instanceof GeometryWrapper) {
			geometryWrapper = (GeometryWrapper) o;
		} else {
			return null;
		}

		Point centre = geometryWrapper.getGeometry().getCentroid();

		// Just copy the list ...
		List<Tuple<?>> sortedTuples = new ArrayList<Tuple<?>>(tuples);
		// and sort the list by the distance to the given point
		sortedTuples.sort(new Comparator<Tuple<?>>() {

			@Override
			public int compare(Tuple<?> o1, Tuple<?> o2) {
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

	private void notifyListeners() {
		for (ISpatialListener listener : listeners) {
			listener.onMovingObjectDataStructureChange(this);
		}
	}

	public String getName() {
		return this.name;
	}

}
