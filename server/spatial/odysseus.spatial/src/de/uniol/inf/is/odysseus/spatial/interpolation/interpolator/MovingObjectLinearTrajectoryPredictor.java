package de.uniol.inf.is.odysseus.spatial.interpolation.interpolator;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.geotools.referencing.GeodeticCalculator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;

public class MovingObjectLinearTrajectoryPredictor implements IMovingObjectTrajectoryPredictor {

	private Map<String, LocationMeasurement> lastMovingObjectLocations;
	private TimeUnit baseTimeUnit;
	private GeodeticCalculator geodeticCalculator;

	public MovingObjectLinearTrajectoryPredictor(TimeUnit baseTimeUnit) {
		this.lastMovingObjectLocations = new HashMap<>();
		this.baseTimeUnit = baseTimeUnit;
		this.geodeticCalculator = new GeodeticCalculator();
	}

	@Override
	public void addLocation(LocationMeasurement locationMeasurement,
			IStreamObject<? extends IMetaAttribute> streamElement) {
		// TODO Use index structure
		this.lastMovingObjectLocations.put(locationMeasurement.getMovingObjectId(), locationMeasurement);
	}

	@Override
	public TrajectoryElement predictTrajectory(String movingObjectId, PointInTime endPointInTime, long timeStepMs) {

		// Get the last measurement
		LocationMeasurement locationMeasurement = lastMovingObjectLocations.get(movingObjectId);

		// Calculate the waypoints between start and destination
		long timeToTravelMs = this.baseTimeUnit
				.toMillis(endPointInTime.minus(locationMeasurement.getMeasurementTime()).getMainPoint());
		long timeTravelledMs = 0;

		double lastLongitude = locationMeasurement.getLongitude();
		double lastLatitude = locationMeasurement.getLatitude();

		TrajectoryElement firstElement = null;
		TrajectoryElement previousElement = null;

		while (timeTravelledMs < timeToTravelMs) {

			if (timeTravelledMs + timeStepMs > timeToTravelMs) {
				// Don't go too far
				timeStepMs = timeToTravelMs - timeTravelledMs;
			}

			double distanceTravelledInMeters = locationMeasurement.getSpeedInMetersPerSecond() * (timeStepMs / 1000);

			// Calculate the new in-between location
			this.geodeticCalculator.setStartingGeographicPoint(lastLongitude, lastLatitude);
			this.geodeticCalculator.setDirection(locationMeasurement.getHorizontalDirection(), distanceTravelledInMeters);
			Point2D waypointDestination = this.geodeticCalculator.getDestinationGeographicPoint();
			PointInTime timeOfLocation = new PointInTime(endPointInTime.getMainPoint() - (timeToTravelMs - timeTravelledMs));

			// Save it as a part of a trajectory
			TrajectoryElement element = new TrajectoryElement(previousElement, movingObjectId,
					waypointDestination.getY(), waypointDestination.getX(), timeOfLocation, null);
			previousElement = element;
			
			// Save the first element of the trajectory
			if (firstElement == null) {
				firstElement = element;
			}

			lastLongitude = waypointDestination.getX();
			lastLatitude = waypointDestination.getY();
			timeTravelledMs += timeStepMs;
		}

		// Return the first element of the trajectory (the start)
		return firstElement;
	}

	@Override
	public Map<String, TrajectoryElement> predictAllTrajectories(PointInTime endPointInTime, long timeStepMs) {

		Map<String, TrajectoryElement> allPredictions = new HashMap<>(this.lastMovingObjectLocations.size());

		for (String movingObjectId : this.lastMovingObjectLocations.keySet()) {
			allPredictions.put(movingObjectId, this.predictTrajectory(movingObjectId, endPointInTime, timeStepMs));
		}

		return allPredictions;
	}

}
