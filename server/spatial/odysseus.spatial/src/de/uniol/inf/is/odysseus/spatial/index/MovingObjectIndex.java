package de.uniol.inf.is.odysseus.spatial.index;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.spatial.datatype.SpatioTemporalQueryResult;

public interface MovingObjectIndex {

	/**
	 * A radius query on the latest known location of a moving object. For the other
	 * objects, the location to that point in time is predicted.
	 * 
	 * @param movingObjectID
	 *            The ID of the object we want to know the neighbors from
	 * @param radius
	 *            The radius around the moving object
	 * @return A map with the other moving objects which are within the given
	 *         radius. The map contains: ID of the other moving object -> Location
	 *         where the other moving object probably is at that point in time
	 */
	public Map<String, SpatioTemporalQueryResult> queryPredictedCircle(String movingObjectID, double radius);

	/**
	 * A radius query on the whole trajectory of a moving object. Checks the
	 * distance to every other moving object by interpolating the location of the
	 * other moving objects at the points in time where we know the location of this
	 * object at.
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

}
