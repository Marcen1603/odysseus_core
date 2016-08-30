package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
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
	 * Calculates the k nearest neighbors of the given geometry (of the centroid
	 * of the geometry) and returns a list of tuples. The list can be smaller,
	 * if there are not enough neighbors (i.e. if the total list of elements is
	 * smaller than k).
	 * 
	 * @param geometry
	 *            The geometry for which the k nearest neighbors are searched
	 *            (the centroid of the geometry is used for distance
	 *            calculations)
	 * @param k
	 *            The number of neighbors
	 * @return A list of tuples which are the nearest neighbors of the given
	 *         geometry
	 */
	public List<Tuple<?>> getKNN(Geometry geometry, int k);
}
