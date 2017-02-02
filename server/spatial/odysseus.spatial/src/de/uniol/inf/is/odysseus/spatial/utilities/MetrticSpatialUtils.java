package de.uniol.inf.is.odysseus.spatial.utilities;

import java.awt.geom.Point2D;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * This class provides methods used for spatial calculations.
 * 
 * @author Tobias Brandt
 *
 */
public class MetrticSpatialUtils {

	private CoordinateReferenceSystem defaultCrs;

	private static MetrticSpatialUtils instance;

	public static MetrticSpatialUtils getInstance() {
		if (instance == null) {
			instance = new MetrticSpatialUtils();
		}
		return instance;
	}

	private MetrticSpatialUtils() {
		try {
			// Just to make it explicit: We want lat/lng as the normal case

			// The long URN identifier does not suffer from axis orientation
			// problems as it is fixed. Just EPSG:4326 would work as well, but
			// here we have a more modern, versioned identifier.

			// If some would want to use the pure EPSG code, the following lines
			// would work:
			// CRSAuthorityFactory factory = CRS.getAuthorityFactory(false);
			// defaultCrs =
			// factory.createCoordinateReferenceSystem("EPSG:4326");
			defaultCrs = CRS.decode("urn:ogc:def:crs:epsg:7.1:4326");
		} catch (FactoryException e) {
			e.printStackTrace();
		}
	}

	/**
	 * DEPRECATED: This formula gives results with only limited accuracy (but
	 * works without additional libraries). Use "calculateDistance" instead.
	 * 
	 * Calculates the distance between two points on the earth in km. Uses the
	 * Haversine formula and therefore takes the curvature of the earth into
	 * account. This is especially useful, if the distance between the two
	 * points is long, therefore km instead of meters are used as output.
	 * 
	 * @param lat1
	 *            Latitude value from the first point
	 * @param lng1
	 *            Longitude value from the first point
	 * @param lat2
	 *            Latitude value from the second point
	 * @param lng2
	 *            Longitude value from the second point
	 * @return The distance between the two points in km
	 */
	@Deprecated
	public double getHaversineDistance(double lat1, double lng1, double lat2, double lng2) {
		// This calculation is based on HEVERSINE formula
		double earthRadius = 6371; // earthRadius in Kilometers
		double dLat = Math.toRadians(lat1 - lat2);
		double dLng = Math.toRadians(lng1 - lng2);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat2))
				* Math.cos(Math.toRadians(lat1)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = earthRadius * c;
		return distance;
	}

	public double calculateHaversineDistance(Coordinate coord1, Coordinate coord2) {
		return getHaversineDistance(coord1.y, coord1.x, coord2.y, coord2.x);
	}

	/**
	 * Calculates the distance between two points in meters in the WGS84
	 * coordinate reference system (e.g. for GPS coordinates).
	 * 
	 * @param coord1
	 *            One point for distance calculation.
	 * @param coord2
	 *            Other point for distance calculation.
	 * @return The distance between the two points in meters.
	 */
	public double calculateDistance(Coordinate coord1, Coordinate coord2) {
		return calculateDistance(defaultCrs, coord1, coord2);
	}

	/**
	 * Calculates the distance between two points in meters in the given
	 * coordinate reference system. If crs is null, WGS84 is used as default.
	 * 
	 * @param crs
	 *            The coordinate reference system. If null, WGS84 is used as
	 *            default.
	 * @param coord1
	 *            One point for distance calculation.
	 * @param coord2
	 *            Other point for distance calculation.
	 * @return The distance between the two points in meters.
	 */
	public double calculateDistance(CoordinateReferenceSystem crs, Coordinate coord1, Coordinate coord2) {
		if (crs == null) {
			crs = defaultCrs;
		}

		// distance in meters
		double distance = 0.0;

		try {
			distance = JTS.orthodromicDistance(coord1, coord2, crs);
		} catch (TransformException e) {
			e.printStackTrace();
		}

		return distance;

	}

	public Envelope getEnvelopeForRadius(Coordinate center, double rangeMeters) {

		org.geotools.referencing.GeodeticCalculator calc = new org.geotools.referencing.GeodeticCalculator();
		// mind, this is lon/lat
		calc.setStartingGeographicPoint(center.y, center.x);

		// get upper left point
		// go to the north
		calc.setDirection(0 /* azimuth */, rangeMeters/* distance */);
		calc.setStartingGeographicPoint(calc.getDestinationGeographicPoint());
		// go to the west
		calc.setDirection(270, rangeMeters);
		Point2D upperLeft = calc.getDestinationGeographicPoint();

		// go to upper right point
		calc.setStartingGeographicPoint(upperLeft);
		// from the top left go 2 times the range to the upper right
		calc.setDirection(90, 2 * rangeMeters);
		Point2D upperRight = calc.getDestinationGeographicPoint();

		// go to lower right point
		calc.setStartingGeographicPoint(upperRight);
		// from the upper right go 2 times the range to the lower right
		calc.setDirection(180, 2 * rangeMeters);
		Point2D lowerRight = calc.getDestinationGeographicPoint();

		// Create an envelope from these three points
		Envelope env = new Envelope();
		env.expandToInclude(upperLeft.getY(), upperLeft.getX());
		env.expandToInclude(upperRight.getY(), upperRight.getX());
		env.expandToInclude(lowerRight.getY(), lowerRight.getX());

		return env;
	}

}
