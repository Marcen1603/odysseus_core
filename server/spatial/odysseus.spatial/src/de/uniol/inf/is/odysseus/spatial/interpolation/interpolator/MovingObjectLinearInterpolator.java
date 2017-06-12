package de.uniol.inf.is.odysseus.spatial.interpolation.interpolator;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import org.geotools.referencing.GeodeticCalculator;

public class MovingObjectLinearInterpolator implements IMovingObjectInterpolator {

	private Map<String, LocationMeasurement> lastMovingObjectLocations;
	private TimeUnit baseTimeUnit;

	public MovingObjectLinearInterpolator(TimeUnit baseTimeUnit) {
		this.lastMovingObjectLocations = new HashMap<>();
		this.baseTimeUnit = baseTimeUnit;
	}

	@Override
	public void addLocation(LocationMeasurement locationMeasurement) {
		lastMovingObjectLocations.put(locationMeasurement.getMovingObjectId(), locationMeasurement);
	}

	@Override
	public LocationMeasurement calcLocation(String movingObjectId, PointInTime time) {

		// Get the last measurement
		LocationMeasurement locationMeasurement = lastMovingObjectLocations.get(movingObjectId);

		// Calculate how far the object may has moved
		long secondsTravelled = baseTimeUnit
				.toSeconds(time.minus(locationMeasurement.getMeasurementTime()).getMainPoint());
		double distanceInMeters = locationMeasurement.getSpeedInMetersPerSecond() * secondsTravelled;

		// Calculate the new location
		GeodeticCalculator geodeticCalculator = new GeodeticCalculator();
		geodeticCalculator.setStartingGeographicPoint(locationMeasurement.getLongitude(),
				locationMeasurement.getLatitude());
		geodeticCalculator.setDirection(locationMeasurement.getHorizontalDirection(), distanceInMeters);
		Point2D destinationGeographicPoint = geodeticCalculator.getDestinationGeographicPoint();

		LocationMeasurement destination = new LocationMeasurement(destinationGeographicPoint.getY(),
				destinationGeographicPoint.getX(), locationMeasurement.getHorizontalDirection(),
				locationMeasurement.getSpeedInMetersPerSecond(), time, movingObjectId);
		return destination;
	}

	@Override
	public Map<String, LocationMeasurement> calcAllLocations(PointInTime time) {
		Map<String, LocationMeasurement> allInterpolatedLocations = new HashMap<>();
		lastMovingObjectLocations.values().stream()
				.forEach(locationMeasurment -> allInterpolatedLocations.put(locationMeasurment.getMovingObjectId(),
						calcLocation(locationMeasurment.getMovingObjectId(), time)));
		return allInterpolatedLocations;
	}

}
