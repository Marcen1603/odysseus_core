package de.uniol.inf.odysseus.spatiotemporal.types.point;

import java.awt.geom.Point2D;

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
	private double speedMeterPerTimeInstance;
	// azimuth = direction
	private double azimuth;

	public LinearMovingPointFunction(Geometry basePoint, PointInTime basePointInTime, double speedMetersPerTimeInstance,
			double azimuth) {
		this.basePoint = basePoint;
		this.basePointInTime = basePointInTime;
		this.speedMeterPerTimeInstance = speedMetersPerTimeInstance;
		this.azimuth = azimuth;
	}

	@Override
	public GeometryWrapper getValue(PointInTime time) {
		long timeTravelled = time.minus(basePointInTime).getMainPoint();
		double distanceMeters = speedMeterPerTimeInstance * timeTravelled;

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
