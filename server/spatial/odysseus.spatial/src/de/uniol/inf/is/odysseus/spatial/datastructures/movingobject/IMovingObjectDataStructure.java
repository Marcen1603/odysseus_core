package de.uniol.inf.is.odysseus.spatial.datastructures.movingobject;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;
import de.uniol.inf.is.odysseus.spatial.datatype.SpatioTemporalQueryResult;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;

public interface IMovingObjectDataStructure {

	/**
	 * Adds an object to the data structure
	 * 
	 * @param o
	 *            The object to add to the data structure
	 */
	public void add(LocationMeasurement locationMeasurement, IStreamObject<? extends IMetaAttribute> streamElement);

	/**
	 * The name of the data structure so that it can be accessed, e.g. via the
	 * SpatialDataStructureProvider.
	 * 
	 * @return The name of the data structure
	 */
	public String getName();

	/**
	 * Calculates the k nearest neighbors of the given geometry and returns a list
	 * of tuples. The list can be smaller, if there are not enough neighbors (i.e.
	 * if the total list of elements is smaller than k).
	 * 
	 * @param geometry
	 *            The geometry for which the k nearest neighbors are searched
	 * 
	 * @param k
	 *            The number of neighbors
	 * 
	 * @param t
	 *            TODO This needs to be changed to a point in time and searching
	 *            needs to be done with interpolation
	 * 
	 *            The time interval of the element where we search the neighbors
	 *            for. Only neighbors are considered which intervals overlap this
	 *            time interval.
	 * 
	 * @return A list of tuples which are the nearest neighbors of the given
	 *         geometry
	 */
	public Map<String, TrajectoryElement> queryKNN(Geometry geometry, int k, ITimeInterval t);

	/**
	 * Calculates all neighbors within the given radius around the geometry.
	 * 
	 * @param geometry
	 *            The geometry around which you want to know the neighborhood
	 * @param radius
	 *            The radius in meters around the geometry
	 * @param t
	 *            The time interval of the element where we search the neighbors
	 *            for. Only neighbors are considered which intervals overlap this
	 *            time interval.
	 * @return A list with all tuples for which their geometry is in the
	 *         neighborhood around the given geometry
	 */
	public Map<String, List<ResultElement>> queryCircle(Geometry geometry, double radius, ITimeInterval t);

	/**
	 * A circle query with a given candidate collection.
	 * 
	 * @param geometry
	 *            The center of the circle to query
	 * @param radius
	 *            The radius of the circle to query
	 * @param t
	 *            The time interval that the returned elements have to intersect
	 * @param movingObjectIdToIgnore
	 *            results with this id won't be included in the resultlist
	 * @return All elements from the candidates that are in the given circle.
	 */
	public Map<String, List<ResultElement>> queryCircle(Geometry geometry, double radius, ITimeInterval t,
			String movingObjectIdToIgnore);

	/**
	 * A query on the whole trajectory of a moving object. Checks the distance to
	 * every other moving object by interpolating the location of the other moving
	 * objects at the points in time where we know the location of this object at.
	 * 
	 * @param movingObjectID
	 *            The ID of the object we want to know the neighbors from
	 * @param radius
	 *            The radius around the moving object
	 * @return A map with the other moving objects which are within the given
	 *         radius. The map contains: ID of the other moving object -> List of
	 *         locations where the other moving object is within the range (possibly
	 *         interpolated)
	 */
	public Map<String, List<SpatioTemporalQueryResult>> queryCircleTrajectory(String movingObjectID, double radius);

	/**
	 * Queries the data structure and returns all data points which are in a polygon
	 * with the edges at the given coordinates
	 * 
	 * @param coordinates
	 *            The points of the edges of the polygon where you want to search in
	 * @param t
	 *            The time interval of the element where we search the neighbors
	 *            for. Only neighbors are considered which intervals overlap this
	 *            time interval.
	 * @return A list of tuples which lie within the given polygon
	 */
	public Map<String, List<TrajectoryElement>> queryBoundingBox(List<Point> coordinates, ITimeInterval t);

	/**
	 * 
	 * @return The position in the tuple where the geometry can be found
	 */
	public int getGeometryPosition();

	/**
	 * 
	 * @return The Ids from all moving objects currently in the data structure.
	 */
	public Set<String> getAllMovingObjectIds();

}
