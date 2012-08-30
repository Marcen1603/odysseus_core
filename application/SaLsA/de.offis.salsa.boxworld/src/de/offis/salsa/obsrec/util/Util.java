package de.offis.salsa.obsrec.util;

import java.util.List;
import java.util.Map.Entry;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;

import de.offis.salsa.obsrec.models.TrackedObject;

public class Util {
	public static GeometryFactory geoFact = new GeometryFactory();
	
	public static String getMaxAffinityType(TrackedObject to){
		Double affnity = -1.0;
		String type = null;
		
		for (Entry<String, Double> t : to.getTypeAffinitys().entrySet()) {
			if (type == null || t.getValue() > affnity) {
				type = t.getKey();
				affnity = t.getValue();
			}
		}
		
		return type;
	}
	
	public static com.vividsolutions.jts.geom.Polygon getMaxAffinityPolygon(TrackedObject to){
		return to.getPolygons().get(Util.getMaxAffinityType(to));
	}
	
	public static com.vividsolutions.jts.geom.Polygon createPolygon(List<Coordinate> coords){
		// FIXME close path, find another solution
		coords.add(new Coordinate(coords.get(0)));
		
		LinearRing r = geoFact.createLinearRing(coords.toArray(new Coordinate[0]));
		return geoFact.createPolygon(r, null);
	}
	
	public static MultiLineString createMultiLineString(){
//		LineString ls = geoFact.createLineString(coordinates);
		return null;
	}
	
	public static java.awt.Polygon convertPolygon(Polygon polygon){
		java.awt.Polygon p = new java.awt.Polygon();
		
		for(Coordinate c : polygon.getCoordinates()){
			p.addPoint((int)c.x, (int)c.y);
		}
		
		return p;
	}
}
