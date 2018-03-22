package de.uniol.inf.odysseus.spatiotemporal.types.point;

import java.awt.geom.Point2D;
import java.util.concurrent.TimeUnit;

import org.geotools.referencing.GeodeticCalculator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;

public class LinearMovingPointFunction implements TemporalFunction<GeometryWrapper> {

	private Geometry basePoint;
	private PointInTime basePointInTime;
	private TimeUnit baseTimeUnit;
	private double speedMeterPerSecond;
	// azimuth = direction
	private double azimuth;

	public LinearMovingPointFunction(Geometry basePoint, PointInTime basePointInTime, double speedMetersPerSecond,
			TimeUnit baseTimeUnit, double azimuth) {
		this.basePoint = basePoint;
		this.basePointInTime = basePointInTime;
		this.speedMeterPerSecond = speedMetersPerSecond;
		this.baseTimeUnit = baseTimeUnit;
		this.azimuth = azimuth;
	}

	@Override
	public GeometryWrapper getValue(PointInTime time) {
		long secondsTravelled = baseTimeUnit.toSeconds(time.minus(basePointInTime).getMainPoint());
		double distanceMeters = speedMeterPerSecond * secondsTravelled;

		GeodeticCalculator geodeticCalculator = new GeodeticCalculator();
		double latitude = basePoint.getCentroid().getX();
		double longitude = basePoint.getCentroid().getY();
		geodeticCalculator.setStartingGeographicPoint(longitude, latitude);
		geodeticCalculator.setDirection(azimuth, distanceMeters);
		Point2D destinationGeographicPoint = geodeticCalculator.getDestinationGeographicPoint();
		
		Geometry destination = GeometryFactory.createPointFromInternalCoord(
				new Coordinate(destinationGeographicPoint.getX(), destinationGeographicPoint.getY()), this.basePoint);
		return new GeometryWrapper(destination);
	}

}
