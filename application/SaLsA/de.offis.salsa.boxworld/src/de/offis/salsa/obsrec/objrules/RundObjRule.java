package de.offis.salsa.obsrec.objrules;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.TrackedObject.Type;

public class RundObjRule extends AbstractObjRule {
	
	public RundObjRule() {
		registerObjRule(this);
	}

	private static final int TOLERANCE = 30;

	@Override
	public Type getType() {
		return Type.RUND;
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
		
		int upperBound = 90 + TOLERANCE;
		int lowerBound = 90 - TOLERANCE;
		double mean = stats.getMean();
		if( mean < upperBound && mean > lowerBound){
			// TODO feingranulare wahrscheinlichkeiten
			return 1.0;
		} else {
			return 0.0;
		}
	}

	@Override
	public Polygon getPredictedPolygon(List<Sample> segment) {
		// TODO Auto-generated method stub
		return new Polygon();
	}

}
