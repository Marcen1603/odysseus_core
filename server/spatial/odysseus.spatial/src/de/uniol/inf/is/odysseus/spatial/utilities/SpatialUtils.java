package de.uniol.inf.is.odysseus.spatial.utilities;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Point;

/**
 * This class provides methods used for spatial calculations.
 * 
 * @author Tobias Brandt
 *
 */
public class SpatialUtils {

	private static DefaultGeographicCRS defaultCrs = DefaultGeographicCRS.WGS84;

	/**
	 * Calculates the distance between two points on the earth in km. Uses the
	 * Heversine formula and therefore takes the curvature of the earth into
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
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
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

	public static double calculateDistance(CoordinateReferenceSystem crs, Point point1, Point point2) {
		if (crs == null) {
			crs = defaultCrs;
		}
		double distance = 0.0;
		
		try {
			distance = JTS.orthodromicDistance(point1.getCoordinate(), point2.getCoordinate(), crs);
		} catch (TransformException e) {
			e.printStackTrace();
		}
		
		return distance;
		
	}

}
