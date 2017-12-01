package de.uniol.inf.is.odysseus.spatial.index;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;

/**
 * Interface for spatial indexes for moving objects.
 * 
 * @author Tobias Brandt
 *
 */
public interface SpatialIndex<T extends IMetaAttribute> {

	/**
	 * Adds a location measurement to the index.
	 * 
	 * @param locationMeasurement
	 *            The location that has to be added to the index
	 * @param streamElement
	 *            The according streaming element
	 */
	public void add(LocationMeasurement locationMeasurement, IStreamObject<? extends T> streamElement);

	/**
	 * Calculates all neighbors within the given radius around the center. Only uses
	 * the latest info from each object. Can filter by time interval.
	 * 
	 * @param centerLatitude
	 *            The latitude of the center of the circle
	 * @param centerLongitude
	 *            The longitude of the center of the circle
	 * @param radius
	 *            The radius in meters around the center
	 * @param interval
	 *            Filters the elements for only the elements within this time
	 *            interval. Put null or an interval (0,inf] to not filter.
	 * @return A map with all elements for which their location is in the
	 *         neighborhood around the center. As only the latest elements are used,
	 *         only one location is possible per moving object. Map: ID of the
	 *         object -> result for that object
	 */
	public Map<String, ResultElement> queryCircleOnLatestElements(double centerLatitude, double centerLongitude,
			double radius, TimeInterval interval);

	/**
	 * Approximates a circle query on the latest element per object in the index.
	 * Results can be inaccurate (especially include more data than in an accurate
	 * query). Avoids distance calculations and only works with the given index
	 * structure, hence improves performance if accuracy is not the main goal.
	 * 
	 * @param centerLatitude
	 *            The latitude of the center of the circle
	 * @param centerLongitude
	 *            The longitude of the center of the circle
	 * @param radius
	 *            The radius in meters around the center
	 * @param interval
	 *            Filters the elements for only the elements within this time
	 *            interval. Put null or an interval (0,inf] to not filter.
	 * @return An approximation of elements within the circle. Map: ID of the object
	 *         -> result for that object
	 */
	public Map<String, TrajectoryElement> approximateCircleOnLatestElements(double centerLatitude, double centerLongitude,
			double radius, TimeInterval interval);

	/**
	 * Searches for the latest known location of the given moving object.
	 * 
	 * @param movingObjectID
	 *            The ID of the moving object to search for
	 * @return The latest known location of that object
	 */
	public TrajectoryElement getLatestLocationOfObject(String movingObjectID);

}
