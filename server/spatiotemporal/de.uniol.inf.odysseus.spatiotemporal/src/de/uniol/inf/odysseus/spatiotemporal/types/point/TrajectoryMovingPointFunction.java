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

public class TrajectoryMovingPointFunction implements TemporalFunction<GeometryWrapper> {

	private List<Long> timeList;
	private List<Point> points;

	public TrajectoryMovingPointFunction(List<Long> timeList, List<Point> points) {
		this.timeList = timeList;
		this.points = points;
	}

	@Override
	public GeometryWrapper getValue(PointInTime time) {

		// Search for the correct part

		/*
		 * Simply use the last known location, because predicting into infinite future
		 * is not possible.
		 */
		if (time.isInfinite()) {
			Point point = points.get(points.size() - 1);
			GeometryWrapper wrapper = new GeometryWrapper(point);
			return wrapper;
		}

		int resultIndex = Collections.binarySearch(timeList, time.getMainPoint(), new Comparator<Long>() {

			@Override
			public int compare(Long arg0, Long arg1) {
				return Long.compare(arg0, arg1);
			}
		});

		// Exact result?
		if (timeList.get(resultIndex).equals(time.getMainPoint())) {
			Point point = points.get(resultIndex);
			GeometryWrapper wrapper = new GeometryWrapper(point);
			return wrapper;
		}

		// First element?
		boolean turnAzimuth = false;
		if (resultIndex == 0) {
			/*
			 * Turn azimuth by 180 degree (we calculate the movement backwards, not
			 * forwards)
			 */
			resultIndex = 1;
			turnAzimuth = true;
		}

		// Between two points
		Point previousLocation = points.get(resultIndex - 1);
		Point nextLocation = points.get(resultIndex);

		GeodeticCalculator geodeticCalculator = getGeodeticCalculator(previousLocation, nextLocation);
		long timeInstancesTravelled = time.getMainPoint() - timeList.get(resultIndex - 1);
		double metersTravelled = geodeticCalculator.getOrthodromicDistance();
		double speedMetersPerTimeInstance = metersTravelled / timeInstancesTravelled;
		if (Double.isNaN(speedMetersPerTimeInstance) || Double.isInfinite(speedMetersPerTimeInstance)) {
			speedMetersPerTimeInstance = 0;
		}
		double azimuth = geodeticCalculator.getAzimuth();
		if (turnAzimuth) {
			azimuth = (azimuth + 180) % 360;
		}
		TemporalFunction<GeometryWrapper> temporalPointFunction = new LinearMovingPointFunction(previousLocation,
				new PointInTime(timeList.get(resultIndex - 1)), speedMetersPerTimeInstance, azimuth);
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
		return "TrajectoryMovingObject: " + points.size() + " elements";
	}

}
