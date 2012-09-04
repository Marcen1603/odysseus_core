package de.offis.salsa.obsrec.objrules;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

import de.offis.salsa.obsrec.annotations.ObjectRule;
import de.offis.salsa.obsrec.models.ObjectType;
import de.offis.salsa.obsrec.util.Util;

@ObjectRule(typeCategory = ObjectType.GERADE, name = "GeradePoint2Point")
public class StraightP2PObjRule implements IObjectRule {
	
	private static final int TOLERANCE = 25;
	
	@Override
	public ObjectType getType() {
		return ObjectType.GERADE;
	}

	@Override
	public double getTypeAffinity(LineString segment) {
		Coordinate first = segment.getCoordinateN(0);
		double firstX = first.x;
		double firstY = first.y;
		Coordinate last = segment.getCoordinateN(segment.getNumPoints() - 1);
		double lastX = last.x;
		double lastY = last.y;

		// das erste und letzte element nicht iterieren ...
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (int i = 1; i < segment.getCoordinates().length - 1; i += 2) {
			Coordinate current = segment.getCoordinateN(i);
			double currentX = current.x;
			double currentY = current.y;
			
			double b = Point2D.distance(firstX, firstY, currentX, currentY);
			double c = Point2D.distance(currentX, currentY, lastX, lastY);
			double a = Point2D.distance(firstX, firstY, lastX, lastY);
			
			double cos_v = (Math.pow(a, 2) - Math.pow(b, 2) - Math.pow(c, 2))/(-2 * b * c);
			double acos = Math.acos(cos_v);
			double v = Math.toDegrees(acos);
			
			stats.addValue(v);
		}
		
		double upperBound = 180 + TOLERANCE;
		double lowerBound = 180 - TOLERANCE;
		double mean = stats.getMean();
		if( mean < upperBound && mean > lowerBound){
			
			double peak = (upperBound - lowerBound)/2.0;
			double normVal = mean - lowerBound;
			
			if(normVal <= peak){
				return peak * normVal;
			} else {
				return -peak * normVal + 1.0;
			}
		} else {
			return 0.0;
		}
	}

	public Polygon getPredictedPolygon(LineString segment){
				
		int x1 = (int)segment.getCoordinateN(0).x;
		int y1 = (int)segment.getCoordinateN(0).y;
		int x2 = (int)segment.getCoordinateN(segment.getNumPoints()-1).x;
		int y2 = (int)segment.getCoordinateN(segment.getNumPoints()-1).y;
				
		double L = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
		double offsetPixels = 10.0;
				
		int x1p = (int) (x1 + offsetPixels * (y2-y1) / L);
		int x2p = (int) (x2 + offsetPixels * (y2-y1) / L);
		int y1p = (int) (y1 + offsetPixels * (x1-x2) / L);
		int y2p = (int) (y2 + offsetPixels * (x1-x2) / L);
		
		List<Coordinate> coords = new ArrayList<Coordinate>();
		coords.add(new Coordinate(x1, y1));
		coords.add(new Coordinate(x2, y2));
		coords.add(new Coordinate(x2p, y2p));
		coords.add(new Coordinate(x1p, y1p));
		return Util.createPolygon(coords);
	}
}
