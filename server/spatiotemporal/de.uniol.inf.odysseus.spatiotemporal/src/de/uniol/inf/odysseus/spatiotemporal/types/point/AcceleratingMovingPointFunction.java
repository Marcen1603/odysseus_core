package de.uniol.inf.odysseus.spatiotemporal.types.point;

import java.awt.geom.Point2D;

import org.geotools.referencing.GeodeticCalculator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;

public class AcceleratingMovingPointFunction implements TemporalFunction<GeometryWrapper> {

	protected Geometry basePoint;
	protected PointInTime basePointInTime;
	protected double speedMeterPerTimeInstance;
	protected double accelerationPerTimeInstance;
	// azimuth = direction
	protected double azimuth;

	/**
	 * 
	 * @param basePoint
	 *            The point from which the calculations are done. The location at
	 *            which the object is at {@code basePointInTime}.
	 * @param basePointInTime
	 *            The time at which the object if at the location {@code basePoint}
	 * @param speedMetersPerTimeInstance
	 *            The meters the object travels per time instance
	 * @param azimuth
	 *            The direction in which the object travels
	 */
	public AcceleratingMovingPointFunction(Geometry basePoint, PointInTime basePointInTime,
			double speedMetersPerTimeInstance, double accelerationPerTimeInstance, double azimuth) {
		this.basePoint = basePoint;
		this.basePointInTime = basePointInTime;
		this.speedMeterPerTimeInstance = speedMetersPerTimeInstance;
		this.accelerationPerTimeInstance = accelerationPerTimeInstance;
		this.azimuth = azimuth;
	}

	public AcceleratingMovingPointFunction(AcceleratingMovingPointFunction other) {
		this.basePoint = other.basePoint;
		this.basePointInTime = other.basePointInTime;
		this.speedMeterPerTimeInstance = other.speedMeterPerTimeInstance;
		this.accelerationPerTimeInstance = other.accelerationPerTimeInstance;
		this.azimuth = other.azimuth;
	}

	@Override
	public GeometryWrapper getValue(PointInTime time) {
		Geometry destination = calculateLocationAtTime(time);
		return new GeometryWrapper(destination);
	}

	/**
	 * Calculates the location of the object of this function at the given point in
	 * time.
	 */
	private Geometry calculateLocationAtTime(PointInTime time) {
		double timeTravelled = time.minus(basePointInTime).getMainPoint();
		// formula for distance with acceleration and initial velocity
		double distanceMeters = (0.5 * accelerationPerTimeInstance * timeTravelled * timeTravelled)
				+ (speedMeterPerTimeInstance * timeTravelled);
		if (Double.isNaN(distanceMeters)) {
			distanceMeters = 0;
		}

		GeodeticCalculator geodeticCalculator = new GeodeticCalculator();
		double latitude = basePoint.getCentroid().getX();
		double longitude = basePoint.getCentroid().getY();
		geodeticCalculator.setStartingGeographicPoint(longitude, latitude);
		geodeticCalculator.setDirection(azimuth, distanceMeters);
		Point2D destinationGeographicPoint = geodeticCalculator.getDestinationGeographicPoint();

		Geometry destination = GeometryFactory.createPointFromInternalCoord(
				new Coordinate(destinationGeographicPoint.getY(), destinationGeographicPoint.getX()), this.basePoint);
		return destination;
	}

	@Override
	public AcceleratingMovingPointFunction clone() {
		return new AcceleratingMovingPointFunction(this);
	}

	@Override
	public String toString() {
		return "speed: " + speedMeterPerTimeInstance + "m/timeInstance; acceleration: " + accelerationPerTimeInstance
				+ "m/timeInstance^2; azimuth: " + azimuth;
	}

}
