package de.offis.salsa.obsrec.objrules;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.TrackedObject.Type;
import de.offis.salsa.obsrec.annotations.ObjectRule;

@ObjectRule(objectType = Type.KONKAV, name = "StandardKonkav")
public class KonkavObjRule extends AbstractObjRule {

	
	@Override
	public Type getType() {
		return Type.KONKAV;
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
			
			if(firstY < currentY || lastY < currentY){
				v = 360 - v;
			}
			
			stats.addValue(v);
		}
		
		if(stats.getMin() > 180){
			// TODO feingranulare wahrscheinlichkeiten
			
			return 1.0;
		} else {
			return 0.0;
		}
	}


	public Polygon getPredictedPolygon(List<Sample> segment){
		Polygon result = new Polygon();
		
		Integer maxX = null;
		Integer maxY = null;
		
		for(Sample s : segment){
			if(maxX == null)
				maxX = (int) s.getX();
			
			if(maxY == null)
				maxY = (int) s.getY();
			
			if(maxX < s.getX())
				maxX = (int) s.getX();
			
			if(maxY < s.getY())
				maxY = (int) s.getY();
			
			result.addPoint((int)s.getX(), (int)s.getY());
		}
		
		result.addPoint(maxX, maxY);
		
		return result;
	}
}
