package de.uniol.inf.is.odysseus.spatial.interpolation.interpolator;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.geotools.referencing.GeodeticCalculator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;

public class MovingObjectLinearLocationPredictor implements IMovingObjectLocationPredictor {

	private Map<String, TrajectoryElement> lastMovingObjectLocations;
	private TimeUnit baseTimeUnit;

	public MovingObjectLinearLocationPredictor(TimeUnit baseTimeUnit) {
		this.lastMovingObjectLocations = new HashMap<>();
		this.baseTimeUnit = baseTimeUnit;
	}

	@Override
	public void addLocation(TrajectoryElement trajectoryElement,
			IStreamObject<? extends IMetaAttribute> streamElement) {
		lastMovingObjectLocations.put(trajectoryElement.getMovingObjectID(), trajectoryElement);
	}

	@Override
	public TrajectoryElement predictLocation(String movingObjectId, PointInTime time) {

		// Get the last measurement
		TrajectoryElement trajectoryElement = lastMovingObjectLocations.get(movingObjectId);

		// Calculate how far the object may has moved
		long secondsTravelled = baseTimeUnit
				.toSeconds(time.minus(trajectoryElement.getMeasurementTime()).getMainPoint());
		double distanceInMeters = trajectoryElement.getSpeed(this.baseTimeUnit) * secondsTravelled;

		// Calculate the new location
		GeodeticCalculator geodeticCalculator = new GeodeticCalculator();
		geodeticCalculator.setStartingGeographicPoint(trajectoryElement.getLongitude(),
				trajectoryElement.getLatitude());
		geodeticCalculator.setDirection(trajectoryElement.getAzimuth(), distanceInMeters);
		Point2D destinationGeographicPoint = geodeticCalculator.getDestinationGeographicPoint();

		TrajectoryElement destination = new TrajectoryElement(trajectoryElement, movingObjectId, destinationGeographicPoint.getY(),
				destinationGeographicPoint.getX(), time, null);
		return destination;
	}

	@Override
	public Map<String, TrajectoryElement> predictAllLocations(PointInTime time) {
		Map<String, TrajectoryElement> allInterpolatedLocations = new HashMap<>();
		this.lastMovingObjectLocations.values().stream()
				.forEach(trajectoryElement -> allInterpolatedLocations.put(trajectoryElement.getMovingObjectID(),
						predictLocation(trajectoryElement.getMovingObjectID(), time)));
		return allInterpolatedLocations;
	}

}
