package de.uniol.inf.is.odysseus.spatial.datastructures.movingobject;

import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;

public interface IMovingObjectDataStructure {

	/**
	 * Adds an object to the data structure
	 * 
	 * @param o
	 *            The object to add to the data structure
	 */
	public void add(Object o);

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
	 *            TODO This needs to be changed to a point in time and searching
	 *            needs to be done with interpolation
	 * 
	 *            The time interval of the element where we search the neighbors
	 *            for. Only neighbors are considered which intervals overlap
	 *            this time interval.
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
	 *            for. Only neighbors are considered which intervals overlap
	 *            this time interval.
	 * @return A list with all tuples for which their geometry is in the
	 *         neighborhood around the given geometry
	 */
	public Map<String, List<ResultElement>> queryCircle(Geometry geometry, double radius, ITimeInterval t);

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
	public Map<String, List<TrajectoryElement>> queryBoundingBox(List<Point> coordinates, ITimeInterval t);

	/**
	 * 
	 * @return The position in the tuple where the geometry can be found
	 */
	public int getGeometryPosition();

}
