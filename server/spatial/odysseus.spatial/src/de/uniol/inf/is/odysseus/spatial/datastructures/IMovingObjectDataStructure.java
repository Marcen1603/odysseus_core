package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.spatial.listener.ISpatialListener;

/**
 * Interface for all spatial data structures / moving object data structures.
 * 
 * @author Tobias Brandt
 *
 */
public interface IMovingObjectDataStructure {

	/**
	 * Adds an object to the data structure
	 * 
	 * @param o
	 *            The object to add to the data structure
	 */
	public void add(Object o);

	/**
	 * Add a listener to the data structure. The listener is notified when the
	 * data in the data structure changes.
	 * 
	 * @param listener
	 */
	public void addListener(ISpatialListener listener);

	/**
	 * Removes the given listener from the listeners so that it is no longer
	 * notifies when changed in the data structure happen.
	 * 
	 * @param listener
	 *            The listener that is removed.
	 */
	public void removeListener(ISpatialListener listener);

	/**
	 * The name of the data structure so that it can be accessed, e.g. via the
	 * SpatialDataStructureProvider.
	 * 
	 * @return The name of the data structure
	 */
	public String getName();

	/**
	 * Calculates the k nearest neighbors of the given geometry and returns a
	 * list of tuples. The list can be smaller, if there are not enough
	 * neighbors (i.e. if the total list of elements is smaller than k).
	 * 
	 * @param geometry
	 *            The geometry for which the k nearest neighbors are searched
	 * 
	 * @param k
	 *            The number of neighbors
	 * 
	 * @param t
	 *            The time interval of the element where we search the neighbors
	 *            for. Only neighbors are considered which intervals overlap
	 *            this time interval.
	 * 
	 * @return A list of tuples which are the nearest neighbors of the given
	 *         geometry
	 */
	public List<Tuple<?>> getKNN(Geometry geometry, int k, ITimeInterval t);

	/**
	 * Calculates all neighbors within the given range around the geometry.
	 * 
	 * @param geometry
	 *            The geometry around which you want to know the neighborhood
	 * @param range
	 *            The range in meters around the geometry
	 * @param t
	 *            The time interval of the element where we search the neighbors
	 *            for. Only neighbors are considered which intervals overlap
	 *            this time interval.
	 * @return A list with all tuples for which their geometry is in the
	 *         neighborhood around the given geometry
	 */
	public List<Tuple<?>> getNeighborhood(Geometry geometry, double range, ITimeInterval t);

	/**
	 * Queries the data structure and returns all data points which are in a
	 * polygon with the edges at the given coordinates
	 * 
	 * @param coordinates
	 *            The points of the edges of the polygon where you want to
	 *            search in
	 * @param t
	 *            The time interval of the element where we search the neighbors
	 *            for. Only neighbors are considered which intervals overlap
	 *            this time interval.
	 * @return A list of tuples which lie within the given polygon
	 */
	public List<Tuple<?>> queryBoundingBox(List<Point> coordinates, ITimeInterval t);

	int getGeometryPosition();
}
