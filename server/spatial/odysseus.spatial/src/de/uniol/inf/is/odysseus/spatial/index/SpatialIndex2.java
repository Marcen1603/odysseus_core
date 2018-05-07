package de.uniol.inf.is.odysseus.spatial.index;

import java.util.List;

/**
 * A spatial index for basic spatial operations.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 *            The type of object that is stored in the index.
 */
public interface SpatialIndex2<T extends Object> {

	public void add(T object);

	public boolean remove(T object);

	public List<T> getKNearestNeighbors(T center, int k);

	/**
	 * Returns all objects that are within a certain distance of the center object
	 * (radius query, distance query).
	 */
	public List<T> getWithinDistance(T center, double distanceInMeters);

}