package de.uniol.inf.odysseus.spatiotemporal.types.point;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.geotools.referencing.GeodeticCalculator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;

/**
 * 
 * A temporal function for a moving point which knows the trajectory of a moving
 * object beforehand, for example, from a navigation system.
 * 
 * @author Tobias Brandt
 *
 */
public class TrajectoryMovingPointFunction implements TemporalFunction<GeometryWrapper> {

	// The times and locations of the trajectory, ordered from oldest to newest
	private List<Long> times;
	private List<Point> locations;

	public TrajectoryMovingPointFunction(List<Long> times, List<Point> locations) {
		this.times = times;
		this.locations = locations;
	}

	@Override
	public GeometryWrapper getValue(PointInTime time) {

		// Search for the correct part

		/*
		 * Simply use the last known location, because predicting into infinite future
		 * is not possible.
		 */
		if (time.isInfinite()) {
			Point point = locations.get(locations.size() - 1);
			GeometryWrapper wrapper = new GeometryWrapper(point);
			return wrapper;
		}

		// Search for the index at which this time would be inserted
		int resultIndex = Collections.binarySearch(times, time.getMainPoint(), new Comparator<Long>() {

			@Override
			public int compare(Long arg0, Long arg1) {
				return Long.compare(arg0, arg1);
			}
		});

		// Exact result? We know the location
		if (resultIndex > 0) {
			Point point = locations.get(resultIndex);
			GeometryWrapper wrapper = new GeometryWrapper(point);
			return wrapper;
		}

		/*
		 * First element? We need to use the first and second location for calculate
		 * backwards.
		 */
		boolean turnAzimuth = false;
		if (resultIndex == 0) {
			/*
			 * Turn azimuth by 180 degree (we calculate the movement backwards, not
			 * forwards)
			 */
			resultIndex = 1;
			turnAzimuth = true;
		}

		// Standard case: between two points
		int insertIndex = (resultIndex * (-1)) - 1;
		Point previousLocation = locations.get(insertIndex - 1);
		Point nextLocation = locations.get(insertIndex);

		// Straight line between previous and next location
		GeodeticCalculator geodeticCalculator = getGeodeticCalculator(previousLocation, nextLocation);
		long timeInstancesBetweenKnownPoints = times.get(insertIndex) - times.get(insertIndex - 1);
		double metersTravelled = geodeticCalculator.getOrthodromicDistance();
		double speedMetersPerTimeInstance = metersTravelled / timeInstancesBetweenKnownPoints;
		if (Double.isNaN(speedMetersPerTimeInstance) || Double.isInfinite(speedMetersPerTimeInstance)) {
			speedMetersPerTimeInstance = 0;
		}
		double azimuth = geodeticCalculator.getAzimuth();
		if (turnAzimuth) {
			/*
			 * Don't calculate from previous to next but from next to previous to calculate
			 * a location before the first one
			 */
			azimuth = (azimuth + 180) % 360;
		}
		TemporalFunction<GeometryWrapper> temporalPointFunction = new LinearMovingPointFunction(previousLocation,
				new PointInTime(times.get(insertIndex - 1)), speedMetersPerTimeInstance, azimuth);
		return temporalPointFunction.getValue(time);
	}

	protected GeodeticCalculator getGeodeticCalculator(Coordinate from, Coordinate to) {
		GeodeticCalculator geodeticCalculator = new GeodeticCalculator();
		double startLongitude = from.y;
		double startLatitude = from.x;
		geodeticCalculator.setStartingGeographicPoint(startLongitude, startLatitude);

		double destinationLongitude = to.y;
		double destinationLatitude = to.x;
		geodeticCalculator.setDestinationGeographicPoint(destinationLongitude, destinationLatitude);

		return geodeticCalculator;
	}

	protected GeodeticCalculator getGeodeticCalculator(Geometry from, Geometry to) {
		return getGeodeticCalculator(from.getCoordinate(), to.getCoordinate());
	}

	@Override
	public String toString() {
		return "TrajectoryMovingObject: " + locations.size() + " elements";
	}

}
