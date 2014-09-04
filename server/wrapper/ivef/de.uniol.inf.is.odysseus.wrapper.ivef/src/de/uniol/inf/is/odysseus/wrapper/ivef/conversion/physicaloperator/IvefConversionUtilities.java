package de.uniol.inf.is.odysseus.wrapper.ivef.conversion.physicaloperator;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class IvefConversionUtilities {
	/**Spatial Utilities*/
	public static Double calculateBearing(Double ownShipLat, Double ownShipLong, Double targetLat, Double targetLong) {
		double radOwnShipLat = Math.toRadians(ownShipLat);
		double radTargetLat = Math.toRadians(targetLat);
		double longDiff= Math.toRadians(targetLong - ownShipLong);
		double y = Math.sin(longDiff)*Math.cos(radTargetLat);
		double x = Math.cos(radOwnShipLat)*Math.sin(radTargetLat) - Math.sin(radOwnShipLat)*Math.cos(radTargetLat)*Math.cos(longDiff);
		return (Math.toDegrees(Math.atan2(y, x))+360)%360;
	}

	public static Double calculateDistance(Double ownShipLat, Double ownShipLong, Double targetLat, Double targetLong) {
		//This calculation is based on HEVERSINE formula
		double earthRadius = 6371; //earthRadius in Kilometers
	    double dLat = Math.toRadians(targetLat - ownShipLat);
	    double dLng = Math.toRadians(targetLong - ownShipLong);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(ownShipLat)) * Math.cos(Math.toRadians(targetLat)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double distance = earthRadius * c;
	    return distance;
	}
	
	public static Geometry geometryTransform(Geometry sourceGeom, int destGeomID){ 
		Geometry targetGeom = (Geometry) sourceGeom.clone(); 
		targetGeom.setSRID(destGeomID);
		//The transformation
		CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
		CRSFactory csFactory = new CRSFactory();
		CoordinateReferenceSystem crs1 = csFactory.createFromName("EPSG:" + sourceGeom.getSRID());
		CoordinateReferenceSystem crs2 = csFactory.createFromName("EPSG:" + targetGeom.getSRID());
		CoordinateTransform trans = ctFactory.createTransform(crs1, crs2);
		/*
		 * Create input and output points (here we have only one point).
		 */
		ProjCoordinate source = new ProjCoordinate();
		ProjCoordinate target = new ProjCoordinate();
		for (Coordinate sourceCoordinate : targetGeom.getCoordinates()) {
			source.x = sourceCoordinate.x;
			source.y = sourceCoordinate.y;
			source.z = sourceCoordinate.z;

			/*
			 * Transform point
			 */
			trans.transform(source, target);
			sourceCoordinate.x = target.x;
			sourceCoordinate.y = target.y;
			sourceCoordinate.z = target.z;
		}
		targetGeom.geometryChanged();
		return targetGeom;
	}
}
