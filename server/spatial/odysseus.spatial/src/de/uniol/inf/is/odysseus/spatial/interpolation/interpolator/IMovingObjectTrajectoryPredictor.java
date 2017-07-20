package de.uniol.inf.is.odysseus.spatial.interpolation.interpolator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;

public interface IMovingObjectTrajectoryPredictor {
	
	public void addLocation(LocationMeasurement locationMeasurement,
			IStreamObject<? extends IMetaAttribute> streamElement);
	
	/**
	 * Predicts the trajectory of an element up until the given time in
	 * {@param endPointInTime}.
	 * 
	 * @param movingObjectId
	 *            The id of the moving object you want to predict the trajectory for
	 * @param endPointInTime
	 *            The time up until you want to predict the object to. The maximum
	 *            timestamp of the predicted trajectory will have the given
	 *            pointInTime.
	 * @param timeStepMs
	 *            The granularity of the trajectory. The time-distance between the
	 *            TrajectoryElements in the Trajectory will be the given time step
	 *            in milliseconds.
	 * @return The last element of the predicted trajectory. The elements are
	 *         connected so that the elements before can be accessed.
	 */
	public TrajectoryElement predictTrajectory(String movingObjectId, PointInTime endPointInTime, long timeStepMs);

	public Map<String, TrajectoryElement> predictAllTrajectories(PointInTime endPointInTime, long timeStep);

}
