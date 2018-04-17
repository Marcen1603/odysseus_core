package de.uniol.inf.odysseus.spatiotemporal.types.point;

import java.awt.geom.Point2D;

import org.geotools.referencing.GeodeticCalculator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;

/**
 * A function for a temporal point that is moving linearly in space.
 * 
 * @author Tobias Brandt
 *
 */
public class LinearMovingPointFunction implements TemporalFunction<GeometryWrapper> {

	private Geometry basePoint;
	private PointInTime basePointInTime;
	private double speedMeterPerTimeInstance;
	// azimuth = direction
	private double azimuth;

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
	public LinearMovingPointFunction(Geometry basePoint, PointInTime basePointInTime, double speedMetersPerTimeInstance,
			double azimuth) {
		this.basePoint = basePoint;
		this.basePointInTime = basePointInTime;
		this.speedMeterPerTimeInstance = speedMetersPerTimeInstance;
		this.azimuth = azimuth;
	}

	public LinearMovingPointFunction(LinearMovingPointFunction other) {
		this.basePoint = other.basePoint;
		this.basePointInTime = other.basePointInTime;
		this.speedMeterPerTimeInstance = other.speedMeterPerTimeInstance;
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
	 * 
	 * @param time
	 * @return
	 */
	private Geometry calculateLocationAtTime(PointInTime time) {
		long timeTravelled = time.minus(basePointInTime).getMainPoint();
		double distanceMeters = speedMeterPerTimeInstance * timeTravelled;

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
	public LinearMovingPointFunction clone() {
		return new LinearMovingPointFunction(this);
	}

	@Override
	public String toString() {
		return "speed: " + speedMeterPerTimeInstance + "m/timeInstance; azimuth: " + azimuth;
	}

}
