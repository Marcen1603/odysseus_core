package de.offis.salsa.obsrec.objrules;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

import de.offis.salsa.lms.model.Sample;
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
	public double getTypeAffinity(List<Sample> segment) {
		Sample first = segment.get(0);
		double firstX = first.getX();
		double firstY = first.getY();
		Sample last = segment.get(segment.size()-1);
		double lastX = last.getX();
		double lastY = last.getY();
		
		// das erste und letzte element nicht iterieren ...		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for(int i = 1 ; i < segment.size()-1 ; i += 2){
			Sample current = segment.get(i);
			double currentX = current.getX();
			double currentY = current.getY();
			
			double b = Point2D.distance(firstX, firstY, currentX, currentY);
			double c = Point2D.distance(currentX, currentY, lastX, lastY);
			double a = Point2D.distance(firstX, firstY, lastX, lastY);
			
			double cos_v = (Math.pow(a, 2) - Math.pow(b, 2) - Math.pow(c, 2))/(-2 * b * c);
			double acos = Math.acos(cos_v);
			double v = Math.toDegrees(acos);
			
			stats.addValue(v);
		}
		
		int upperBound = 180 + TOLERANCE;
		int lowerBound = 180 - TOLERANCE;
		double mean = stats.getMean();
		if( mean < upperBound && mean > lowerBound){
			// TODO feingranulare wahrscheinlichkeiten
			return 1.0;
		} else {
			return 0.0;
		}
	}

	public Polygon getPredictedPolygon(List<Sample> segment){
		// TODO ausgleichsgrade benutzen!
				
//		 var x1 = ..., x2 = ..., y1 = ..., y2 = ... // The original line
//				    var L = Math.Sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))
//
//				    var offsetPixels = 10.0
//
//				    // This is the second line
//				    var x1p = x1 + offsetPixels * (y2-y1) / L
//				    var x2p = x2 + offsetPixels * (y2-y1) / L
//				    var y1p = y1 + offsetPixels * (x1-x2) / L
//				    var y2p = y2 + offsetPixels * (x1-x2) / L
				
		int x1 = (int)segment.get(0).getX();
		int y1 = (int)segment.get(0).getY();
		int x2 = (int)segment.get(segment.size()-1).getX();
		int y2 = (int)segment.get(segment.size()-1).getY();
				
		double L = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
		double offsetPixels = 10.0;
				
		int x1p = (int) (x1 + offsetPixels * (y2-y1) / L);
		int x2p = (int) (x2 + offsetPixels * (y2-y1) / L);
		int y1p = (int) (y1 + offsetPixels * (x1-x2) / L);
		int y2p = (int) (y2 + offsetPixels * (x1-x2) / L);
		
//		Polygon p = new Polygon();
//		p.addPoint(x1, y1);
//		p.addPoint(x2, y2);
////p.addPoint(0, 0);
//
//		p.addPoint(x2p, y2p);
//		p.addPoint(x1p, y1p);
//		
//		return p;
		
		List<Coordinate> coords = new ArrayList<Coordinate>();
		coords.add(new Coordinate(x1, y1));
		coords.add(new Coordinate(x2, y2));
		coords.add(new Coordinate(x2p, y2p));
		coords.add(new Coordinate(x1p, y1p));
		return Util.createPolygon(coords);
	}
}
